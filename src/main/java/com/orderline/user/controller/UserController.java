package com.orderline.user.controller;
import com.orderline.basic.exception.InternalServerErrorException;

import com.orderline.basic.model.dto.ApiResponseDto;
import com.orderline.common.user.model.dto.UserDto;
import com.orderline.common.user.service.UserService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Api(tags={"01.User"})
@RestController
@RequestMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Resource(name = "userService")
    UserService userService;

    private static final String APP_NAME= "ORDERLINE";

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
}
