package com.orderline.user.service;

import com.orderline.basic.config.security.JwtTokenProvider;
import com.orderline.basic.exception.InternalServerErrorException;
import com.orderline.basic.exception.NotFoundException;
import com.orderline.basic.service.AwsS3Service;
import com.orderline.user.model.entity.User;
import com.orderline.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

@DependsOn("Env")
@Slf4j
@Service
public class UserService {
    @Resource(name = "userRepository")
    UserRepository userRepository;

    @Resource(name = "awsS3Service")
    AwsS3Service awsS3Service;

    @Resource(name = "jwtTokenProvider")
    JwtTokenProvider jwtTokenProvider;

    @Resource(name = "passwordEncoder")
    PasswordEncoder passwordEncoder;

    @Resource(name = "restTemplate")
    RestTemplate restTemplate;
    
    
//    @Transactional
//    public UserDto.ResponseUserInfoWithAuthDto doSignIn(UserDto.UserInfoDto userInfoDto) {
//
//        Optional<User> userOptional = userRepository.findById(userInfoDto.getUserId());
//        if(!userOptional.isPresent()) return null;
//
//        UserDto.RoleDto userRoleDto = customUserDetailsService.getUserRoleById(userInfoDto.getUserId());
//        String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(userInfoDto.getUserId()), userRoleDto);
//        String refreshToken = jwtTokenProvider.createRefreshToken();
//        Long expiresIn = jwtTokenProvider.getExpiresIn(accessToken);
//
//        User user = userOptional.get();
//        user.updateLastLoginDt();
//        userRepository.save(user);
//
//        UserToken userToken = UserToken.builder()
//                .user(user)
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .build();
//        userTokenRepository.save(userToken);
//
//        UserDto.ResponseJwtDto jwtDto = UserDto.ResponseJwtDto.builder()
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .expiresIn(expiresIn)
//                .build();
//        return UserDto.ResponseUserInfoWithAuthDto.toDto(userInfoDto, jwtDto, userRoleDto);
//    }
//
//    // hk : user 테이블에 휴면회원 ID도 있으므로 user_out_rest 테이블 조회 안함
//    public UserDto.UserInfoDto getUserInfoByUsernameAndPassword(String username, String password) {
//
//        Optional<User> userOptional = userRepository.findByUsername(username);
//        if(userOptional.isPresent()) {
//            User user = userOptional.get();
//            if(!passwordEncoder.matches(password, user.getPassword())) {
//                throw new InternalServerErrorException("ID/비밀번호를 확인해주세요.");
//            }
//            return UserDto.UserInfoDto.toDto(user);
//        }
//
//        throw new NotFoundException("존재하지 않는 유저입니다.");
//    }
//
//    @Transactional
//    public void doSignOut(Long userId) {
//        List<UserToken> userTokens = userTokenRepository.findByUser_Id(userId);
//        for(UserToken userToken : userTokens) {
//            userToken.deleteToken();
//            userTokenRepository.save(userToken);
//        }
//    }
//
//    //추천인 아이디 검색과 함께 사용하기 위해 다시 userId return으로 변경
//    // hk : user 테이블에 휴면회원 ID도 있으므로 user_out_rest 테이블 조회 안함
//    public Long getUserInfoByUsername(String username) {
//
//        Optional<User> userOptional = userRepository.findByUsername(username);
//        if(userOptional.isPresent()) {
//            return userOptional.get().getId();
//        }
//        return 0L;
//    }

}
