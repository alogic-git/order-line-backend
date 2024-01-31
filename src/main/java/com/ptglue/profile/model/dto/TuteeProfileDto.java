package com.ptglue.profile.model.dto;

import com.ptglue.common.user.model.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TuteeProfileDto {
    @Getter
    @Builder
    public static class RequestTuteeProfileDto {
        // 이름, 생년월일, 휴대폰 번호, 이메일 주소
        @ApiModelProperty(value = "이름")
        private String name;

        @ApiModelProperty(value = "생년월일")
        private LocalDate birthDate;

        @ApiModelProperty(value = "휴대폰 번호")
        private String phone;

        @ApiModelProperty(value = "휴대폰 번호 인증 유무")
        private Boolean phoneActiveYn;

        public User toEntity(){
            return User.builder()
                    .name(name)
                    .birthDate(birthDate)
                    .phone(phone)
                    .phoneActiveYn(phoneActiveYn)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ResponseTuteeProfileDto{

        @ApiModelProperty(value = "유저 id", example = "1")
        private Long userId;

        @ApiModelProperty(value = "유저 아이디", example = "user1")
        private String username;

        @ApiModelProperty(value = "유저 이름", example = "고배배")
        private String name;

        @ApiModelProperty(value = "유저 폰번호", example = "01012345678")
        private String phone;

        @ApiModelProperty(value = "유저 생년월일")
        private LocalDate birthDate;

        public static TuteeProfileDto.ResponseTuteeProfileDto toDto(final User user){
            return TuteeProfileDto.ResponseTuteeProfileDto.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .name(user.getName())
                    .phone(user.getPhone())
                    .birthDate(user.getBirthDate())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ResponseUserProfileStatisticsDto{
        @ApiModelProperty(value = "나의 수강권")
        private Integer ticketCount;

        @ApiModelProperty(value = "연결된 지점")
        private Integer branchCount;

        public static ResponseUserProfileStatisticsDto toDto(Integer ticketCount, Integer branchCount){
            return ResponseUserProfileStatisticsDto.builder()
                    .ticketCount(ticketCount)
                    .branchCount(branchCount)
                    .build();
        }
    }
}
