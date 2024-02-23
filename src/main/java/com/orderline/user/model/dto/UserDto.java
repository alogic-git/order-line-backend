package com.orderline.user.model.dto;

import com.orderline.user.model.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import javax.validation.constraints.*;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDto {


    private static final String USERNAME_PATTERN = "[a-z0-9]{4,12}";
    private static final String PASSWORD_PATTERN = "(?=.*\\d)(?=.*[a-z])(?=.*\\W)(?=\\S+$).{8,20}";

    @Getter
    @Builder
    public static class RequestUserSignInDto {
        @ApiModelProperty(value = "ID", example = "gobaebae")
        @NotBlank(message = "Id를 입력해주세요.")
        private String username;

        @ApiModelProperty(value = "비밀번호", example = "asdf1234!")
        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;

    }

    @Getter
    @Builder
    public static class UserInfoDto {

        @ApiModelProperty(value = "DB user.id")
        private Long userId;

        @ApiModelProperty(value = "ID", example = "gobaebae")
        @NotBlank(message = "Id를 입력해주세요.")
        private String username;

        private String password;

        @ApiModelProperty(value = "Name")
        private String name;

        @ApiModelProperty(value = "Phone")
        @NotBlank(message = "Phone을 입력해주세요.")
        private String phone;

        @ApiModelProperty(value = "Join Date")
        private ZonedDateTime joinDt;

        @ApiModelProperty(value = "Last Login Date")
        private ZonedDateTime lastLoginDt;

        @ApiModelProperty(value = "운영자 여부")
        private Boolean adminYn;

        public User toEntity(){
            return User.builder()
                    .id(userId)
                    .username(username)
                    .name(name)
                    .phone(phone)
                    .joinDt(joinDt)
                    .lastLoginDt(lastLoginDt)
                    .build();
        }

        public static UserInfoDto toDto(User user) {
            return UserInfoDto.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .name(user.getName())
                    .phone(user.getPhone())
                    .joinDt(user.getJoinDt())
                    .lastLoginDt(user.getLastLoginDt())
                    .adminYn(user.getAdminYn())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ResponseJwtDto{
        @NotEmpty
        private String accessToken;
        @NotEmpty
        private String refreshToken;
        @NotEmpty
        private Long expiresIn;
    }

    @Getter
    @Builder
    public static class ResponseUserInfoWithAuthDto{
        @ApiModelProperty(value = "DB user.id", example = "9")
        private Long userId;

        @ApiModelProperty(value = "유저 id", example = "gobaebae")
        private String username;

        @ApiModelProperty(value = "유저명", example = "고배배")
        private String name;

        @ApiModelProperty(value = "Phone", example = "01012345678")
        private String phone;

        @ApiModelProperty(value = "가입 일시")
        private Long joinDt;

        @ApiModelProperty(value = "최종 로그인 일시")
        private Long lastLoginDt;

        private ResponseJwtDto responseJwtDto;

        @ApiModelProperty(value = "운영자 여부")
        private Boolean adminYn;

        public static ResponseUserInfoWithAuthDto toDto(final UserInfoDto userInfoDto, final ResponseJwtDto responseJwtDto) {
            return ResponseUserInfoWithAuthDto.builder()
                    .userId(userInfoDto.getUserId())
                    .username(userInfoDto.getUsername())
                    .name(userInfoDto.getName())
                    .phone(userInfoDto.getPhone())
                    .joinDt(userInfoDto.getJoinDt().toEpochSecond())
                    .lastLoginDt(userInfoDto.getLastLoginDt().toEpochSecond())
                    .responseJwtDto(responseJwtDto)
                    .build();
        }
    }


    @Getter
    @Builder
    public static class RequestUserSignUpDto {

        @ApiModelProperty(value = "유저 id(필수)", example = "gobaebae")
        @Pattern(regexp = USERNAME_PATTERN, message = "아이디는 4~12자 영문이나 숫자를 사용하세요.")
        @NotBlank(message = "유저 id은 필수 입력사항입니다.")
        private String username;

        @ApiModelProperty(value = "비밀번호(필수)", example = "asdf1234!")
        @Pattern(regexp = PASSWORD_PATTERN, message = "비밀번호는 8~20자 영문, 숫자, 특수문자를 포함해주세요.")
        @NotBlank(message = "비밀번호는 필수입니다.")
        private String password;

        @ApiModelProperty(value = "이름", example = "고배배")
        private String name;

        @ApiModelProperty(value = "전화번호", example = "01030902825")
        private String phone;

        @ApiModelProperty(value = "가입 일시")
        private ZonedDateTime joinDt;

        @ApiModelProperty(value = "최종 로그인 일시")
        private ZonedDateTime lastLoginDt;

        public void encodePassword(String password){
            this.password = password;
        }
        public User toUserSignUpEntity() {
            return User.builder()
                    .username(username)
                    .password(password)
                    .name(name)
                    .phone(phone)
                    .joinDt(ZonedDateTime.now(ZoneId.of("UTC")))
                    .lastLoginDt(ZonedDateTime.now(ZoneId.of("UTC")))
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestFindIdDto {
        @NotBlank(message = "휴대폰 번호는 필수 입력값입니다.")
        @ApiModelProperty(value = "휴대폰 번호(필수)", example = "01030902825")
        private String phone;

    }

    @Getter
    @Builder
    public static class RequestPasswordVerifyDto{

        @ApiModelProperty(value = "유저 id(필수)", example = "gobaebae")
        @NotBlank(message = "유저 id는 필수 입력사항입니다.")
        private String username;

        @NotBlank(message = "휴대폰 번호는 필수 입력값입니다.")
        @ApiModelProperty(value = "전화번호(필수)", example = "01030902825")
        private String phone;
    }

    @Getter
    @Builder
    public static class RequestResetPassword{

        @ApiModelProperty(value = "유저 id(필수)", example = "gobaebae")
        @NotBlank(message = "유저 id는 필수 입력사항입니다.")
        private String username;

        @ApiModelProperty(value = "비밀번호(필수)", example = "asdf1234!")
        @Pattern(regexp = PASSWORD_PATTERN, message = "비밀번호는 8~20자 영문, 숫자, 특수문자를 포함해주세요.")
        @NotBlank(message = "비밀번호는 필수입니다.")
        private String password;
    }

    @Getter
    @Builder
    public static class RequestProfileDto {
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
                    .phone(phone)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class RequestMemberOutDto {
        private String outType;
        private String outReason;
    }

    @Getter
    @Builder
    public static class RequestResetPasswordDto {
        private String oldPassword;
        @Pattern(regexp = PASSWORD_PATTERN, message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        @NotBlank(message = "비밀번호는 필수입니다.")
        private String newPassword1;

        @Pattern(regexp = PASSWORD_PATTERN, message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        @NotBlank(message = "비밀번호는 필수입니다.")
        private String newPassword2;
    }

    @Getter
    public static class RequestRefreshJwtDto{
        @NotBlank(message = "refresh token 값이 필요합니다.")
        private String refreshToken;
    }

    @Getter
    @Builder
    public static class ResponseMaskedUserDto {

        @ApiModelProperty(value = "DB user.id", example = "1")
        private Long userId;

        @ApiModelProperty(value = "유저 아이디", example = "user1")
        private String username;

        @ApiModelProperty(value = "유저 이름", example = "고배배")
        private String name;

        @ApiModelProperty(value = "휴대폰 번호", example = "01012345678")
        private String phone;

        public static ResponseMaskedUserDto toDto(final User user){
            return ResponseMaskedUserDto.builder()
                    .userId(user.getId())
                    .username(user.getUsername().substring(0, 2) + "****")
                    .name(user.getName().length() < 3
                            ? user.getName().substring(0, 1) + "*"
                            : user.getName().substring(0, 1)
                            + "*"
                            + user.getName().substring(user.getName().length() - 1)
                    )
                    .phone(user.getPhone())
                    .build();
        }
    }

    @Builder
    @Getter
    public static class ResponseMaskedUserListDto {

        @ApiModelProperty(value = "유저 리스트")
        private List<ResponseMaskedUserDto> results;
        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static ResponseMaskedUserListDto build(Page<ResponseMaskedUserDto> responseDtoPage, Integer currentPage, Integer maxResults){
            return ResponseMaskedUserListDto.builder()
                    .results(responseDtoPage.getContent())
                    .maxResults(maxResults)
                    .currentPage(currentPage)
                    .totalPages(responseDtoPage.getTotalPages())
                    .totalElements(responseDtoPage.getTotalElements())
                    .build();
        }
    }
}