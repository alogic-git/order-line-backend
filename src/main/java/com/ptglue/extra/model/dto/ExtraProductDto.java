package com.ptglue.extra.model.dto;

import io.swagger.annotations.ApiModelProperty;
import com.ptglue.extra.model.entity.ExtraProduct;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExtraProductDto {

    @Getter
    @Builder
    public static class ResponseExtraProductDto {
        @ApiModelProperty(value = "부가상품 Id")
        private Long id;

        @ApiModelProperty(value = "지점 Id")
        private Long branchId;

        @ApiModelProperty(value = "부가 상품명")
        private String extraProductName;

        @ApiModelProperty(value = "부가 상품 가격")
        private Integer price;

        @ApiModelProperty(value = "개인 메모")
        private String privateMemo;

        @ApiModelProperty(value = "공개 메모")
        private String publicMemo;

        @ApiModelProperty(value = "보관 여부")
        private Boolean archiveYn;

        public static ResponseExtraProductDto toDto(ExtraProduct extraProduct){
            return ResponseExtraProductDto.builder()
                    .id(extraProduct.getId())
                    .branchId(extraProduct.getBranch().getId())
                    .extraProductName(extraProduct.getExtraProductName())
                    .price(extraProduct.getPrice())
                    .privateMemo(extraProduct.getPrivateMemo())
                    .publicMemo(extraProduct.getPublicMemo())
                    .build();
        }
    }
}
