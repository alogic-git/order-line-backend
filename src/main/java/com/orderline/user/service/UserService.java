package com.orderline.user.service;

import com.orderline.basic.exception.InternalServerErrorException;
import com.orderline.basic.exception.NotFoundException;
import com.orderline.basic.service.AuthService;
import com.orderline.user.model.dto.UserDto;
import com.orderline.user.model.entity.User;
import com.orderline.user.model.entity.UserToken;
import com.orderline.user.repository.UserRepository;
import com.orderline.user.repository.UserTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

@DependsOn("Env")
@Slf4j
@Service
public class UserService {
    @Resource(name = "userRepository")
    UserRepository userRepository;

    @Resource(name = "userTokenRepository")
    UserTokenRepository userTokenRepository;

    @Resource(name = "passwordEncoder")
    PasswordEncoder passwordEncoder;

    @Resource(name = "authService")
    AuthService authService;

    @Transactional
    public UserDto.ResponseUserInfoWithAuthDto doSignIn(UserDto.UserInfoDto userInfoDto) {

        Long userId =  userInfoDto.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 유저입니다."));

        user.updateLastLoginDt();
        User savedUser = userRepository.save(user);

        UserDto.UserInfoDto savedUserInfoDto = UserDto.UserInfoDto.toDto(savedUser);

        return authService.createToken(savedUserInfoDto);
    }

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

    @Transactional
    public void doSignOut(Long userId) {
        List<UserToken> userTokens = userTokenRepository.findByUserId(userId);
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

        signUpDto.encodePassword(passwordEncoder.encode(signUpDto.getPassword()));
        User user = userRepository.save(signUpDto.toUserSignUpEntity());

        return UserDto.UserInfoDto.toDto(user);
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
}
