package com.ptglue.common.user.service;

import com.ptglue.basic.config.Env;
import com.ptglue.basic.config.security.JwtTokenProvider;
import com.ptglue.basic.exception.ExternalServerErrorException;
import com.ptglue.basic.exception.InternalServerErrorException;
import com.ptglue.basic.exception.NotFoundException;
import com.ptglue.basic.exception.UnauthorizedException;
import com.ptglue.basic.service.AwsS3Service;
import com.ptglue.branch.model.entity.BranchUserRole;
import com.ptglue.branch.repository.BranchUserRoleRepository;
import com.ptglue.common.user.enums.SnsTypeEnum;
import com.ptglue.common.user.model.dto.ActivationCodeDto;
import com.ptglue.common.user.model.dto.UserDto;
import com.ptglue.common.user.model.entity.*;
import com.ptglue.common.user.repository.*;
import com.ptglue.common.user.model.entity.UserSns;
import com.ptglue.common.user.model.entity.User;
import com.ptglue.common.user.model.entity.UserToken;
import com.ptglue.common.user.repository.UserSnsRepository;
import com.ptglue.common.user.repository.UserRepository;
import com.ptglue.common.user.repository.UserTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static java.util.Objects.isNull;

@DependsOn("Env")
@Slf4j
@Service
public class UserService {
    @Resource(name = "userRepository")
    UserRepository userRepository;

    @Resource(name = "userTokenRepository")
    UserTokenRepository userTokenRepository;

    @Resource(name = "customUserDetailsService")
    CustomUserDetailsService customUserDetailsService;

    @Resource(name = "userSnsRepository")
    UserSnsRepository userSnsRepository;

    @Resource(name = "branchUserRoleRepository")
    BranchUserRoleRepository branchUserRoleRepository;

    @Resource(name = "awsS3Service")
    AwsS3Service awsS3Service;

    @Resource(name = "jwtTokenProvider")
    JwtTokenProvider jwtTokenProvider;

    @Resource(name = "passwordEncoder")
    PasswordEncoder passwordEncoder;

    @Resource(name = "restTemplate")
    RestTemplate restTemplate;

    @Resource(name = "activationCodeRepository")
    ActivationCodeRepository activationCodeRepository;

    @Resource(name = "userOutRestRepository")
    UserOutRestRepository userOutRestRepository;

    public static final String NAVER_TOKEN_URL = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code";

    public static final String PROFILE_URL = "https://openapi.naver.com/v1/nid/me";

    private static final Long SMS_EXPIRATION_TIME = 60 * 3L;

    //test용으로 1일 설정
    private static final Long ACTIVATION_CODE_EXPIRATION_TIME = 60 * 60 * 24L;

    @Transactional
    public UserDto.ResponseUserInfoWithAuthDto doSignIn(UserDto.UserInfoDto userInfoDto) {

        Optional<User> userOptional = userRepository.findById(userInfoDto.getUserId());
        if(!userOptional.isPresent()) return null;

        UserDto.RoleDto userRoleDto = customUserDetailsService.getUserRoleById(userInfoDto.getUserId());
        String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(userInfoDto.getUserId()), userRoleDto);
        String refreshToken = jwtTokenProvider.createRefreshToken();
        Long expiresIn = jwtTokenProvider.getExpiresIn(accessToken);

        User user = userOptional.get();
        user.updateLastLoginDt();
        userRepository.save(user);

        UserToken userToken = UserToken.builder()
                .user(user)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        userTokenRepository.save(userToken);

