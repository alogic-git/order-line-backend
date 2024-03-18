package com.orderline.basic.service;

import com.orderline.basic.config.security.JwtTokenProvider;
import com.orderline.basic.exception.InternalServerErrorException;
import com.orderline.basic.exception.NotFoundException;
import com.orderline.user.model.dto.UserDto;
import com.orderline.user.model.dto.UserTokenDto;
import com.orderline.user.model.entity.UserToken;
import com.orderline.user.repository.UserTokenRepository;
import com.orderline.user.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import static java.util.Objects.isNull;


@Service
@RequiredArgsConstructor
public class AuthService {

    @Resource(name = "userTokenRepository")
    UserTokenRepository userTokenRepository;

    @Resource(name = "jwtTokenProvider")
    JwtTokenProvider jwtTokenProvider;

    @Resource(name = "customUserDetailsService")
    CustomUserDetailsService customUserDetailsService;

    public UserTokenDto.UserTokenInfoDto getValidUserToken(String refreshToken) {
        UserToken userToken = userTokenRepository.findFirstByRefreshTokenOrderByIdDesc(refreshToken).orElseThrow(() -> new NotFoundException("refresh token이 존재하지 않습니다. 로그인을 다시 진행해주세요."));

        if(!jwtTokenProvider.validateToken(userToken.getRefreshToken())) {
            throw new InternalServerErrorException("입력하신 refresh token 이 유효하지 않습니다. 로그인을 다시 진행해주세요.");
        }

        return UserTokenDto.UserTokenInfoDto.toDto(userToken);
    }

    @Transactional
    public void deleteUserToken(String refreshToken) {
        UserToken userToken = userTokenRepository.findFirstByRefreshTokenOrderByIdDesc(refreshToken)
                .orElseThrow(() -> new NotFoundException("refresh token이 존재하지 않습니다. 로그인을 다시 진행해주세요."));
        userToken.deleteToken();
    }

    @Transactional
    public UserDto.ResponseUserInfoWithAuthDto reissueJwt(UserTokenDto.UserTokenInfoDto userTokenDto) {

        //기존 토큰 삭제
        deleteUserToken(userTokenDto.getRefreshToken());

        UserDto.UserInfoDto userInfoDto = userTokenDto.getUser();
        if(isNull(userInfoDto)){
            throw new InternalServerErrorException("존재하지 않는 유저입니다.");
        }
        return createToken(userInfoDto);
    }

    public UserDto.ResponseUserInfoWithAuthDto createToken(final UserDto.UserInfoDto userInfoDto) {
        Long userId = userInfoDto.getUserId();
        Long siteId = null;
        if(userInfoDto.getSiteId() != null){
            siteId = userInfoDto.getSiteId();
        }
        UserDto.RoleDto roleDto = customUserDetailsService.getUserRoleById(userId);
        String newAccessToken = jwtTokenProvider.createAccessToken(String.valueOf(userId), roleDto);
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
