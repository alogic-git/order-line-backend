package com.orderline.user.model.dto;

import com.orderline.user.model.entity.UserToken;
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

        public static UserTokenInfoDto toDto(UserToken userToken){
            return UserTokenInfoDto.builder()
                    .user(UserDto.UserInfoDto.toDto(userToken.getUser()))
                    .refreshToken(userToken.getRefreshToken())
                    .build();
        }
    }


}