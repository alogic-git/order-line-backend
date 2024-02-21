package com.orderline.basic.controller;


import com.orderline.basic.config.security.JwtTokenProvider;
import com.orderline.basic.exception.InternalServerErrorException;
import com.orderline.basic.exception.NotFoundException;
import com.orderline.basic.service.AuthService;
import com.orderline.user.model.dto.UserDto;
import com.orderline.user.model.dto.UserTokenDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static java.util.Objects.isNull;

@Api(tags={"00.Auth"})
@RestController
@RequestMapping(path = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    @Resource(name = "authService")
    AuthService authService;

    @Resource(name = "jwtTokenProvider")
    JwtTokenProvider jwtTokenProvider;

    @ApiOperation(value = "토큰 재발행", notes = "토큰을 재발행합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "토큰 발행에 성공했습니다."),
            @ApiResponse(code = 400, message = "입력하신 refresh token 이 존재하지 않습니다. 로그인을 다시 진행해주세요."),
    })
    @PostMapping("/token/reissue")
    @ResponseStatus(HttpStatus.OK)
    public UserDto.ResponseUserInfoWithAuthDto reissueJwt(
            @RequestBody @Valid UserDto.RequestRefreshJwtDto refreshToken) {

        UserTokenDto.UserTokenInfoDto userToken = authService.getUserToken(refreshToken.getRefreshToken());
        if(isNull(userToken)) {
            throw new NotFoundException("입력하신 refresh token 이 존재하지 않습니다. 로그인을 다시 진행해주세요.");
        }
        UserDto.UserInfoDto user = userToken.getUser();
        if(isNull(user)){
            throw new InternalServerErrorException("존재하지 않는 유저입니다.");
        }

        if(!jwtTokenProvider.validateToken(userToken.getRefreshToken())) {
            throw new InternalServerErrorException("입력하신 refresh token 이 유효하지 않습니다. 로그인을 다시 진행해주세요.");
        }
        Long result = authService.deleteUserToken(userToken);
        if(result <= 0){
            throw new InternalServerErrorException("refresh token 갱신중 오류가 발생했습니다.");
        }

        return authService.reissueJwt(user, userToken);
    }

}
