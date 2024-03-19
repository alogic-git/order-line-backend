package com.orderline.material.model.dto;

import com.orderline.material.enums.ProductStatusEnum;
import com.orderline.material.model.entity.Product;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductDto {

    @Getter
    @Builder
    public static class ResponseProductDto {
        @ApiModelProperty(value = "자재 ID", example = "1")
        private Long id;

        @ApiModelProperty(value = "자재 이름", example = "자재 명")
        private String name;

        @ApiModelProperty(value = "모델 번호", example = "12345677")
        private String modelNumber;

        @ApiModelProperty(value = "상태", example = "주문 가능")
        private ProductStatusEnum status;

        @ApiModelProperty(value = "단가", example = "10000")
        private int unitPrice;

        @ApiModelProperty(value = "할인가", example = "8000")
        private int salePrice;

        @ApiModelProperty(value = "재고 수량", example = "100")
        private int availableStock;

        @ApiModelProperty(value = "색상", example = "black")
        private String color;

        @ApiModelProperty(value = "이미지 URI", example = "http://image.com")
        private String imageUri;

        public static ResponseProductDto toDto(Product product) {
            return ResponseProductDto.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .modelNumber(product.getModelNumber())
                    .status(product.getStatus())
                    .unitPrice(product.getUnitPrice())
                    .salePrice(product.getSalePrice())
                    .availableStock(product.getAvailableStock())
                    .color(product.getColor())
                    .imageUri(product.getImageUri())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class RequestCreateProductDto {
        @ApiModelProperty(value = "자재 이름", example = "자재 명")
        private String name;

        @ApiModelProperty(value = "회사명", example = "에이로직")
        private String companyName;

        @ApiModelProperty(value = "모델 번호", example = "12345677")
        private String modelNumber;

        @ApiModelProperty(value = "단가", example = "10000")
        private int unitPrice;

        @ApiModelProperty(value = "할인가", example = "8000")
        private int salePrice;

        @ApiModelProperty(value = "재고 수량", example = "100")
        private int availableStock;

        @ApiModelProperty(value = "색상", example = "black")
        private String color;

        @ApiModelProperty(value = "이미지 URI", example = "http://image.com")
        private String imageUri;

        @ApiModelProperty(value = "특이 사항", example = "특이 사항입니다.")
        private String specifics;

        @ApiModelProperty(value = "상태", example = "주문 가능")
        private ProductStatusEnum status;

        public Product toEntity() {
            return Product.builder()
                    .name(name)
                    .companyName(companyName)
                    .modelNumber(modelNumber)
                    .unitPrice(unitPrice)
                    .salePrice(salePrice)
                    .availableStock(availableStock)
                    .color(color)
                    .imageUri(imageUri)
                    .specifics(specifics)
                    .status(status)
                    .build();
        }
    }

    @Builder
    @Getter
    public static class ResponseProductListDto {

        @ApiModelProperty(value = "자재 목록")
        private List<ResponseProductDto> results;

        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지 당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static ResponseProductListDto build(Page<ResponseProductDto> responseDtoPage, Integer currentPage, Integer maxResults) {
            return ResponseProductListDto.builder()
                    .results(responseDtoPage.getContent())
                    .maxResults(maxResults)
                    .currentPage(currentPage)
                    .totalPages(responseDtoPage.getTotalPages())
                    .totalElements(responseDtoPage.getTotalElements())
                    .build();
        }
    }


}
