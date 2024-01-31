package com.ptglue.common.user.controller;
import com.ptglue.basic.exception.InternalServerErrorException;
import com.ptglue.basic.exception.NotFoundException;
import com.ptglue.basic.model.dto.ApiResponseDto;
import com.ptglue.basic.service.CommonService;
import com.ptglue.common.user.enums.SnsTypeEnum;
import com.ptglue.common.user.model.dto.ActivationCodeDto;
import com.ptglue.common.user.model.dto.UserDto;
import com.ptglue.common.user.service.UserService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.security.SecureRandom;

import static java.util.Objects.isNull;

@Api(tags={"01.User"})
@RestController
@RequestMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Resource(name = "userService")
    UserService userService;

    @Resource(name = "commonService")
    CommonService commonService;

    private static final String APP_NAME= "PTGLUE";

    @ApiOperation(value = "일반로그인", notes = "로그인을 합니다.")
    @PostMapping("/login")
    @ApiResponses({
            @ApiResponse(code=200, message="로그인 성공"),
            @ApiResponse(code=400, message="ID/비밀번호를 확인해주세요.")
    })
    @ResponseStatus(HttpStatus.OK)
    public UserDto.ResponseUserInfoWithAuthDto doSignIn(
            @RequestBody @Valid UserDto.RequestUserSignInDto signInDto) {
        UserDto.UserInfoDto userInfoDto = userService.getUserInfoByUsernameAndPassword(signInDto.getUsername(), signInDto.getPassword());

        return userService.doSignIn(userInfoDto);
    }

    @ApiOperation(value = "로그아웃", notes = "로그아웃을 합니다.")
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseDto<Boolean> doSignOut(
            HttpServletRequest httpServletRequest){

        Long userId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        userService.doSignOut(userId);
        return ApiResponseDto.createException("로그아웃이 완료되었습니다.", true);
    }

    @ApiOperation(value = "아이디 중복 검사")
    @GetMapping("/check/{username}")
    public ApiResponseDto<Boolean> checkId(
            @ApiParam(value = "username", required = true) @PathVariable String username) {

        Long userId = userService.getUserInfoByUsername(username);
        if(userId > 0L) throw new InternalServerErrorException("이미 가입된 ID 입니다.");
        return ApiResponseDto.createException("사용 가능한 ID 입니다.", true);
    }

    @ApiOperation(value = "회원 가입", notes = "회원 가입을 합니다.")
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto.ResponseUserInfoWithAuthDto doSignUp(
            @RequestBody @Valid UserDto.RequestUserSignUpDto signUpDto) {

        //아이디 중복체크
        Long userId = userService.getUserInfoByUsername(signUpDto.getUsername());
        if(userId > 0L) throw new InternalServerErrorException("이미 가입된 ID 입니다.");
        //휴대폰 인증 여부 확인
        userService.checkActivation(signUpDto.getPhone());
        //회원가입
        UserDto.UserInfoDto userInfoDto = userService.doSignUp(signUpDto);
        //로그인
        return userService.doSignIn(userInfoDto);
    }

    @ApiOperation(value = "추천인 아이디 확인", notes = "추천인 아이디를 확인해서 userId를 반환합니다.")
    @GetMapping("/check/recommended/{username}")
    public Long checkRecommendedId(
            @ApiParam(value = "username", required = true) @PathVariable String username) {

        Long recommendedUserId = userService.getUserInfoByUsername(username);
        if (recommendedUserId == 0L) throw new NotFoundException("추천인 아이디가 존재하지 않습니다.");
        return recommendedUserId;
    }

    // 휴면 회원인 경우 로그인 됨
    @ApiOperation(value = "소셜 회원 가입 체크", notes = "소셜 회원 가입 여부 체크한 후, 가입이 되어 있으면 로그인하고, 가입이 되어 있지 않으면 snsId와 token 을 반환합니다..")
    @PostMapping("/signup/social/check")
    public UserDto.ResponseUserInfoWithAuthDto doSignUpSocialCheck(
            @RequestBody @Valid UserDto.RequestUserSignUpSocialCheckDto snsCheckDto) {

        //소셜 가입 여부 확인
        UserDto.UserInfoDto userInfoDto = userService.getUserInfoBySnsInfo(snsCheckDto.getSnsType(), snsCheckDto.getSnsId());
        if(isNull(userInfoDto)) {
            return UserDto.ResponseUserInfoWithAuthDto.builder()
                    .snsId(snsCheckDto.getSnsId())
                    .snsToken(snsCheckDto.getSnsToken())
                    .build();
        }

        return userService.doSignIn(userInfoDto);
    }

    //test 필요
    @ApiOperation(value = "네이버 회원가입 체크", notes = "네이버 회원 가입 여부 체크한 후, 가입이 되어 있으면 로그인하고, 가입이 되어 있지 않으면 snsId와 token 을 반환합니다.")
    @PostMapping("/signup/naver/check")
    public UserDto.ResponseUserInfoWithAuthDto doSignUpNaverCheck(
            @RequestBody @Valid UserDto.RequestNaverDto requestNaverDto) {

        String accessToken = userService.getNaverAccessToken(requestNaverDto);

        UserDto.NaverUserProfileDto naverUserProfileDto = userService.getNaverUserProfileInfo(accessToken);

        //소셜 가입 여부 확인
        UserDto.UserInfoDto userInfoDto = userService.getUserInfoBySnsInfo(SnsTypeEnum.NAVER, naverUserProfileDto.getSnsId());
        if(isNull(userInfoDto)){
            return UserDto.ResponseUserInfoWithAuthDto.builder()
                    .snsId(naverUserProfileDto.getSnsId())
                    .snsToken(accessToken)
                    .build();
        }

        return userService.doSignIn(userInfoDto);
    }

    @ApiOperation(value = "소셜 회원 가입", notes = "소셜 회원 가입을 합니다.")
    @PostMapping("/signup/social")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto.ResponseUserInfoWithAuthDto doSignUpSocial(
            @RequestBody @Valid UserDto.RequestUserSignUpSocialDto signUpDto) {

        //소셜 가입 여부 확인
        UserDto.UserInfoDto userInfoDto = userService.getUserInfoBySnsInfo(signUpDto.getSnsType(), signUpDto.getSnsId());
        if(!isNull(userInfoDto)) {
            return userService.doSignIn(userInfoDto);
        }
        //휴대폰 인증 여부 확인
        userService.checkActivation(signUpDto.getPhone());
        userInfoDto = userService.doSignUpSocial(signUpDto);
        return userService.doSignIn(userInfoDto);
    }

    @ApiOperation(value = "휴대폰 인증 SMS 발송", notes = "인증번호를 해당 번호로 전송합니다.\n")
    @PostMapping("/activation-code/send/phone")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseDto<Boolean> sendActivationCodeToPhone(
            @RequestBody @Valid ActivationCodeDto.RequestActivationCodeDto requestActivationCodeDto) {

//        Random rand = new SecureRandom();
        StringBuilder activationCode = new StringBuilder();

//            for (int i = 0; i < 6; i++) {
//                activationCode.append(rand.nextInt(10));
//            }
        activationCode.append("000000");
        if (commonService.sendSms(requestActivationCodeDto.getPhone(),
                String.format("[" + APP_NAME + "] 인증번호 [%s]를 입력해주세요.", activationCode)) != HttpStatus.ACCEPTED.value()) {
            throw new InternalServerErrorException("문자 발송중 오류가 발생했습니다.");
        }
        userService.createPhoneActivationInfo(ActivationCodeDto.PhoneActivationInfoDto.builder()
                .phone(requestActivationCodeDto.getPhone())
                .activationCode(activationCode.toString())
                .build());
        return ApiResponseDto.createException(requestActivationCodeDto.getPhone() + "으로 인증번호가 전송되었습니다.", true);
    }

    @ApiOperation(value = "회원 가입 시 인증번호 확인", notes = "회원 가입 시, 휴대전화 중복체크와 인증번호를 확인합니다.\n")
    @PostMapping("/signup/activation-code/verify")
    @ResponseStatus(HttpStatus.OK)
    public ActivationCodeDto.PhoneActivationInfoDto signupVerifyActivationCode(
            @RequestBody @Valid ActivationCodeDto.VerifyRequestDto verifyRequest) {

        userService.checkPhone(verifyRequest.getPhone());
        return userService.verifyPhoneActivation(verifyRequest);
    }

    @ApiOperation(value = "인증번호 확인", notes = "인증번호를 확인합니다.\n")
    @PostMapping("/activation-code/verify")
    @ResponseStatus(HttpStatus.OK)
    public ActivationCodeDto.PhoneActivationInfoDto verifyActivationCode(
            @RequestBody @Valid ActivationCodeDto.VerifyRequestDto verifyRequest) {

        return userService.verifyPhoneActivation(verifyRequest);
    }

    @ApiOperation(value = "아이디 찾기")
    @PostMapping("/find/id")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> findId(
            @RequestBody @Valid UserDto.RequestFindIdDto requestFindIdDto) {

        if(!userService.checkActivation(requestFindIdDto.getPhone())) {
            throw new InternalServerErrorException("인증을 다시 진행해주세요.");
        }
        UserDto.UserInfoDto userInfoDto = userService.getUserInfoByPhone(requestFindIdDto.getPhone());
        return ResponseEntity.ok().body((userInfoDto.getUsername().substring(0, userInfoDto.getUsername().length()-2) + "**"));
    }

    @ApiOperation(value = "비밀번호 초기화 인증")
    @PostMapping("/password/verify")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> passwordVerify(
            @RequestBody @Valid UserDto.RequestPasswordVerifyDto requestPasswordVerifyDto) {

        if(!userService.checkActivation(requestPasswordVerifyDto.getPhone())) {
            throw new InternalServerErrorException("인증을 다시 진행해주세요.");
        }

        userService.getUserInfoByUsernameAndPhone(requestPasswordVerifyDto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "비밀번호 재설정")
    @PatchMapping("/reset/password")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> resetPassword(
            @RequestBody @Valid UserDto.RequestResetPassword requestResetPassword) {

        userService.resetPassword(requestResetPassword);
        return ResponseEntity.ok().build();
    }


    @ApiOperation(value = "휴면 해제", notes = "휴면회원을 휴면 해제 합니다.")
    @PostMapping("/activate")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseDto<Boolean> activateUser(
            HttpServletRequest httpServletRequest){

        Long userId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        boolean result = userService.doActivateUser(userId);
        if(result) return ApiResponseDto.createException("휴면 해제가 완료되었습니다.", true);
        else throw new InternalServerErrorException("휴면 해제에 실패했습니다.");
    }

    @ApiOperation(value = "임시 아이디 생성", notes = "임시 아이디 생성합니다.")
    @PostMapping("temporary")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto.UserInfoDto createTemporaryUser(
            @RequestBody @Valid UserDto.RequestTempUserSignUpDto requestTempUserSignUpDto) {

        return userService.createTemporaryUser(requestTempUserSignUpDto);
    }

    @ApiOperation(value = "전체 회원중 인증된 고객 조회", notes = "휴대폰 번호로 인증된 회원 목록을 조회합니다.")
    @GetMapping("search/phone")
    public UserDto.ResponseUser getUser(
            @ApiParam(value = "휴대폰 번호", required = true, defaultValue = "01012345678") @RequestParam String phone){

        return userService.getUser(phone);
    }

    @ApiOperation(value = "사용자가 프로필에서 휴대폰 번호를 수정할때, 인증번호 전송")
    @PostMapping("profile/activation-code/send")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseDto<Boolean> sendProfileActivation(
            @RequestBody @Valid ActivationCodeDto.RequestActivationCodeDto requestActivationCodeDto
    ) {
        StringBuilder activationCode = new StringBuilder();
        SecureRandom rand = new SecureRandom();

        for (int i = 0; i < 6; i++) {
            activationCode.append(rand.nextInt(10));
        }

        String message = String.format("[" + APP_NAME + "] 인증번호 [%s]를 입력해주세요.", activationCode);

        Integer sendSmsStatusCode = commonService.sendSms(requestActivationCodeDto.getPhone(), message);

        if ( sendSmsStatusCode != HttpStatus.ACCEPTED.value()) {
            throw new InternalServerErrorException("문자 발송중 오류가 발생했습니다.");
        }

        userService.createPhoneActivationInfo(ActivationCodeDto.PhoneActivationInfoDto.builder()
                .phone(requestActivationCodeDto.getPhone())
                .activationCode(activationCode.toString())
                .build());

        return ApiResponseDto.createException(requestActivationCodeDto.getPhone() + "으로 인증번호가 전송되었습니다.", true);
    }

    @ApiOperation(value = "사용자가 프로필에서 휴대폰 번호를 수정할때, 휴대폰 번호 인증 확인")
    @PostMapping("profile/activation-code/verify")
    @ResponseStatus(HttpStatus.OK)
    public ActivationCodeDto.PhoneActivationInfoDto verifyProfileActivation(
            @RequestBody @Valid ActivationCodeDto.VerifyRequestDto verifyRequest
    ) {
        return userService.verifyPhoneActivation(verifyRequest);
    }

    @ApiOperation(value = "휴대폰 번호 변경 가능 확인", notes = "변경할 번호가 사용중인지 확인")
    @PostMapping("phone/verify")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponseDto<Boolean> validProfileActivationCode(
            @RequestBody @Valid ActivationCodeDto.RequestActivationCodeDto requestActivationCodeDto
    ) {
        Boolean isPhoneUsed = userService.isPhoneUsed(requestActivationCodeDto.getPhone());

        String message = isPhoneUsed ?
                String.format("%s 번호는 다른 사용자가 등록한 번호입니다. 그래도 변경하시겠습니까?", requestActivationCodeDto.getPhone()) :
                String.format("%s 번호는 변경이 가능한 번호 입니다.", requestActivationCodeDto.getPhone());

        return ApiResponseDto.createException(message, !isPhoneUsed);
    }
}
