package com.ptglue.ticket.model.dto;


import com.ptglue.common.user.model.entity.User;
import com.ptglue.product.model.entity.Product;
import com.ptglue.ticket.enums.TicketPaidStatusTypeEnum;
import com.ptglue.ticket.enums.TicketStatusTypeEnum;
import io.swagger.annotations.ApiModelProperty;

import com.ptglue.branch.model.entity.Branch;
import com.ptglue.ticket.model.entity.Ticket;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TicketDto {
    @Getter
    @Builder
    public static class ResponseTicketDto{

        @ApiModelProperty(value = "티켓 ID")
        private Long ticketId;

        @ApiModelProperty(value = "지점 ID")
        private Long branchId;

        @ApiModelProperty(value = "수강권 ID")
        private Long productId;

        @ApiModelProperty(value = "유저 ID")
        private Long userId;

        @ApiModelProperty(value = "수강권 이름")
        private String ticketName;

        @ApiModelProperty(name = "기본 유효 기간")
        private Integer period;

        @ApiModelProperty(name = "가격")
        private Integer price;

        @ApiModelProperty(name = "총 결제 금액")
        private Integer totalPaidPrice;

        @ApiModelProperty(name = "총 환불 금액")
        private Integer totalRefundPrice;

        @ApiModelProperty(name = "예약 가능 횟수")
        private Integer availableReservationCount;

        @ApiModelProperty(name = "타지점 예약 가능 횟수")
        private Integer availableOtherBranchReservationCount;

        @ApiModelProperty(name = "잔여 예약 횟수")
        private Integer remainReservationCount;

        @ApiModelProperty(name = "타지점 잔여 예약 횟수")
        private Integer remainOtherBranchReservationCount;

        @ApiModelProperty(name = "총 예약 횟수")
        private Integer totalReservationCount;

        @ApiModelProperty(name = "타지점 총 예약 횟수")
        private Integer totalOtherBranchReservationCount;

        @ApiModelProperty(name = "상태")
        private TicketStatusTypeEnum statusType;

        @ApiModelProperty(name = "납부 상태")
        private TicketPaidStatusTypeEnum paidStatusType;

        @ApiModelProperty(name = "시작 일자")
        private LocalDate startDate;

        @ApiModelProperty(name = "종료 일자")
        private LocalDate endDate;

        @ApiModelProperty(name = "비공개 메모")
        private String privateMemo;

        @ApiModelProperty(name = "공개 메모")
        private String publicMemo;

        @ApiModelProperty(name = "일시 정지 가능 여부")
        private Boolean stopEnableYn;

        @ApiModelProperty(name = "일시 정지 가능 일수")
        private Integer stopEnableDate;

        @ApiModelProperty(name = "일시 정지 총 가능 일수")
        private Integer stopEnableTotalDate;

        @ApiModelProperty(name = "일시 정지 가능 횟수")
        private Integer stopEnableCount;

        @ApiModelProperty(name = "현재 일시 정지 총 가능 일수")
        private Integer currentStopEnableTotalDate;

        @ApiModelProperty(name = "현재 일시 정지 가능 횟수")
        private Integer currentStopEnableCount;

        @ApiModelProperty(name = "포함 클랙스 수")
        private Integer klassCount;

        @ApiModelProperty(name = "보관 여부")
        private Boolean archiveYn;

        public static ResponseTicketDto toDto(final Ticket ticket){
            return ResponseTicketDto.builder()
                    .ticketId(ticket.getId())
                    .branchId(ticket.getBranch().getId())
                    .productId(ticket.getProduct().getId())
                    .userId(ticket.getUser().getId())
                    .ticketName(ticket.getTicketName())
                    .period(ticket.getPeriod())
                    .price(ticket.getPrice())
                    .totalPaidPrice(ticket.getTotalPaidPrice())
                    .totalRefundPrice(ticket.getTotalRefundPrice())
                    .availableReservationCount(ticket.getAvailableReservationCount())
                    .availableOtherBranchReservationCount(ticket.getAvailableOtherBranchReservationCount())
                    .remainReservationCount(ticket.getRemainReservationCount())
                    .remainOtherBranchReservationCount(ticket.getRemainOtherBranchReservationCount())
                    .totalReservationCount(ticket.getTotalReservationCount())
                    .totalOtherBranchReservationCount(ticket.getTotalOtherBranchReservationCount())
                    .statusType(ticket.getStatusType())
                    .paidStatusType(ticket.getPaidStatusType())
                    .startDate(ticket.getStartDate())
                    .endDate(ticket.getEndDate())
                    .privateMemo(ticket.getPrivateMemo())
                    .publicMemo(ticket.getPublicMemo())
                    .stopEnableYn(ticket.getStopEnableYn())
                    .stopEnableDate(ticket.getStopEnableDate())
                    .stopEnableTotalDate(ticket.getStopEnableTotalDate())
                    .stopEnableCount(ticket.getStopEnableCount())
                    .currentStopEnableTotalDate(ticket.getCurrentStopEnableTotalDate())
                    .currentStopEnableCount(ticket.getCurrentStopEnableCount())
                    .archiveYn(ticket.getArchiveYn())
                    .build();
        }

        public static ResponseTicketDto toDto(final Ticket ticket, Integer klassCount){
            return ResponseTicketDto.builder()
                    .ticketId(ticket.getId())
                    .branchId(ticket.getBranch().getId())
                    .productId(ticket.getProduct().getId())
                    .userId(ticket.getUser().getId())
                    .ticketName(ticket.getTicketName())
                    .period(ticket.getPeriod())
                    .price(ticket.getPrice())
                    .totalPaidPrice(ticket.getTotalPaidPrice())
                    .totalRefundPrice(ticket.getTotalRefundPrice())
                    .availableReservationCount(ticket.getAvailableReservationCount())
                    .availableOtherBranchReservationCount(ticket.getAvailableOtherBranchReservationCount())
                    .remainReservationCount(ticket.getRemainReservationCount())
                    .remainOtherBranchReservationCount(ticket.getRemainOtherBranchReservationCount())
                    .totalReservationCount(ticket.getTotalReservationCount())
                    .totalOtherBranchReservationCount(ticket.getTotalOtherBranchReservationCount())
                    .statusType(ticket.getStatusType())
                    .paidStatusType(ticket.getPaidStatusType())
                    .startDate(ticket.getStartDate())
                    .endDate(ticket.getEndDate())
                    .privateMemo(ticket.getPrivateMemo())
                    .publicMemo(ticket.getPublicMemo())
                    .stopEnableYn(ticket.getStopEnableYn())
                    .stopEnableDate(ticket.getStopEnableDate())
                    .stopEnableTotalDate(ticket.getStopEnableTotalDate())
                    .stopEnableCount(ticket.getStopEnableCount())
                    .currentStopEnableTotalDate(ticket.getCurrentStopEnableTotalDate())
                    .currentStopEnableCount(ticket.getCurrentStopEnableCount())
                    .klassCount(klassCount)
                    .archiveYn(ticket.getArchiveYn())
                    .build();
        }
    }

    @Builder
    @Getter
    public static class ResponseTicketListDto {
        @ApiModelProperty(value = "티켓 리스트")
        private List<TicketDto.ResponseTicketDto> results;
        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지 당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static ResponseTicketListDto build(Page<TicketDto.ResponseTicketDto> responseDtoPage, Integer currentPage, Integer maxResults){
            return ResponseTicketListDto.builder()
                    .results(responseDtoPage.getContent())
                    .maxResults(maxResults)
                    .currentPage(currentPage)
                    .totalPages(responseDtoPage.getTotalPages())
                    .totalElements(responseDtoPage.getTotalElements())
                    .build();
        }
    }

    @Builder
    @Getter
    public static class RequestTicketDto {
        @ApiModelProperty(value = "수강권 id", example = "6")
        private Long productId;

        @ApiModelProperty(value = "유저 id", example = "266")
        private Long userId;

        @ApiModelProperty(value = "수강권 이름")
        private String ticketName;

        @ApiModelProperty(value = "기본 유효 기간")
        private Integer period;

        @ApiModelProperty(value = "가격")
        private Integer price;

        @ApiModelProperty(value = "총 결제 금액")
        private Integer totalPaidPrice;

        @ApiModelProperty(name = "총 환불 금액")
        private Integer totalRefundPrice;

        @ApiModelProperty(name = "예약 가능 횟수")
        private Integer availableReservationCount;

        @ApiModelProperty(name = "타지점 예약 가능 횟수")
        private Integer availableOtherBranchReservationCount;

        @ApiModelProperty(name = "잔여 예약 횟수")
        private Integer remainReservationCount;

        @ApiModelProperty(name = "타지점 잔여 예약 횟수")
        private Integer remainOtherBranchReservationCount;

        @ApiModelProperty(name = "총 예약 횟수")
        private Integer totalReservationCount;

        @ApiModelProperty(name = "타지점 총 예약 횟수")
        private Integer totalOtherBranchReservationCount;

        @ApiModelProperty(name = "상태", example = "BEFORE")
        private TicketStatusTypeEnum statusType;

        @ApiModelProperty(name = "납부 상태", example = "PAID")
        private TicketPaidStatusTypeEnum paidStatusType;

        @ApiModelProperty(name = "시작 일자")
        private LocalDate startDate;

        @ApiModelProperty(name = "종료 일자")
        private LocalDate endDate;

        @ApiModelProperty(name = "비공개 메모")
        private String privateMemo;

        @ApiModelProperty(name = "공개 메모")
        private String publicMemo;

        @ApiModelProperty(name = "일시 정지 가능 여부")
        private Boolean stopEnableYn;

        @ApiModelProperty(name = "일시 정지 가능 일수")
        private Integer stopEnableDate;

        @ApiModelProperty(name = "일시 정지 총 가능 일수")
        private Integer stopEnableTotalDate;

        @ApiModelProperty(name = "일시 정지 가능 횟수")
        private Integer stopEnableCount;

        @ApiModelProperty(name = "현재 일시 정지 총 가능 일수")
        private Integer currentStopEnableTotalDate;

        @ApiModelProperty(name = "현재 일시 정지 가능 횟수")
        private Integer currentStopEnableCount;

        public Ticket toEntity(Branch branch, Product product, User user){
            return Ticket.builder()
                    .branch(branch)
                    .product(product)
                    .user(user)
                    .ticketName(ticketName)
                    .period(period)
                    .price(price)
                    .totalPaidPrice(totalPaidPrice)
                    .totalRefundPrice(totalRefundPrice)
                    .availableReservationCount(availableReservationCount)
                    .availableOtherBranchReservationCount(availableOtherBranchReservationCount)
                    .remainReservationCount(remainReservationCount)
                    .remainOtherBranchReservationCount(remainOtherBranchReservationCount)
                    .totalReservationCount(totalReservationCount)
                    .totalOtherBranchReservationCount(totalOtherBranchReservationCount)
                    .statusType(statusType)
                    .paidStatusType(paidStatusType)
                    .startDate(startDate)
                    .endDate(endDate)
                    .privateMemo(privateMemo)
                    .publicMemo(publicMemo)
                    .stopEnableYn(stopEnableYn)
                    .stopEnableDate(stopEnableDate)
                    .stopEnableTotalDate(stopEnableTotalDate)
                    .stopEnableCount(stopEnableCount)
                    .currentStopEnableTotalDate(currentStopEnableTotalDate)
                    .currentStopEnableCount(currentStopEnableCount)
                    .build();
        }
    }
}

