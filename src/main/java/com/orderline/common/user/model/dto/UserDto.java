package com.orderline.common.user.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.orderline.common.user.model.entity.User;
import com.orderline.common.user.model.entity.UserOutRest;
import com.orderline.common.user.model.entity.UserSns;
import io.swagger.annotations.ApiModelProperty;
import com.orderline.branch.enums.ReservationTypeEnum;
import com.orderline.common.user.enums.LoginTypeEnum;
import com.orderline.common.user.enums.SnsTypeEnum;
import com.orderline.common.user.enums.UserOutTypeEnum;
import com.orderline.common.user.enums.UserRoleEnum;
import lombok.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDto {


    private static final String USERNAME_PATTERN = "[a-z0-9]{4,12}";
    private static final String PASSWORD_PATTERN = "(?=.*\\d)(?=.*[a-z])(?=.*\\W)(?=\\S+$).{8,20}";

    @Getter
    @Builder
    public static class RoleDto {
        private Long id;

        private String password;

        @ApiModelProperty(value = "마지막 로그인시 선택한 역할")
        private UserRoleEnum roleType;

        @ApiModelProperty(value = "마지막 로그인시 선택한 지점")
        private Long branchId;

        @ApiModelProperty(value = "내가 마지막 로그인시 선택한 역할로 속한 지점 수")
        private Long branchCount;

        @ApiModelProperty(value = "지점의 예약 타입")
        private ReservationTypeEnum reservationType;
    }

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
    public static class RequestUserSignInSocialDto {
        @ApiModelProperty(value = "소셜로그인 타입")
        private SnsTypeEnum snsType;

        @NotNull(message = "sns id를 입력해주세요.")
        @ApiModelProperty(value = "sns id")
        private String snsId;
    }


    @Getter
    @Builder
    public static class UserInfoDto {

        @ApiModelProperty(value = "DB user ID")
        private Long userId;

        @ApiModelProperty(value = "ID", example = "gobaebae")
        @NotBlank(message = "Id를 입력해주세요.")
        private String username;

        @ApiModelProperty(value = "Name")
        private String name;

        @ApiModelProperty(value = "Phone")
        @NotBlank(message = "Phone을 입력해주세요.")
        private String phone;

        @ApiModelProperty(value = "Phone Active")
        private Boolean phoneActiveYn;

        @ApiModelProperty(value = "Birth Date")
        private LocalDate birthDate;

        @ApiModelProperty(value = "마지막 로그인 시 선택한 역할")
        private UserRoleEnum lastLoginRoleType;

        @ApiModelProperty(value = "Login Type Code")
        private LoginTypeEnum loginType;

        @ApiModelProperty(value = "SNS ID")
        private SnsTypeEnum snsType;

        @ApiModelProperty(value = "SNS ID")
        private String snsId;

        @ApiModelProperty(value = "SNS Token")
        private String snsToken;

        @ApiModelProperty(value = "Profile URI")
        private String profileUri;

        @ApiModelProperty(value = "Recommend User")
        private Long recommendUserId;

        @ApiModelProperty(value = "Advertise")
        private Boolean advertiseYn;

        @ApiModelProperty(value = "Join Date")
        private ZonedDateTime joinDt;

        @ApiModelProperty(value = "Last Login Date")
        private ZonedDateTime lastLoginDt;

        @ApiModelProperty(value = "회원 out 상태")
        private UserOutTypeEnum outType;

        @ApiModelProperty(value = "다크 모드 여부")
        private Boolean darkModeYn;

        @ApiModelProperty(value = "운영자 여부")
        private Boolean adminYn;

        public User toEntity(){
            return User.builder()
                    .id(userId)
                    .username(username)
                    .name(name)
                    .phone(phone)
                    .phoneActiveYn(phoneActiveYn)
                    .birthDate(birthDate)
                    .profileUri(profileUri)
                    .recommendedUserId(recommendUserId)
                    .advertiseYn(advertiseYn)
                    .joinDt(joinDt)
                    .lastLoginDt(lastLoginDt)
                    .lastLoginRoleType(lastLoginRoleType)
                    .build();
        }

        public static UserInfoDto toDto(User user) {
            return UserInfoDto.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .name(user.getName())
                    .phone(user.getPhone())
                    .phoneActiveYn(user.getPhoneActiveYn())
                    .birthDate(user.getBirthDate())
                    .profileUri(user.getProfileUri())
                    .recommendUserId(user.getRecommendedUserId())
                    .advertiseYn(user.getAdvertiseYn())
                    .joinDt(user.getJoinDt())
                    .lastLoginDt(user.getLastLoginDt())
                    .outType(user.getOutType())
                    .lastLoginRoleType(user.getLastLoginRoleType())
                    .darkModeYn(user.getDarkModeYn())
                    .adminYn(user.getAdminYn())
                    .build();
        }

        public static UserInfoDto toDto(UserOutRest userOutRest) {
            return UserInfoDto.builder()
                    .userId(userOutRest.getUserId())
                    .username(userOutRest.getUsername())
                    .name(userOutRest.getName())
                    .phone(userOutRest.getPhone())
                    .phoneActiveYn(userOutRest.getPhoneActiveYn())
                    .birthDate(userOutRest.getBirthDate())
                    .profileUri(userOutRest.getProfileUri())
                    .recommendUserId(userOutRest.getRecommendedUserId())
                    .advertiseYn(userOutRest.getAdvertiseYn())
                    .joinDt(userOutRest.getJoinDt())
                    .lastLoginDt(userOutRest.getLastLoginDt())
                    .outType(userOutRest.getOutType())
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
        @ApiModelProperty(value = "ID", example = "9")
        private Long userId;

        @ApiModelProperty(value = "유저 id", example = "gobaebae")
        private String username;

        @ApiModelProperty(value = "유저명", example = "고배배")
        private String name;

        @ApiModelProperty(value = "Phone", example = "01012345678")
        private String phone;

        @ApiModelProperty(value = "Phone Active", example = "true")
        private Boolean phoneActiveYn;

        @ApiModelProperty(value = "Birth Date", example = "2011-05-28")
        private LocalDate birthDate;

        @ApiModelProperty(value = "마지막 로그인 시 선택한 역할")
        private UserRoleEnum lastLoginRoleType;

        @ApiModelProperty(value = "마지막 로그인 시 선택한 지점 id")
        private Long branchId;

        @ApiModelProperty(value = "내가 마지막 로그인시 선택한 역할로 속한 지점 수")
        private Long branchCount;

        @ApiModelProperty(value = "지점의 예약 타입")
        private ReservationTypeEnum reservationType;

        @ApiModelProperty(value = "Login Type Code")
        private LoginTypeEnum loginType;

        @ApiModelProperty(value = "SNS ID")
        private String snsId;

        @ApiModelProperty(value = "SNS Token")
        private String snsToken;

        @ApiModelProperty(value = "Profile URI")
        private String profileUri;

        @ApiModelProperty(value = "추천인 아이디")
        private Long recommendedUserId;

        @ApiModelProperty(value = "광고 동의 여부")
        private Boolean advertiseYn;

        @ApiModelProperty(value = "가입 일시")
        private Long joinDt;

        @ApiModelProperty(value = "최종 로그인 일시")
        private Long lastLoginDt;

        private ResponseJwtDto responseJwtDto;

        @ApiModelProperty(value = "회원 out 상태")
        private UserOutTypeEnum outType;

        @ApiModelProperty(value = "다크 모드 여부")
        private Boolean darkModeYn;

        @ApiModelProperty(value = "운영자 여부")
        private Boolean adminYn;

        public static ResponseUserInfoWithAuthDto toDto(final UserInfoDto userInfoDto, final ResponseJwtDto responseJwtDto, UserDto.RoleDto roleDto) {
            return ResponseUserInfoWithAuthDto.builder()
                    .userId(userInfoDto.getUserId())
                    .username(userInfoDto.getUsername())
                    .name(userInfoDto.getName())
                    .phone(userInfoDto.getPhone())
                    .phoneActiveYn(userInfoDto.getPhoneActiveYn())
                    .birthDate(userInfoDto.getBirthDate())
                    .snsId(userInfoDto.getSnsId())
                    .snsToken(userInfoDto.getSnsToken())
                    .profileUri(userInfoDto.getProfileUri())
                    .advertiseYn(userInfoDto.getAdvertiseYn())
                    .joinDt(userInfoDto.getJoinDt().toEpochSecond())
                    .lastLoginDt(userInfoDto.getLastLoginDt().toEpochSecond())
                    .responseJwtDto(responseJwtDto)
                    .outType(userInfoDto.getOutType())
                    .lastLoginRoleType(userInfoDto.getLastLoginRoleType())
                    .darkModeYn(userInfoDto.getDarkModeYn())
                    .adminYn(userInfoDto.getDarkModeYn())
                    .branchId(roleDto.getBranchId())
                    .branchCount(roleDto.getBranchCount())
                    .reservationType(roleDto.getReservationType())
                    .build();
        }

        public static ResponseUserInfoWithAuthDto toMockup(){
            return ResponseUserInfoWithAuthDto.builder()
                    .userId(1L)
                    .username("gobaebae")
                    .name("고배배")
                    .phone("01012345678")
                    .phoneActiveYn(true)
                    .birthDate(LocalDate.of(2011, 5, 28))
                    .lastLoginRoleType(UserRoleEnum.TUTOR)
                    .responseJwtDto(
                            ResponseJwtDto.builder()
                                    .accessToken("accessToken")
                                    .refreshToken("refreshToken")
                                    .build()
                    )
                    .outType(UserOutTypeEnum.ACTIVE)
                    .build();
        }
    }


    @Getter
    @Builder
    public static class RequestUserSignUpDto {
        @ApiModelProperty(example = "TUTEE")
        @NotNull
        private UserRoleEnum lastLoginRoleType;

        @ApiModelProperty(value = "사용자 ID", example = "123")
        private Long userId;

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

        @ApiModelProperty(value = "전화번호(필수)", example = "01030902825")
        @NotBlank(message = "전화번호는 필수 입력사항입니다.")
        private String phone;

        @ApiModelProperty(value = "전화번호 활성 여부", example = "true")
        private Boolean phoneActiveYn;

        @ApiModelProperty(value = "추천인 아이디")
        private Long recommendedUserId;

        @ApiModelProperty(value = "광고 동의 여부", example = "true")
        private Boolean advertiseYn;

        @ApiModelProperty(value = "가입 일시")
        private ZonedDateTime joinDt;

        @ApiModelProperty(value = "최종 로그인 일시")
        private ZonedDateTime lastLoginDt;

        @ApiModelProperty(value = "성별(M/W/X)", example = "M")
        private String gender;

        @ApiModelProperty(value = "생년월일", example = "2011-05-28")
        private String birthDate;

        public void encodePassword(String password){
            this.password = password;
        }

        public void updatePhoneActiveYn() {
            this.phoneActiveYn = true;
        }

        public User toUserSignUpEntity() {
            return User.builder()
                    .username(username)
                    .password(password)
                    .name(name)
                    .phone(phone)
                    .phoneActiveYn(phoneActiveYn)
                    .recommendedUserId(recommendedUserId)
                    .advertiseYn(advertiseYn)
                    .gender(gender)
                    .lastLoginRoleType(lastLoginRoleType)
                    .joinDt(ZonedDateTime.now(ZoneId.of("UTC")))
                    .lastLoginDt(ZonedDateTime.now(ZoneId.of("UTC")))
                    .build();
        }
    }

    @Getter
    @Builder
    public static class RequestTempUserSignUpDto {

        @ApiModelProperty(example = "TUTEE")
        @NotNull
        private UserRoleEnum lastLoginRoleType;

        @ApiModelProperty(value = "사용자 ID", example = "123")
        private Long userId;

        @ApiModelProperty(value = "유저 id(필수X)", example = "gobaebae")
        private String username;

        @ApiModelProperty(value = "비밀번호(필수X)", example = "asdf1234!")
        private String password;

        @ApiModelProperty(value = "이름", example = "고배배")
        private String name;

        @ApiModelProperty(value = "전화번호(필수)", example = "01030902825")
        @NotBlank(message = "전화번호는 필수 입력사항입니다.")
        private String phone;

        @ApiModelProperty(value = "전화번호 활성 여부", example = "true")
        private Boolean phoneActiveYn;

        @ApiModelProperty(value = "가입 일시")
        private ZonedDateTime joinDt;

        @ApiModelProperty(value = "최종 로그인 일시")
        private ZonedDateTime lastLoginDt;

        @ApiModelProperty(value = "성별(M/W/X)", example = "M")
        private String gender;

        @ApiModelProperty(value = "생년월일", example = "2011-05-28")
        private String birthDate;

        public void makeUsername(String username){
            this.username = username;
        }
        public void encodePassword(String password){
            this.password = password;
        }

        public User toUserSignUpEntity() {
            return User.builder()
                    .username(username)
                    .password(password)
                    .name(name)
                    .phone(phone)
                    .phoneActiveYn(false)
                    .gender(gender)
                    .joinDt(ZonedDateTime.now(ZoneId.of("UTC")))
                    .lastLoginDt(ZonedDateTime.now(ZoneId.of("UTC")))
                    .lastLoginRoleType(lastLoginRoleType)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class RequestUserSignUpSocialCheckDto{

        @ApiModelProperty(value = "sns id(필수)", example = "123456789")
        @NotBlank(message = "sns id는 필수 입력사항입니다.")
        private String snsId;

        @NotNull(message = "snsType은 필수 입력사항입니다.")
        @ApiModelProperty(example = "KAKAO", value = "sns type(필수)")
        private SnsTypeEnum snsType;

        @ApiModelProperty(value = "sns token", example = "123456789")
        private String snsToken;

    }

    @Getter
    @Builder
    public static class RequestUserSignUpSocialDto{

        @ApiModelProperty(example = "TUTEE")
        @NotNull
        private UserRoleEnum lastLoginRoleType;

        @ApiModelProperty(value = "사용자 ID", example = "123")
        private Long userId;

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

        @ApiModelProperty(value = "전화번호(필수)", example = "01030902825")
        @NotBlank(message = "전화번호는 필수 입력사항입니다.")
        private String phone;

        @ApiModelProperty(value = "전화번호 활성 여부", example = "true")
        private Boolean phoneActiveYn;

        @ApiModelProperty(value = "추천인 아이디")
        private Long recommendedUserId;

        @ApiModelProperty(value = "광고 동의 여부", example = "true")
        private Boolean advertiseYn;

        @ApiModelProperty(value = "가입 일시")
        private ZonedDateTime joinDt;

        @ApiModelProperty(value = "최종 로그인 일시")
        private ZonedDateTime lastLoginDt;

        @ApiModelProperty(value = "sns id(필수)", example = "123456789")
        @NotBlank(message = "sns id는 필수 입력사항입니다.")
        private String snsId;

        @NotNull(message = "snsType은 필수 입력사항입니다.")
        @ApiModelProperty(example = "NAVER", value = "sns type(필수)")
        private SnsTypeEnum snsType;

        @ApiModelProperty(value = "sns token", example = "123456789")
        private String snsToken;

        @ApiModelProperty(example = "SNS")
        private LoginTypeEnum loginTypeEnum;

        public void encodePassword(String password){
            this.password = password;
        }

        public void updatePhoneActiveYn() {
            this.phoneActiveYn = true;
        }

        public User toUserSignUpEntity() {
            return User.builder()
                    .username(username)
                    .password(password)
                    .name(name)
                    .phone(phone)
                    .phoneActiveYn(phoneActiveYn)
                    .advertiseYn(advertiseYn)
                    .joinDt(ZonedDateTime.now(ZoneId.of("UTC")))
                    .lastLoginDt(ZonedDateTime.now(ZoneId.of("UTC")))
                    .lastLoginRoleType(lastLoginRoleType)
                    .build();
        }

        public UserSns toSnsIfoSignUpEntity(User user){
            return UserSns.builder()
                    .user(user)
                    .snsType(snsType)
                    .snsId(snsId)
                    .snsToken(snsToken)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class RequestNaverDto {
        @NotEmpty(message = "네이버 로그인중 오류가 발생했습니다.[1]")
        private String code;
        @NotEmpty(message = "네이버 로그인중 오류가 발생했습니다.[2]")
        private String state;
    }


    @Getter
    public static class NaverUserProfileDto {
        @JsonProperty("id")
        private String snsId;

        @JsonProperty("name")
        private String name;

        @Builder
        private NaverUserProfileDto(String snsId, String name) {
            this.snsId = snsId;
            this.name = name;
        }

    }

    @Getter
    @Builder
    public static class ResponseNaverTokenDto {
        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("refresh_token")
        private String refreshToken;

        @JsonProperty("token_type")
        private String tokenType;

        @JsonProperty("expires_in")
        private String expiresIn;

        @JsonProperty("error")
        private String error;

        @JsonProperty("error_description")
        private String errorDescription;

        @Builder
        private ResponseNaverTokenDto(String accessToken, String refreshToken, String tokenType, String expiresIn,
                                      String error, String errorDescription) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.tokenType = tokenType;
            this.expiresIn = expiresIn;
            this.error = error;
            this.errorDescription = errorDescription;
        }
    }

    @Getter
    @Builder
    public static class ResponseNaverProfileDto {
        @JsonProperty("resultcode")
        private String resultCode;

        @JsonProperty("message")
        private String message;

        @JsonProperty("response")
        private NaverUserProfileDto naverUserProfileDto;

        @Builder
        private ResponseNaverProfileDto(String resultCode, String message, NaverUserProfileDto naverUserProfileDto) {
            this.resultCode = resultCode;
            this.message = message;
            this.naverUserProfileDto = naverUserProfileDto;
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
                    .birthDate(birthDate)
                    .phone(phone)
                    .phoneActiveYn(phoneActiveYn)
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
    public static class ResponseUser{

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

        public static ResponseUser toDto(final User user){
            return ResponseUser.builder()
                    .userId(user.getId())
                    .username(user.getUsername().substring(0, user.getUsername().length() - 2) + "**")
                    .name(user.getName().length() < 4
                            ? user.getName().substring(0, user.getName().length() - 1) + "*"
                            : user.getName().substring(0, user.getName().length() - 2) + "**")
                    .phone(user.getPhone())
                    .birthDate(user.getBirthDate())
                    .build();
        }
    }
}
