package com.orderline.ticket.model.dto;

import com.orderline.ticket.enums.TicketPauseReasonTypeEnum;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class TicketPauseHistoryMockupDto {
    public static TicketPauseHistoryDto.ResponseTicketPauseHistoryDto getMockup1(){
        return TicketPauseHistoryDto.ResponseTicketPauseHistoryDto.builder()
                .ticketPauseHistoryId(1L)
                .branchId(1L)
                .productId(1L)
                .ticketId(1L)
                .startDate(LocalDate.of(2023,1,15))
                .endDate(LocalDate.of(2023,1,17))
                .reasonType(TicketPauseReasonTypeEnum.TUTEE_REQUEST)
                .build();
    }

    public static TicketPauseHistoryDto.ResponseTicketPauseHistoryDto getMockup2(){
        return TicketPauseHistoryDto.ResponseTicketPauseHistoryDto.builder()
                .ticketPauseHistoryId(2L)
                .branchId(1L)
                .productId(1L)
                .ticketId(1L)
                .startDate(LocalDate.of(2023,5,30))
                .endDate(LocalDate.of(2023,6,12))
                .reasonType(TicketPauseReasonTypeEnum.BRANCH_REQUEST)
                .build();
    }

    public static TicketPauseHistoryDto.ResponseTicketPauseHistoryDto getMockup3(){
        return TicketPauseHistoryDto.ResponseTicketPauseHistoryDto.builder()
                .ticketPauseHistoryId(3L)
                .branchId(1L)
                .productId(1L)
                .ticketId(1L)
                .startDate(LocalDate.of(2023,6,22))
                .endDate(LocalDate.of(2023,8,25))
                .reasonType(TicketPauseReasonTypeEnum.TUTEE_REQUEST)
                .build();
    }

    public static TicketPauseHistoryDto.ResponseTicketPauseHistoryDto getMockup4(){
        return TicketPauseHistoryDto.ResponseTicketPauseHistoryDto.builder()
                .ticketPauseHistoryId(4L)
                .branchId(1L)
                .productId(1L)
                .ticketId(1L)
                .startDate(LocalDate.of(2023,9,1))
                .endDate(LocalDate.of(2023,11,30))
                .reasonType(TicketPauseReasonTypeEnum.BRANCH_REQUEST)
                .build();
    }

    public static TicketPauseHistoryDto.ResponseTicketPauseHistoryListDto getListMockup(){
        List<TicketPauseHistoryDto.ResponseTicketPauseHistoryDto> pauseHistoryMockupList = Arrays.asList(
                getMockup1(),
                getMockup2(),
                getMockup3(),
                getMockup4()
        );


        return TicketPauseHistoryDto.ResponseTicketPauseHistoryListDto.builder()
                .results(pauseHistoryMockupList)
                .maxResults(4)
                .currentPage(1)
                .totalPages(1)
                .totalElements(4L)
                .build();
    }
}
