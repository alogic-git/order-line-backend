package com.ptglue.common.user.model.dto;

import com.ptglue.common.user.model.entity.ActivationCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ActivationCodeDto {

        @Getter
        @Builder
        public static class PhoneActivationInfoDto{

                private Long activationCodeId;

                @NotBlank(message = "휴대폰 번호는 필수 입력값입니다.")
                @ApiModelProperty(value = "휴대폰 번호(필수)", example = "01030902825")
                private String phone;

                @NotBlank(message = "인증코드는 필수 입력값입니다.")
                @ApiModelProperty(value = "인증코드(필수)", example = "000000")
                private String activationCode;

                private Boolean activationYn;

                private LocalDateTime regDateTime;

                public static PhoneActivationInfoDto toDto(final ActivationCode activationCode) {
                    return PhoneActivationInfoDto.builder()
                            .activationCodeId(activationCode.getId())
                            .phone(activationCode.getPhone())
                            .activationCode(activationCode.getActivationCode())
                            .activationYn(activationCode.getActivationYn())
                            .regDateTime(activationCode.getRegDateTime())
                            .build();
                }

                public ActivationCode toEntity(){
                        return ActivationCode.builder()
                                .phone(phone)
                                .activationCode(activationCode)
                                .build();
                }
        }

        @Getter
        @Builder
        public static class VerifyRequestDto {

                @ApiModelProperty(value = "휴대폰 번호(필수)", example = "01030902825")
                @NotBlank(message = "휴대폰 번호는 필수 입력값입니다.")
                private String phone;

                @ApiModelProperty(value = "인증코드(필수)", example = "000000")
                @NotBlank(message = "인증코드는 필수 입력값입니다.")
                private String activationCode;
        }

        @Getter
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class RequestActivationCodeDto {
                @NotBlank(message = "휴대폰 번호는 필수 입력값입니다.")
                @ApiModelProperty(value = "휴대폰 번호(필수)", example = "01030902825")
                private String phone;

        }

        @Getter
        @Builder
        public static class FindUserRequestDto {

                @NotBlank(message = "휴대폰 번호는 필수 입력값입니다.")
                @ApiModelProperty(value = "휴대폰 번호(필수)", example = "01030902825")
                private String phone;

                @ApiModelProperty(value = "인증코드(필수)", example = "000000")
                @NotBlank(message = "인증코드는 필수 입력값입니다.")
                private String activationCode;
        }
}
