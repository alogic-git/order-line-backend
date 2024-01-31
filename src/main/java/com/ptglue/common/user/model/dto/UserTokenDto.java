package com.ptglue.common.user.model.dto;

import com.ptglue.common.user.model.entity.UserToken;
import com.ptglue.common.user.enums.UserRoleEnum;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserTokenDto {

    @Getter
    @Builder
    public static class UserTokenInfoDto {
        @NotEmpty(message = "ID는 필수 입력값 입니다.")
        private UserDto.UserInfoDto user;
        private String refreshToken;

        private UserRoleEnum userRoleEnum;

        public UserToken toEntity() {
            return UserToken.builder()
                    .user(user.toEntity())
                    .refreshToken(refreshToken)
                    .build();
        }

        public static UserTokenInfoDto toDto(UserToken userToken){
            return UserTokenInfoDto.builder()
                    .user(UserDto.UserInfoDto.toDto(userToken.getUser()))
                    .refreshToken(userToken.getRefreshToken())
                    .build();
        }
    }


}
