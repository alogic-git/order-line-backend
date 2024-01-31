package com.orderline.ticket.model.dto;

import java.time.LocalDate;

public class TicketMockupDto {

    public static TicketDto.ResponseTicketDto getTicketMockup() {
        return TicketDto.ResponseTicketDto.builder()
                .ticketId(1L)
                .branchId(1L)
                .ticketName("티켓1")
                .price(10000)
                .totalReservationCount(20)
                .remainReservationCount(10)
                .availableReservationCount(10)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(30))
                .build();
    }

    public static TicketDto.ResponseTicketDto getTicketMockup2() {
        return TicketDto.ResponseTicketDto.builder()
                .ticketId(2L)
                .branchId(1L)
                .ticketName("티켓2")
                .price(20000)
                .totalReservationCount(20)
                .remainReservationCount(10)
                .availableReservationCount(10)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(10))
                .build();
    }


}
