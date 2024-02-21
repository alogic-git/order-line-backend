package com.orderline.order.model.dto;


import com.orderline.basic.utils.TimeFunction;
import com.orderline.order.enums.OrderStatusEnum;
import com.orderline.order.model.entity.Order;
import com.orderline.site.model.entity.Site;
import com.orderline.user.model.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderDto {

    @Getter
    @Builder
    public static class ResponseOrderDto {
        @ApiModelProperty(value = "발주 ID", example = "1")
        private Long id;

        @ApiModelProperty(value = "현장명", example = "가락")
        private String siteName;

        @ApiModelProperty(value = "발주 주소", example = "서울시 강남구 역삼동 123-4")
        private String address;

        @ApiModelProperty(value = "특이사항", example = "발주 특이사항입니다.")
        private String specifics;

        @ApiModelProperty(value = "담당자명", example = "홍길동")
        private String managerName;

        @ApiModelProperty(value = "긴급 여부", example = "false")
        private Boolean emergencyYn;

        @ApiModelProperty(value = "진행 상태", example = "발주완료")
        private OrderStatusEnum status;

        public static ResponseOrderDto toDto(Order order, Site site) {
            return ResponseOrderDto.builder()
                    .id(order.getId())
                    .siteName(site.getName())
                    .address(order.getAddress())
                    .specifics(order.getSpecifics())
                    .managerName(order.getManagerName())
                    .emergencyYn(order.getEmergencyYn())
                    .status(order.getStatus())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class RequestCreateOrderDto {
        @ApiModelProperty(value = "현장 ID", example = "1")
        private Long siteId;

        @ApiModelProperty(value = "발주 주소", example = "서울시 강남구 역삼동 123-4")
        private String address;

        @ApiModelProperty(value = "특이 사항", example = "발주 특이사항입니다.")
        private String specifics;

        @ApiModelProperty(value = "담당자명", example = "홍길동")
        private String managerName;

        @ApiModelProperty(value = "긴급 여부", example = "false")
        private Boolean emergencyYn;

        @ApiModelProperty(value = "발주일", example = "2021-01-01")
        private Long orderDt;

        @ApiModelProperty(value = "배송 요청일", example = "2021-01-01")
        private Long requestDt;

        public Order toEntity() {
            return Order.builder()
                    .address(address)
                    .specifics(specifics)
                    .managerName(managerName)
                    .emergencyYn(emergencyYn)
                    .orderDt(TimeFunction.toZonedDateTime(orderDt))
                    .requestDt(TimeFunction.toZonedDateTime(requestDt))
                    .build();
        }
    }

    @Builder
    @Getter
    public static class ResponseOrderListDto {

        @ApiModelProperty(value = "발주 목록")
        private List<ResponseOrderDto> results;

        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static ResponseOrderListDto build(Page<ResponseOrderDto> responseDtoPage, Integer currentPage, Integer maxResults) {
            return ResponseOrderListDto.builder()
                    .results(responseDtoPage.getContent())
                    .maxResults(maxResults)
                    .currentPage(currentPage)
                    .totalPages(responseDtoPage.getTotalPages())
                    .totalElements(responseDtoPage.getTotalElements())
                    .build();
        }
    }


}
