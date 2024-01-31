package com.ptglue.ticket.model.dto;

import com.ptglue.product.model.entity.Product;
import com.ptglue.ticket.enums.TicketPauseReasonTypeEnum;
import com.ptglue.ticket.model.entity.Ticket;
import io.swagger.annotations.ApiModelProperty;
import com.ptglue.branch.model.entity.Branch;
import com.ptglue.ticket.model.entity.TicketPauseHistory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TicketPauseHistoryDto {
    @Getter
    @Builder
    public static class ResponseTicketPauseHistoryDto{

        @ApiModelProperty(value = "일시정지 티켓 ID")
        private Long ticketPauseHistoryId;

        @ApiModelProperty(value = "지점 ID")
        private Long branchId;

        @ApiModelProperty(value = "상품 ID")
        private Long productId;

        @ApiModelProperty(value = "티켓 ID")
        private Long ticketId;

        @ApiModelProperty(value = "시작 일자")
        private LocalDate startDate;

        @ApiModelProperty(value = "종료 일자")
        private LocalDate endDate;

        @ApiModelProperty(value = "일시정지 사유")
        private TicketPauseReasonTypeEnum reasonType;

        @ApiModelProperty(value = "수강권 연장 여부")
        private Boolean extensionYn;

        public static ResponseTicketPauseHistoryDto toDto(final TicketPauseHistory ticketPauseHistory){
            return ResponseTicketPauseHistoryDto.builder()
                    .ticketPauseHistoryId(ticketPauseHistory.getId())
                    .branchId(ticketPauseHistory.getBranch().getId())
                    .productId(ticketPauseHistory.getProduct().getId())
                    .ticketId(ticketPauseHistory.getTicket().getId())
                    .startDate(ticketPauseHistory.getStartDate())
                    .endDate(ticketPauseHistory.getEndDate())
                    .reasonType(ticketPauseHistory.getReasonType())
                    .extensionYn(ticketPauseHistory.getExtensionYn())
                    .build();
        }
    }

    @Builder
    @Getter
    public static class ResponseTicketPauseHistoryListDto {

        @ApiModelProperty(value = "일시정지 티켓 리스트")
        private List<TicketPauseHistoryDto.ResponseTicketPauseHistoryDto> results;

        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지 당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static TicketPauseHistoryDto.ResponseTicketPauseHistoryListDto build(Page<TicketPauseHistoryDto.ResponseTicketPauseHistoryDto> responseDtoPage, Integer currentPage, Integer maxResults){
            return TicketPauseHistoryDto.ResponseTicketPauseHistoryListDto.builder()
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
    public static class RequestTicketPauseHistoryDto{

        @ApiModelProperty(value = "상품 ID")
        private Long productId;

        @ApiModelProperty(value = "티켓 ID")
        private Long ticketId;

        @ApiModelProperty(value = "시작 일자")
        private LocalDate startDate;

        @ApiModelProperty(value = "종료 일자")
        private LocalDate endDate;

        @ApiModelProperty(value = "일시정지 사유")
        private TicketPauseReasonTypeEnum reasonType;

        public TicketPauseHistory toEntity(Branch branch, Product product, Ticket ticket) {
            return TicketPauseHistory.builder()
                    .branch(branch)
                    .product(product)
                    .ticket(ticket)
                    .startDate(startDate)
                    .endDate(endDate)
                    .reasonType(reasonType)
                    .build();
        }
    }
}
