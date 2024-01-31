package com.orderline.payment.controller;

import com.orderline.ticket.model.dto.TicketTuteeDto;
import com.orderline.payment.model.dto.TuteePaymentHistoryDto;
import com.orderline.payment.service.TuteePaymentHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Api(tags = {"109.Tutee Payment History"})
@RestController
@RequestMapping(path = "tutee/ticket", produces = MediaType.APPLICATION_JSON_VALUE)
public class TuteeTicketPaymentHistoryController {
    @Resource(name = "tuteePaymentHistoryService")
    TuteePaymentHistoryService tuteePaymentHistoryService;

    @ApiOperation(value = "수강권 결제 내역",notes ="수강권 결제 내역")
    @GetMapping("tutee-payment-history")
    public TuteePaymentHistoryDto.ResponseTuteePaymentHistoryListDto getPaymentList(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "페이지 번호", required = true, defaultValue = "0") Integer page,
            @ApiParam(value = "페이지당 항목 수", required = true, defaultValue = "10") Integer maxResults
    ){
        Long userId = (Long) httpServletRequest.getAttribute("userId");
        Pageable pageable = PageRequest.of(page, maxResults);

        Page<TuteePaymentHistoryDto.ResponseTuteePaymentHistoryDto> tuteePaymentList = tuteePaymentHistoryService.getPaymentList(userId, pageable);

        return TuteePaymentHistoryDto.ResponseTuteePaymentHistoryListDto.build(tuteePaymentList, page, maxResults);
    }

    @ApiOperation(value = "수상권 결제 내역 상세", notes = "수상권 결제 내역 상세 조회")
    @GetMapping("{ticketId}/tutee-payment-history")
    public TicketTuteeDto.ResponseTicketPaymentDto getPayment(
            @ApiParam(value = "수강권 id", required = true, defaultValue = "6")@PathVariable Long ticketId
    ){
        return tuteePaymentHistoryService.getPaymentByTicketId(ticketId);
    }

    @ApiOperation(value = "수강권 결제의 상세 내역",notes ="결재내역의 상세 정보 조회")
    @GetMapping("{ticketId}/tutee-payment-history/{tuteePaymentHistoryId}")
    public TuteePaymentHistoryDto.ResponseTuteePaymentHistoryDto getPaymentByTicketId(
            @ApiParam(value = "수강권 id", required = true, defaultValue = "6")@PathVariable Long ticketId,
            @ApiParam(value = "수강권 결제 내역 id", required = true, defaultValue = "1")@PathVariable Long tuteePaymentHistoryId
    ){
        return tuteePaymentHistoryService.getPayment(tuteePaymentHistoryId);
    }
}
