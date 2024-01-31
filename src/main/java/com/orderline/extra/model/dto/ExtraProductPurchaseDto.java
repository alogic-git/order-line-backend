package com.orderline.extra.model.dto;

import com.orderline.extra.enums.ExtraPaidStatusTypeEnum;
import com.orderline.extra.enums.ExtraStatusTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import com.orderline.extra.model.entity.ExtraProductPurchase;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExtraProductPurchaseDto {

    @Getter
    @Builder
    public static class ResponseExtraProductPurchaseDto{
        @ApiModelProperty(value = "부가상품 구입 Id")
        private Long id;

        @ApiModelProperty(value = "지점 Id")
        private Long branchId;

        @ApiModelProperty(value = "부가상품 Id")
        private Long extraProductId;

        @ApiModelProperty(value = "회원 Id")
        private Long tuteeId;

        @ApiModelProperty(value = "구매 일자")
        private LocalDate purchaseDate;

        @ApiModelProperty(value = "구매 상태")
        private ExtraStatusTypeEnum statusType;

        @ApiModelProperty(value = "납부 상태")
        private ExtraPaidStatusTypeEnum paidStatusType;

        public static ResponseExtraProductPurchaseDto toDto(ExtraProductPurchase extraProductPurchase){
            return ResponseExtraProductPurchaseDto.builder()
                    .id(extraProductPurchase.getId())
                    .branchId(extraProductPurchase.getBranch().getId())
                    .extraProductId(extraProductPurchase.getExtraProduct().getId())
                    .tuteeId(extraProductPurchase.getTutee().getId())
                    .statusType(extraProductPurchase.getStatusType())
                    .paidStatusType(extraProductPurchase.getPaidStatusType())
                    .build();
        }
    }
}
