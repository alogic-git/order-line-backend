package com.orderline.schedule.controller;

import com.orderline.basic.model.dto.ApiResponseDto;
import com.orderline.schedule.model.dto.ReservationDto;
import com.orderline.schedule.service.ReservationService;
import com.orderline.ticket.model.dto.TicketDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Api(tags = {"61. Reservation"})
@RestController
@RequestMapping(path = {"manager/reservation", "tutor/reservation"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class ReservationController {
    @Resource(name = "reservationService")
    ReservationService reservationService;

    @ApiOperation(value = "예약 상세 조회", notes = "선택한 예약을 상세 조회 합니다.")
    @GetMapping("{reservationId}")
    public ReservationDto.ResponseReservationDto get(
            @ApiParam(value = "reservationId", required = true, defaultValue = "1") @PathVariable Long reservationId
    ){

        return reservationService.get(reservationId);
    }

    @ApiOperation(value = "예약 목록 조회", notes = "예약 목록을 조회 합니다.")
    @GetMapping("")
    public ReservationDto.ResponseReservationListDto get(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "페이지 번호", required = true, defaultValue ="0") Integer page,
            @ApiParam(value = "페이지당 항목 수", required = true, defaultValue ="0") Integer maxResults
    ){
        Long userId = (Long) httpServletRequest.getAttribute("userId");
        Pageable pageable = PageRequest.of(page, maxResults);

        Page<ReservationDto.ResponseReservationDto> reservationList = reservationService.getList(userId, pageable);

        return ReservationDto.ResponseReservationListDto.build(reservationList, page, maxResults);
    }

    @ApiOperation(value = "예약 추가", notes = "예약을 새로 추가 합니다.")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReservationDto.ResponseReservationDto> create(
            HttpServletRequest httpServletRequest,
            @RequestBody @Valid ReservationDto.RequestReservationDto requestReservationDto
    ){
        Long branchId = (Long) httpServletRequest.getAttribute("branchId");

        ReservationDto.ResponseReservationDto reservationInfo =  reservationService.create(branchId, requestReservationDto);
        return ApiResponseDto.createdResponseEntity(reservationInfo.getReservationId(), reservationInfo);
    }

    @ApiOperation(value = "예약 취소", notes = "예약을 취소 처리 합니다.")
    @PatchMapping("{reservationId}/cancel")
    public ReservationDto.ResponseReservationDto cancel(
            @ApiParam(value = "일정 id", required = true, defaultValue = "1") @PathVariable Long reservationId
    ){
        return reservationService.cancel(reservationId);
    }

    @ApiOperation(value = "예약 수정", notes = "예약을 수정 합니다.")
    @PatchMapping("{reservationId}")
    public ReservationDto.ResponseReservationDto update(
            @ApiParam(value = "예약 id", required = true, defaultValue = "1") @PathVariable Long reservationId,
            @RequestBody @Valid ReservationDto.RequestReservationDto requestReservationDto
    ){
        return reservationService.update(reservationId, requestReservationDto);
    }

    @ApiOperation(value = "실시간 출석 체크 가능한 수업(예약내역) 조회")
    @GetMapping("attendance-enable")
    public List<ReservationDto.ResponseAttendanceReservationDto> getAttendanceReservationList(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "회원 전화 번호", required = true, defaultValue = "01012345678") @RequestParam String phoneNumber){

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        return reservationService.getAttendanceReservationList(branchId, phoneNumber);
    }

    @ApiOperation(value = "실시간 출석 체크 선택한 수업 수강권 조회")
    @GetMapping("{reservationId}/attendance-enable/ticket")
    public TicketDto.ResponseTicketDto getAttendanceReservationTicket(
            @ApiParam(value = "예약 id", required = true, defaultValue = "1") @PathVariable Long reservationId){

        return reservationService.getAttendanceReservationTicket(reservationId);
    }

    @ApiOperation(value = "실시간 출석 체크")
    @PatchMapping("{reservationId}/attendance")
    @ResponseStatus(HttpStatus.OK)
    public ReservationDto.ResponseAttendanceReservationDto updateAttendanceReservation(
            @ApiParam(value = "예약 id", required = true, defaultValue = "1") @PathVariable Long reservationId){

        return reservationService.updateAttendanceReservation(reservationId);
    }
}
