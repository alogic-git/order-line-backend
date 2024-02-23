package com.orderline.user.service;

import com.orderline.basic.exception.NotFoundException;
import com.orderline.user.model.dto.UserDto;
import com.orderline.user.model.entity.User;
import com.orderline.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Resource(name = "userRepository")
    UserRepository userRepository;

    public UserDetails loadUserByUsername(String userId) {

        UserDto.UserInfoDto userInfo = getUsernameById(Long.valueOf(userId));
        return new CustomUserDetails(userInfo);
    }

    public UserDto.UserInfoDto getUsernameById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("사용자 정보가 없습니다"));

        return UserDto.UserInfoDto.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .adminYn(user.getAdminYn())
                .build();
    }
}