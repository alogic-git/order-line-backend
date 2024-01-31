package com.orderline.ticket.model.dto;

import com.orderline.ticket.enums.TicketPaidStatusTypeEnum;
import com.orderline.ticket.enums.TicketStatusTypeEnum;
import com.orderline.payment.model.dto.TuteePaymentHistoryDto;
import io.swagger.annotations.ApiModelProperty;

import com.orderline.ticket.model.entity.Ticket;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TicketTuteeDto {
    @Getter
    @Builder
    public static class ResponseTicketTuteeDetailDto{
        @ApiModelProperty(value = "수강권 ID")
        private Long ticketId;

        @ApiModelProperty(value = "수강권 이름")
        private String ticketName;

        @ApiModelProperty(name = "기본 유효 기간")
        private Integer period;

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

        public static ResponseTicketTuteeDetailDto toDto(final Ticket ticket){
            return ResponseTicketTuteeDetailDto.builder()
                    .ticketId(ticket.getId())
                    .ticketName(ticket.getTicketName())
                    .period(ticket.getPeriod())
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
                    .publicMemo(ticket.getPublicMemo())
                    .stopEnableYn(ticket.getStopEnableYn())
                    .stopEnableDate(ticket.getStopEnableDate())
                    .stopEnableTotalDate(ticket.getStopEnableTotalDate())
                    .stopEnableCount(ticket.getStopEnableCount())
                    .currentStopEnableTotalDate(ticket.getCurrentStopEnableTotalDate())
                    .currentStopEnableCount(ticket.getCurrentStopEnableCount())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ResponseTicketTuteeDto{
        @ApiModelProperty(value = "수강권 Id")
        private Long ticketId;

        @ApiModelProperty(value = "수강권 이름")
        private String ticketName;

        @ApiModelProperty(name = "종료 일자")
        private LocalDate endDate;

        @ApiModelProperty(name = "상태")
        private TicketStatusTypeEnum statusType;

        @ApiModelProperty(name = "예약 가능 횟수")
        private Integer availableReservationCount;

        @ApiModelProperty(name = "잔여 예약 횟수")
        private Integer remainReservationCount;

        @ApiModelProperty(name = "총 예약 횟수")
        private Integer totalReservationCount;

        public static ResponseTicketTuteeDto toDto(final Ticket ticket){
            return ResponseTicketTuteeDto.builder()
                    .ticketId(ticket.getId())
                    .ticketName(ticket.getTicketName())
                    .endDate(ticket.getEndDate())
                    .statusType(ticket.getStatusType())
                    .totalReservationCount(ticket.getTotalReservationCount())
                    .availableReservationCount(ticket.getAvailableReservationCount())
                    .remainReservationCount(ticket.getRemainReservationCount())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ResponseTicketTuteeListDto{
        @ApiModelProperty(value = "수강권 리스트")
        private List<TicketTuteeDto.ResponseTicketTuteeDto> results;

        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static TicketTuteeDto.ResponseTicketTuteeListDto build(Page<TicketTuteeDto.ResponseTicketTuteeDto> responseDtoPage, Integer currentPage, Integer maxResults){
            return TicketTuteeDto.ResponseTicketTuteeListDto.builder()
                    .results(responseDtoPage.getContent())
                    .maxResults(maxResults)
                    .currentPage(currentPage)
                    .totalPages(responseDtoPage.getTotalPages())
                    .totalElements(responseDtoPage.getTotalElements())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ResponseTicketPaymentDto{
        @ApiModelProperty(value = "수강권 Id")
        private Long ticketId;

        @ApiModelProperty(value = "수강권 이름")
        private String ticketName;

        @ApiModelProperty(name = "시작 일자")
        private LocalDate startDate;

        @ApiModelProperty(name = "종료 일자")
        private LocalDate endDate;

        @Enumerated(EnumType.STRING)
        @ApiModelProperty(name = "상태")
        private TicketStatusTypeEnum statusType;

        @Enumerated(EnumType.STRING)
        @ApiModelProperty(name = "지불 방법")
        private TicketPaidStatusTypeEnum paidStatusType;

        @ApiModelProperty(name = "가격")
        private Integer price;

        @ApiModelProperty(name = "총 결제 금액")
        private Integer totalPaidPrice;

        @ApiModelProperty(name = "총 환불 금액")
        private Integer totalRefundPrice;

        @ApiModelProperty(name = "포함 클래스")
        private Integer innerKlass;

        @ApiModelProperty(name = "결제 내역")
        private List<TuteePaymentHistoryDto.ResponseTuteePaymentHistoryDto> paymentHistoryDtoList;

        public static ResponseTicketPaymentDto toDto(Ticket ticket, List<TuteePaymentHistoryDto.ResponseTuteePaymentHistoryDto> tuteePaymentHistory){
            return ResponseTicketPaymentDto.builder()
                    .ticketId(ticket.getId())
                    .ticketName(ticket.getTicketName())
                    .startDate(ticket.getStartDate())
                    .endDate(ticket.getEndDate())
                    .statusType(ticket.getStatusType())
                    .paidStatusType(ticket.getPaidStatusType())
                    .price(ticket.getPrice())
                    .totalPaidPrice(ticket.getTotalPaidPrice())
                    .totalRefundPrice(ticket.getTotalRefundPrice())
                    .innerKlass(ticket.getInnerKlass())
                    .paymentHistoryDtoList(tuteePaymentHistory)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ResponseTicketPaymentListDto{
        @ApiModelProperty(value = "수강권 결제 내역 리스트")
        private List<TicketTuteeDto.ResponseTicketPaymentDto> results;

        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static TicketTuteeDto.ResponseTicketPaymentListDto build(Page<TicketTuteeDto.ResponseTicketPaymentDto> responseDtoPage, Integer currentPage, Integer maxResults){
            return TicketTuteeDto.ResponseTicketPaymentListDto.builder()
                    .results(responseDtoPage.getContent())
                    .maxResults(maxResults)
                    .currentPage(currentPage)
                    .totalPages(responseDtoPage.getTotalPages())
                    .totalElements(responseDtoPage.getTotalElements())
                    .build();
        }
    }
}

