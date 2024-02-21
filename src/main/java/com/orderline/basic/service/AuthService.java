package com.orderline.basic.service;

import com.orderline.basic.config.security.JwtTokenProvider;
import com.orderline.user.model.dto.UserDto;
import com.orderline.user.model.dto.UserTokenDto;
import com.orderline.user.model.entity.UserToken;
import com.orderline.user.repository.UserTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;


@Service
@RequiredArgsConstructor
public class AuthService {

    @Resource(name = "userTokenRepository")
    UserTokenRepository userTokenRepository;

    @Resource(name = "jwtTokenProvider")
    JwtTokenProvider jwtTokenProvider;

    public UserTokenDto.UserTokenInfoDto getUserToken(String refreshToken) {
        UserToken userToken = userTokenRepository.findFirstByRefreshTokenOrderByIdDesc(refreshToken).orElse(null);
        if (userToken == null) {
            return null;
        }
        return UserTokenDto.UserTokenInfoDto.toDto(userToken);
    }

    @Transactional
    public Long deleteUserToken(UserTokenDto.UserTokenInfoDto userTokenDto) {
        UserToken userToken = userTokenRepository.findFirstByRefreshTokenOrderByIdDesc(userTokenDto.getRefreshToken()).orElse(null);
        if (userToken == null) {
            return null;
        }
        userToken.deleteToken();
        return userTokenRepository.save(userToken).getId();
    }

    @Transactional
    public UserDto.ResponseUserInfoWithAuthDto reissueJwt(UserDto.UserInfoDto userInfoDto, UserTokenDto.UserTokenInfoDto userTokenDto) {

        String newAccessToken = jwtTokenProvider.createAccessToken(String.valueOf(userTokenDto.getUser().getUserId()));
        String newRefreshToken = jwtTokenProvider.createRefreshToken();
        Long expiresIn = jwtTokenProvider.getExpiresIn(newAccessToken);

        UserToken userToken = UserToken.builder()
                .user(userInfoDto.toEntity())
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
        userTokenRepository.save(userToken);

        UserDto.ResponseJwtDto jwtDto = UserDto.ResponseJwtDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .expiresIn(expiresIn)
                .build();
        return UserDto.ResponseUserInfoWithAuthDto.toDto(userInfoDto, jwtDto);
    }

}
