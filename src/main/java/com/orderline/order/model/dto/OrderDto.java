package com.orderline.order.model.dto;


import com.orderline.basic.utils.TimeFunction;
import com.orderline.order.enums.OrderStatusEnum;
import com.orderline.order.model.entity.Order;
import com.orderline.site.model.entity.Site;
import com.orderline.user.model.entity.User;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.ZonedDateTime;
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

        @ApiModelProperty(value = "발주일", example = "1709106929")
        private Long orderDt;

        @ApiModelProperty(value = "배송 요청일", example = "1709106929")
        private Long requestDt;

        @ApiModelProperty(value = "배송 예정일", example = "1709106929")
        private Long expectedDt;

        public static ResponseOrderDto toDto(Order order) {
            return ResponseOrderDto.builder()
                    .id(order.getId())
                    .siteName(order.getSite().getName())
                    .address(order.getAddress())
                    .specifics(order.getSpecifics())
                    .managerName(order.getManagerName())
                    .emergencyYn(order.getEmergencyYn())
                    .status(order.getStatus())
                    .orderDt(TimeFunction.toUnixTime(order.getOrderDt()))
                    .requestDt(TimeFunction.toUnixTime(order.getRequestDt()))
                    .expectedDt(TimeFunction.toUnixTime(order.getExpectedDt()))
                    .build();
        }

        public static ResponseOrderDto toDto(Order order, ZonedDateTime expectedDt) { //Order, Material update 후 배송 예정일 수정 시 사용
            return ResponseOrderDto.builder()
                    .id(order.getId())
                    .siteName(order.getSite().getName())
                    .address(order.getAddress())
                    .specifics(order.getSpecifics())
                    .managerName(order.getManagerName())
                    .emergencyYn(order.getEmergencyYn())
                    .status(order.getStatus())
                    .orderDt(TimeFunction.toUnixTime(order.getOrderDt()))
                    .requestDt(TimeFunction.toUnixTime(order.getRequestDt()))
                    .expectedDt(TimeFunction.toUnixTime(expectedDt))
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

        @ApiModelProperty(value = "상태" , example = "발주 완료")
        private OrderStatusEnum status;

        @ApiModelProperty(value = "발주일", example = "1709106929")
        private Long orderDt;

        @ApiModelProperty(value = "배송 요청일", example = "1709106929")
        private Long requestDt;

        @ApiModelProperty(value = "배송 예정일", example = "1709106929")
        private Long expectedDt;

        public Order toEntity(Site site) {
            return Order.builder()
                    .site(site)
                    .address(address)
                    .specifics(specifics)
                    .status(OrderStatusEnum.ONGOING)
                    .managerName(managerName)
                    .emergencyYn(emergencyYn)
                    .totalPrice(0)
                    .orderDt(TimeFunction.toZonedDateTime(orderDt))
                    .requestDt(TimeFunction.toZonedDateTime(requestDt))
                    .expectedDt(TimeFunction.toZonedDateTime(expectedDt))
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
