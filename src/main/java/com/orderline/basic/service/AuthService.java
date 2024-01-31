package com.orderline.basic.service;

import com.orderline.basic.config.security.JwtTokenProvider;
import com.orderline.common.user.model.dto.UserDto;
import com.orderline.common.user.model.dto.UserTokenDto;
import com.orderline.common.user.model.entity.UserToken;
import com.orderline.common.user.repository.UserTokenRepository;
import com.orderline.common.user.service.CustomUserDetailsService;
import com.orderline.branch.service.BranchService;
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

    @Resource(name = "customUserDetailsService")
    CustomUserDetailsService customUserDetailsService;

    @Resource(name = "branchService")
    BranchService branchService;

    public UserTokenDto.UserTokenInfoDto getUserToken(String refreshToken) {
        UserToken userToken = userTokenRepository.findFirstByRefreshTokenOrderByIdDesc(refreshToken);
        if (userToken == null) return null;
        return UserTokenDto.UserTokenInfoDto.toDto(userToken);
    }

    @Transactional
    public Long deleteUserToken(UserTokenDto.UserTokenInfoDto userTokenDto) {
        UserToken userToken = userTokenRepository.findFirstByRefreshTokenOrderByIdDesc(userTokenDto.getRefreshToken());
        userToken.deleteToken();
        return userTokenRepository.save(userToken).getId();
    }

    @Transactional
    public UserDto.ResponseUserInfoWithAuthDto reissueJwt(UserDto.UserInfoDto userInfoDto, UserTokenDto.UserTokenInfoDto userTokenDto) {

        UserDto.RoleDto userRoleDto = customUserDetailsService.getUserRoleById(userTokenDto.getUser().getUserId());
        String newAccessToken = jwtTokenProvider.createAccessToken(String.valueOf(userTokenDto.getUser().getUserId()), userRoleDto);
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
        return UserDto.ResponseUserInfoWithAuthDto.toDto(userInfoDto, jwtDto, userRoleDto);
    }

}
