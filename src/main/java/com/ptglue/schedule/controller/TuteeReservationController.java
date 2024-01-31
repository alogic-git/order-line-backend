package com.ptglue.schedule.controller;

import com.ptglue.schedule.model.dto.ReservationMockupDto;
import com.ptglue.schedule.model.dto.ScheduleMockupDto;
import com.ptglue.schedule.model.dto.ReservationTuteeDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = {"68. Tutee Reservation"})
@RestController
@RequestMapping(path = {"tutee/reservation"},produces = MediaType.APPLICATION_JSON_VALUE)
public class TuteeReservationController {
    @ApiOperation(value = "홈 화면 예약 목록", notes ="홈 화면에서 내가 예약한 강의 목록")
    @GetMapping("")
    public ReservationTuteeDto.ResponseReservationTuteeListDto getList(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "페이지 번호", required = true, defaultValue = "1") Integer page,
            @ApiParam(value = "페이지당 항목 수", required = true, defaultValue = "10") Integer maxResults
    ){
        return ScheduleMockupDto.getScheduleReservationTuteeListMockup();
    }

    @ApiOperation(value = "예약 상세 조회", notes = "사용자가 예약을 상세 확인 했을때")
    @GetMapping("{reservationId}")
    public ReservationTuteeDto.ResponseReservationDto getReservation(
            @ApiParam(value = "예약 ID",required = true, defaultValue = "1")@PathVariable Integer reservationId
    ){
        return ReservationMockupDto.getReseponseReservedDetailMockup();
    }

    @ApiOperation(value = "예약 추가", notes = "사용자가 예약을 추가 했을때")
    @PostMapping("")
    public ReservationTuteeDto.ResponseReservationDto createReservation (
            HttpServletRequest httpServletRequest,
            ReservationTuteeDto.RequestReservationTuteeDto requestReservationDto
    ) {
//        klass table의 reservation_auto_accept_yn의 값을보고 statusType의 값이 대기,승낙,확정으로 나뉘어진다.
//        schedule table -  current_tutee_num +=1
//        ticket table - available_reservation_count -=1 , remain_reservation_count -=1, total_reservation_count +=1,
//        reservation 추가
//        response memberType은 INDIVIDUAL(사전개설형),GROUP(사전개설형),FREE(자유예약형) 3가지
        return ReservationMockupDto.getReseponseReservedDetailMockup();
    }
}