        UserDto.ResponseJwtDto jwtDto = UserDto.ResponseJwtDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(expiresIn)
                .build();
        return UserDto.ResponseUserInfoWithAuthDto.toDto(userInfoDto, jwtDto, userRoleDto);
    }

    // hk : user 테이블에 휴면회원 ID도 있으므로 user_out_rest 테이블 조회 안함
    public UserDto.UserInfoDto getUserInfoByUsernameAndPassword(String username, String password) {

        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            if(!passwordEncoder.matches(password, user.getPassword())) {
                throw new InternalServerErrorException("ID/비밀번호를 확인해주세요.");
            }
            return UserDto.UserInfoDto.toDto(user);
        }

        throw new NotFoundException("존재하지 않는 유저입니다.");
    }

    public UserDto.UserInfoDto getUserInfoBySnsInfo(SnsTypeEnum snsType, String snsId) {

        Optional<UserSns> snsInfoOptional = userSnsRepository.findBySnsTypeAndSnsId(snsType, snsId);
        if(snsInfoOptional.isPresent()) {
            UserSns userSns = snsInfoOptional.get();
            //user  휴면 계정일 때 처리하는 법 확인
            return UserDto.UserInfoDto.toDto(userSns.getUser());
        }

        return null;
    }

    @Transactional
    public void doSignOut(Long userId) {
        List<UserToken> userTokens = userTokenRepository.findByUser_Id(userId);
        for(UserToken userToken : userTokens) {
            userToken.deleteToken();
            userTokenRepository.save(userToken);
        }
    }

    //추천인 아이디 검색과 함께 사용하기 위해 다시 userId return으로 변경
    // hk : user 테이블에 휴면회원 ID도 있으므로 user_out_rest 테이블 조회 안함
    public Long getUserInfoByUsername(String username) {

        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()) {
            return userOptional.get().getId();
        }
        return 0L;
    }

    @Transactional
    public UserDto.UserInfoDto doSignUp(UserDto.RequestUserSignUpDto signUpDto){

        signUpDto.updatePhoneActiveYn();
        signUpDto.encodePassword(passwordEncoder.encode(signUpDto.getPassword()));

        User user = userRepository.save(signUpDto.toUserSignUpEntity());
        BranchUserRole userRole = BranchUserRole.builder()
                .user(user)
                .roleType(signUpDto.getLastLoginRoleType())
                .build();
        branchUserRoleRepository.save(userRole);
        return UserDto.UserInfoDto.toDto(user);
    }

    @Transactional
    public UserDto.UserInfoDto doSignUpSocial(UserDto.RequestUserSignUpSocialDto signUpDto){

        signUpDto.updatePhoneActiveYn();
        signUpDto.encodePassword(passwordEncoder.encode(signUpDto.getPassword()));
        User user = userRepository.save(signUpDto.toUserSignUpEntity());
        BranchUserRole userRole = BranchUserRole.builder()
                .user(user)
                .roleType(signUpDto.getLastLoginRoleType())
                .build();
        branchUserRoleRepository.save(userRole);
        userSnsRepository.save(signUpDto.toSnsIfoSignUpEntity(user));
        return UserDto.UserInfoDto.toDto(user);
    }

    public String getNaverAccessToken(UserDto.RequestNaverDto requestNaverDto) {

        String accessToken;

        final String CLIENT_ID = Env.naverClientId();
        final String CLIENT_SECRET = Env.naverClientSecret();
        final String code = requestNaverDto.getCode();
        final String state = requestNaverDto.getState();
        String naverTokenUrl = NAVER_TOKEN_URL;
        naverTokenUrl += ("&client_id=" + CLIENT_ID);
        naverTokenUrl += ("&client_secret=" + CLIENT_SECRET);
        naverTokenUrl += ("&code=" + code);
        naverTokenUrl += ("&state=" + state);

        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        HttpEntity<String> tokenRequestEntity = new HttpEntity<>(tokenHeaders);

        try {
            ResponseEntity<UserDto.ResponseNaverTokenDto> response
                    = restTemplate.exchange(naverTokenUrl, HttpMethod.GET, tokenRequestEntity, UserDto.ResponseNaverTokenDto.class);
            if(response.getStatusCodeValue() != 200) {
                throw new ExternalServerErrorException("네이버 로그인중 서버 오류가 발생했습니다.[0]");
            }
            UserDto.ResponseNaverTokenDto tokenResponse = response.getBody();
            if(tokenResponse == null){
                throw new InternalServerErrorException("네이버 로그인중 오류가 발생했습니다.[3]");
            }
            if(isNull(tokenResponse.getAccessToken())) {
                throw new InternalServerErrorException("네이버 로그인중 오류가 발생했습니다.[4]");
            }
            accessToken = tokenResponse.getAccessToken();

        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            throw new UnauthorizedException();
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new ExternalServerErrorException("네이버 로그인중 서버 오류가 발생했습니다.[1]");
        }

        return accessToken;
    }

    public UserDto.NaverUserProfileDto getNaverUserProfileInfo(String accessToken) {

        UserDto.NaverUserProfileDto naverUserProfileDto;
        HttpHeaders profileHeaders = new HttpHeaders();
        profileHeaders.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> profileRequestEntity = new HttpEntity<>(profileHeaders);
        try {
            ResponseEntity<UserDto.ResponseNaverProfileDto> response = restTemplate.exchange(PROFILE_URL, HttpMethod.GET, profileRequestEntity, UserDto.ResponseNaverProfileDto.class);
            if(response.getStatusCodeValue() != 200) {
                throw new ExternalServerErrorException("네이버 로그인중 서버 오류가 발생했습니다.[2]");
            }
            UserDto.ResponseNaverProfileDto profileResponse = response.getBody();
            if(isNull(profileResponse)){
                throw new InternalServerErrorException("네이버 로그인중 오류가 발생했습니다.[4]");
            }
            if(isNull(profileResponse.getNaverUserProfileDto())) {
                throw new ExternalServerErrorException("네이버 로그인중 서버 오류가 발생했습니다.[3]");
            }
            naverUserProfileDto = profileResponse.getNaverUserProfileDto();
            if(isNull(naverUserProfileDto)){
                throw new InternalServerErrorException("네이버 로그인중 오류가 발생했습니다.[5]");
            }
            if(isNull(naverUserProfileDto.getSnsId()) || isNull(naverUserProfileDto.getName())) {
                throw new InternalServerErrorException("snsId 또는 사용자 이름이 존재하지 않습니다.");
            }
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            throw new UnauthorizedException();
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new ExternalServerErrorException("네이버 로그인중 서버 오류가 발생했습니다.[4]");
        }
        return naverUserProfileDto;
    }

    @Transactional
    public void createPhoneActivationInfo(ActivationCodeDto.PhoneActivationInfoDto phoneActivationInfoDto) {

        ActivationCode phoneActivation = phoneActivationInfoDto.toEntity();
        activationCodeRepository.save(phoneActivation);
    }

    @Transactional
    public ActivationCodeDto.PhoneActivationInfoDto verifyPhoneActivation (ActivationCodeDto.VerifyRequestDto verifyRequest){

        //인증번호 찾기
        ActivationCode phoneActivation = activationCodeRepository.findFirstByPhoneOrderByRegDateTimeDesc(verifyRequest.getPhone());
        if(isNull(phoneActivation))  throw new NotFoundException("인증번호 받기를 클릭해주세요");

        //인증 번호 비교
        if(!verifyRequest.getActivationCode().equals(phoneActivation.getActivationCode())) {
            throw new InternalServerErrorException("인증번호가 다릅니다. 확인 후 다시 입력해주세요.");
        }

        //인증 번호 만료 시간 비교
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        Duration duration = Duration.between(phoneActivation.getRegDateTime(), now);
        if(duration.getSeconds() > SMS_EXPIRATION_TIME) {
            phoneActivation.deleteActivationCode();
            activationCodeRepository.save(phoneActivation);
            throw new InternalServerErrorException("입력 시간이 초과되었습니다. 인증 번호를 다시 받아주세요.");
        }

        //인증 완료
        phoneActivation.updateActivationStatus();
        activationCodeRepository.save(phoneActivation);
        return ActivationCodeDto.PhoneActivationInfoDto.toDto(phoneActivation);
    }

    public void checkPhone (String phone){

        List<User> userList = userRepository.findAllByPhone(phone);
        if(!userList.isEmpty()) throw new InternalServerErrorException("이미 가입된 번호입니다.");

        List<UserOutRest> userOutRestList = userOutRestRepository.findAllByPhone(phone);
        if(!userOutRestList.isEmpty()) throw new InternalServerErrorException("휴면 계정 휴대폰 번호입니다.");
    }

    public boolean checkActivation(String phone) {
        ActivationCode activationCode = activationCodeRepository.findFirstByPhoneOrderByModDateTimeDesc(phone);
        if(isNull(activationCode)) throw new NotFoundException("인증번호를 먼저 요청해주세요.");
        if(!activationCode.getActivationYn()) throw new InternalServerErrorException("인증이 완료되지 않았습니다.");

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        Duration duration = Duration.between(activationCode.getModDateTime(), now);
        if(duration.getSeconds() > ACTIVATION_CODE_EXPIRATION_TIME) throw new InternalServerErrorException("인증이 만료되었습니다.");

        return true;
    }

    public UserDto.UserInfoDto getUserInfoByPhone(String phone) {

        Optional<User> userOptional = Optional.ofNullable(userRepository.findFirstByPhoneOrderByModDateTimeDesc(phone));
        if(userOptional.isPresent()){
            User user = userOptional.get();
            return UserDto.UserInfoDto.toDto(user);
        }

        Optional<UserOutRest> userOutRestOptional = Optional.ofNullable(userOutRestRepository.findFirstByPhoneOrderByModDateTimeDesc(phone));
        if(userOutRestOptional.isPresent()){
            UserOutRest userOutRest = userOutRestOptional.get();
            return UserDto.UserInfoDto.toDto(userOutRest);

        }

        throw new NotFoundException("해당 휴대전화로 가입된 정보가 없습니다.");
    }

    public UserDto.UserInfoDto getUserInfoByUsernameAndPhone(UserDto.RequestPasswordVerifyDto requestPasswordVerifyDto) {

        Optional<User> userOptional = Optional.ofNullable(userRepository.findByUsernameAndPhone(requestPasswordVerifyDto.getUsername(), requestPasswordVerifyDto.getPhone()));
        if(userOptional.isPresent()){
            User user = userOptional.get();
            return UserDto.UserInfoDto.toDto(user);
        }

        Optional<UserOutRest> userOutRestOptional = Optional.ofNullable(userOutRestRepository.findByUsernameAndPhone(requestPasswordVerifyDto.getUsername(), requestPasswordVerifyDto.getPhone()));
        if(userOutRestOptional.isPresent()){
            UserOutRest userOutRest = userOutRestOptional.get();
            return UserDto.UserInfoDto.toDto(userOutRest);
        }

       throw new NotFoundException("해당 휴대전화로 가입된 정보가 없습니다.");
    }

    @Transactional
    public Long resetPassword(UserDto.RequestResetPassword requestResetPassword) {

        Optional<User> userOptional = userRepository.findByUsername(requestResetPassword.getUsername());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.updatePassword(passwordEncoder.encode(requestResetPassword.getPassword()));
            return userRepository.save(user).getId();
        }

        throw new NotFoundException("해당 회원이 존재하지 않습니다.");
    }

    @Transactional
    public boolean doActivateUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<UserOutRest> userOutRestOptional = userOutRestRepository.findByUserId(userId);
        if(userOptional.isPresent() && userOutRestOptional.isPresent()){
            User user = userOptional.get();
            UserOutRest userOutRest = userOutRestOptional.get();
            user.activateUser(UserDto.UserInfoDto.toDto(userOutRest));
            userRepository.save(user);
            userOutRest.delete();
            userOutRestRepository.save(userOutRest);
            return true;
        }
        return false;
    }

    //임시 유저 생성
    @Transactional
    public UserDto.UserInfoDto createTemporaryUser(UserDto.RequestTempUserSignUpDto signUpDto){

        String username = signUpDto.getName() + signUpDto.getPhone().substring(signUpDto.getPhone().length()-4, signUpDto.getPhone().length());
        Optional<User> userOptional = userRepository.findByUsername(username);
        boolean usernameCheck = true;
        if(userOptional.isPresent()){
            usernameCheck = false;
            username = signUpDto.getName();
            Random rand = new SecureRandom();

            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 3; j++) {
                    username = username + rand.nextInt(10);
                }
                userOptional = userRepository.findByUsername(username);
                if(!userOptional.isPresent()){
                    usernameCheck = true;
                    break;
                }
            }
        }
        //10번 시도 했는데도 중복되는 이름이 있으면 에러
        if(!usernameCheck) throw new InternalServerErrorException("임시 아이디 발급에 실패했습니다. 다시 시도해주세요.");

        signUpDto.makeUsername(username);
        signUpDto.encodePassword(passwordEncoder.encode("0000"));

        User user = signUpDto.toUserSignUpEntity();
        userRepository.save(user);
        return UserDto.UserInfoDto.toDto(user);
    }

    public UserDto.ResponseUser getUser(String phone) {

        Optional<User> userOptional = userRepository.findByPhoneAndPhoneActiveYn(phone, true);
        if(!userOptional.isPresent()){
            throw new NotFoundException("회원을 찾을 수 없습니다.");
        }
        return UserDto.ResponseUser.toDto(userOptional.get());
    }

    public Boolean isPhoneUsed(String phone){
        Optional<ActivationCode> activationCodeOptional = activationCodeRepository.findByPhoneAndActivationYn(phone, true);
        return activationCodeOptional.isPresent();
    }
}
