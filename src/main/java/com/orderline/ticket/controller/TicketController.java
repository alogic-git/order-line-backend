package com.orderline.ticket.controller;

import com.orderline.ticket.service.TicketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.orderline.ticket.model.dto.TicketDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = {"40.Ticket"})
@RestController
@RequestMapping(path = {"tutor/ticket", "manager/ticket"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class TicketController {
    @Resource(name = "ticketService")
    TicketService ticketService;

    @ApiOperation(value = "ticket 상세 조회", notes = "선택한 티켓을 상세 조회합니다")
    @GetMapping("ticket/{ticketId}")
    public TicketDto.ResponseTicketDto getTicket(
            @ApiParam(value = "티켓 id", required = true, defaultValue = "1")@PathVariable Long ticketId) {
        return ticketService.get(ticketId);
    }
}
