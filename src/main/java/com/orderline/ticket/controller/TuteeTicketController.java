package com.orderline.ticket.controller;

import com.orderline.klass.model.dto.KlassTuteeDto;
import com.orderline.klass.service.KlassTuteeService;
import com.orderline.schedule.model.dto.ScheduleTuteeDto;
import com.orderline.schedule.service.ReservationTuteeService;
import com.orderline.ticket.enums.TicketStatusTypeEnum;
import com.orderline.ticket.model.dto.TicketPauseHistoryDto;
import com.orderline.ticket.service.TicketPauseHistoryService;
import com.orderline.ticket.service.TicketTuteeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.orderline.ticket.model.dto.TicketTuteeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Api(tags = {"49.Ticket Tutee"})
@RestController
@RequestMapping(path = "tutee/ticket", produces = MediaType.APPLICATION_JSON_VALUE)
public class TuteeTicketController {
    @Resource(name = "ticketTuteeService")
    TicketTuteeService ticketTuteeService;

    @Resource(name = "klassTuteeService")
    KlassTuteeService klassTuteeService;

    @Resource(name = "reservationTuteeService")
    ReservationTuteeService reservationTuteeService;

    @Resource(name = "ticketPauseHistoryService")
    TicketPauseHistoryService ticketPauseHistoryService;

    @ApiOperation(value = "수강권 상세 내역", notes = "수강권 상세 내역 조회")
    @GetMapping("{ticketId}")
    public TicketTuteeDto.ResponseTicketTuteeDetailDto get(
        @ApiParam(value = "수강권 id", required = true, defaultValue = "1")@PathVariable Long ticketId
    ){
        return ticketTuteeService.get(ticketId);
    }

    @ApiOperation(value = "수강권 내역", notes = "수강권 내역 목록")
    @GetMapping("")
    public TicketTuteeDto.ResponseTicketTuteeListDto getList(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "수강권 상태", required = true, defaultValue = "ALL") TicketStatusTypeEnum statusType,
            @ApiParam(value = "페이지 번호", required = true, defaultValue = "0") Integer page,
            @ApiParam(value = "페이지당 항목 수", required = true, defaultValue = "10") Integer maxResults
    ){
        Long userId = (Long) httpServletRequest.getAttribute("userId");
        Pageable pageable = PageRequest.of(page, maxResults);

        Page<TicketTuteeDto.ResponseTicketTuteeDto> ticketTuteeList = ticketTuteeService.getList(userId, statusType, pageable);
        return TicketTuteeDto.ResponseTicketTuteeListDto.build(ticketTuteeList, page, maxResults);
    }

    @ApiOperation(value = "참여 가능한 수업 목록", notes = "수강권 상세 화면에서 참여 가능한 수업 확인")
    @GetMapping("{ticketId}/available-klasses")
    public KlassTuteeDto.ResponseTuteeKlassByTicketIdListDto getKlassListByTicketId(
            @ApiParam(value = "ticketId", required = true, defaultValue = "6") @PathVariable Long ticketId,
            @ApiParam(value = "페이지 번호", required = true, defaultValue = "0") Integer page,
            @ApiParam(value = "페이지당 항목 수", required = true, defaultValue = "10") Integer maxResults
    ){
        Pageable pageable = PageRequest.of(page, maxResults);

        Page<KlassTuteeDto.ResponseTuteeKlassByTicketIdDto> klassList =  klassTuteeService.getKlassList(ticketId, pageable);
        return KlassTuteeDto.ResponseTuteeKlassByTicketIdListDto.build(klassList, page, maxResults);
    }

    @ApiOperation(value = "수강권에서 결석 수 확인", notes = "수강권 상세에서 기본정보에서 결석수 확인")
    @GetMapping("{ticketId}/absence/count")
    public ScheduleTuteeDto.ResponseTuteeAbsenceReservationCount  getAbsenceReservationCount (
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "ticketId", required = true, defaultValue = "6") @PathVariable Long ticketId
    ) {
        Long userId = (Long) httpServletRequest.getAttribute("userId");
        Integer absenceCount = reservationTuteeService.getReservationAbsenceCount(ticketId, userId);

        return ScheduleTuteeDto.ResponseTuteeAbsenceReservationCount.toDto(absenceCount);
    }

    @ApiOperation(value = "수강권에서 일정 내역 목록", notes = "수강권 상세에서 일정내역 목록 확인")
    @GetMapping("{ticketId}/reservation")
    public ScheduleTuteeDto.ResponseReservationTuteeListByTicketIdDto getReservationHistory (
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "ticketId", required = true, defaultValue = "6") @PathVariable Long ticketId,
            @ApiParam(value = "페이지 번호", required = true, defaultValue = "0") Integer page,
            @ApiParam(value = "페이지당 항목 수", required = true, defaultValue = "10") Integer maxResults
    ) {
        Long userId = (Long) httpServletRequest.getAttribute("userId");
        Pageable pageable = PageRequest.of(page, maxResults);

        Page<ScheduleTuteeDto.ResponseReservationTuteeByTicketIdDto> attendanceStampPage = reservationTuteeService.getAttendanceStamp(userId, ticketId, pageable);

        return ScheduleTuteeDto.ResponseReservationTuteeListByTicketIdDto.build(attendanceStampPage, page, maxResults);
    }

    @ApiOperation(value = "수강권 일시정지 내역", notes = "수강권 상세에서 일시정지 목록 확인")
    @GetMapping("{ticketId}/pause")
    public TicketPauseHistoryDto.ResponseTicketPauseHistoryListDto getTicketPauseHistory(
        @ApiParam(value = "ticketId", required = true, defaultValue = "6") @PathVariable Long ticketId,
        @ApiParam(value = "페이지 번호", required = true, defaultValue = "0") Integer page,
        @ApiParam(value = "페이지당 항목 수", required = true, defaultValue = "10") Integer maxResults
    ){
        Pageable pageable = PageRequest.of(page, maxResults);

        Page<TicketPauseHistoryDto.ResponseTicketPauseHistoryDto> pauseHistoryPage = ticketPauseHistoryService.getPauseList(ticketId, pageable);

        return TicketPauseHistoryDto.ResponseTicketPauseHistoryListDto.build(pauseHistoryPage, page, maxResults);
    }
}
