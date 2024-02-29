package com.orderline.material.model.dto;


import com.orderline.basic.utils.TimeFunction;
import com.orderline.material.model.entity.Material;
import com.orderline.material.model.entity.Product;
import com.orderline.order.enums.OrderStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MaterialDto {

    @Getter
    @Builder
    public static class ResponseMaterialDto {
        @ApiModelProperty(value = "자재 ID", example = "1")
        private Long id;

        @ApiModelProperty(value = "자재 이름", example = "자재 명")
        private String name;

        @ApiModelProperty(value = "진행 상태", example = "진행중")
        private OrderStatusEnum status;

        @ApiModelProperty(value = "단가", example = "10000")
        private int unitPrice;

        @ApiModelProperty(value = "할인가", example = "8000")
        private int salePrice;

        @ApiModelProperty(value = "재고 수량", example = "1000")
        private int availableStock;

        @ApiModelProperty(value = "수량", example = "50")
        private int quantity;

        @ApiModelProperty(value = "총 가격", example = "500000")
        private int totalPrice;

        @ApiModelProperty(value = "배송 요청일", example = "1709106929")
        private Long requestDt;

        public static ResponseMaterialDto toDto(Material material, Product product) {
            return ResponseMaterialDto.builder()
                    .id(material.getId())
                    .name(material.getName())
                    .status(material.getStatus())
                    .unitPrice(product.getUnitPrice())
                    .salePrice(product.getSalePrice())
                    .quantity(material.getQuantity())
                    .totalPrice(material.getTotalPrice())
                    .requestDt(material.getRequestDt().toEpochSecond())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class RequestCreateMaterialDto {
        @ApiModelProperty(value = "발주 ID", example = "1")
        private Long orderId;

        @ApiModelProperty(value = "자재 ID", example = "1")
        private Long productId;

        @ApiModelProperty(value = "수량", example = "50")
        private int quantity;

        @ApiModelProperty(value = "진행 상태", example = "ONGOING")
        private OrderStatusEnum status;

        @ApiModelProperty(value = "특이 사항", example = "특이 사항입니다.")
        private String specifics;

        @ApiModelProperty(value = "배송 요청일", example = "1709106929")
        private Long requestDt;

        @ApiModelProperty(value = "배송 예정일", example = "1709106929")
        private Long expectedDt;

        public Material toEntity(Product product) {
            return Material.builder()
                    .product(product)
                    .name(product.getName())
                    .quantity(quantity)
                    .specifics(specifics)
                    .totalPrice(product.getUnitPrice() * quantity)
                    .status(status)
                    .requestDt(TimeFunction.toZonedDateTime(requestDt))
                    .expectedDt(TimeFunction.toZonedDateTime(expectedDt))
                    .build();
        }
    }

    @Getter
    @Builder
    public static class RequestUpdateMaterialDto {
        @ApiModelProperty(value = "수량", example = "50")
        private int quantity;

        @ApiModelProperty(value = "진행 상태", example = "ONGOING")
        private OrderStatusEnum status;

        @ApiModelProperty(value = "특이 사항", example = "특이 사항입니다.")
        private String specifics;

        @ApiModelProperty(value = "배송 요청일", example = "1709106929")
        private Long requestDt;

        @ApiModelProperty(value = "배송 예정일", example = "1709106929")
        private Long expectedDt;

    }

    @Builder
    @Getter
    public static class ResponseMaterialListDto {

        @ApiModelProperty(value = "자재 목록")
        private List<ResponseMaterialDto> results;

        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static ResponseMaterialListDto build(Page<ResponseMaterialDto> responseDtoPage, Integer currentPage, Integer maxResults) {
            return ResponseMaterialListDto.builder()
                    .results(responseDtoPage.getContent())
                    .maxResults(maxResults)
                    .currentPage(currentPage)
                    .totalPages(responseDtoPage.getTotalPages())
                    .totalElements(responseDtoPage.getTotalElements())
                    .build();
        }
    }


}
