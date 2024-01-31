package com.orderline.payment.model.dto;

import com.orderline.payment.enums.PaymentHistoryPaidTypeEnum;
import com.orderline.payment.model.entity.TuteePaymentHistory;
import com.orderline.payment.enums.PaymentHistoryPaidMethodTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TuteePaymentHistoryDto {
    @Builder
    @Getter
    public static class ResponseTuteePaymentHistoryDto{
        @ApiModelProperty(value = "결제 기록 ID")
        private Long id;

        @ApiModelProperty(value = "지점 ID")
        private Long branchId;

        @ApiModelProperty(value = "티켓 ID")
        private Long ticketId;

        @ApiModelProperty(value = "부가상품 구매 ID")
        private Long extraProductPurchaseId;

        @ApiModelProperty(value = "결제일")
        private LocalDate paidDate;

        @ApiModelProperty(value = "결제 수단")
        private PaymentHistoryPaidMethodTypeEnum paidMethodType;

        @ApiModelProperty(value = "결제 타입 코드")
        private PaymentHistoryPaidTypeEnum paidType;

        @ApiModelProperty(value = "결제 금액")
        private Integer paidPrice;

        @ApiModelProperty(value = "포스 연동 여부")
        private Boolean possYn;

        public static ResponseTuteePaymentHistoryDto toDto(TuteePaymentHistory tuteePaymentHistory){
            return ResponseTuteePaymentHistoryDto.builder()
                    .id(tuteePaymentHistory.getId())
                    .branchId(tuteePaymentHistory.getBranch().getId())
                    .ticketId(tuteePaymentHistory.getTicketId())
                    .extraProductPurchaseId(tuteePaymentHistory.getExtraProductPurchaseId())
                    .paidDate(tuteePaymentHistory.getPaidDate())
                    .paidMethodType(tuteePaymentHistory.getPaidMethodType())
                    .paidType(tuteePaymentHistory.getPaidType())
                    .paidPrice(tuteePaymentHistory.getPaidPrice())
                    .possYn(tuteePaymentHistory.getPossYn())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ResponseTuteePaymentHistoryListDto{
        @ApiModelProperty(value = "수강권 결제 내역 리스트")
        private List<TuteePaymentHistoryDto.ResponseTuteePaymentHistoryDto> results;

        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static TuteePaymentHistoryDto.ResponseTuteePaymentHistoryListDto build(Page<TuteePaymentHistoryDto.ResponseTuteePaymentHistoryDto> responseDtoPage, Integer currentPage, Integer maxResults){
            return TuteePaymentHistoryDto.ResponseTuteePaymentHistoryListDto.builder()
                    .results(responseDtoPage.getContent())
                    .maxResults(maxResults)
                    .currentPage(currentPage)
                    .totalPages(responseDtoPage.getTotalPages())
                    .totalElements(responseDtoPage.getTotalElements())
                    .build();
        }
    }
}
