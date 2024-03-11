package com.orderline.user.service;

import com.orderline.basic.exception.NotFoundException;
import com.orderline.user.enums.UserRoleEnum;
import com.orderline.user.model.dto.UserDto;
import com.orderline.user.model.entity.User;
import com.orderline.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@RequiredArgsConstructor
@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Resource(name = "userRepository")
    UserRepository userRepository;

    public UserDetails loadUserByUsername(String userId) {

        UserDto.RoleDto roleDto = getUserRoleById(Long.valueOf(userId));
        return new CustomUserDetails(roleDto);
    }

    public UserDto.RoleDto getUserRoleById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("사용자 정보가 없습니다"));

        UserRoleEnum userRoleEnum = null;
        if (user.getAdminYn() == Boolean.TRUE) {
            userRoleEnum = UserRoleEnum.ADMIN;
        } else {
            userRoleEnum = UserRoleEnum.USER;
        }

        return UserDto.RoleDto.builder()
                .id(userId)
                .password(user.getPassword())
                .siteId(user.getSiteId() == null ? null : user.getSiteId())
                .roleType(userRoleEnum)
                .build();
    }
}