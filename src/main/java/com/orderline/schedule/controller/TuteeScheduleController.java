package com.orderline.schedule.controller;

import com.orderline.basic.exception.NotFoundException;
import com.orderline.schedule.enums.ScheduleMemberTypeEnum;
import com.orderline.schedule.model.dto.*;
import com.orderline.schedule.service.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

@Api(tags = {"60. Schedule"})
@RestController
@RequestMapping(path = {"tutee/schedule" }, produces = MediaType.APPLICATION_JSON_VALUE)
public class TuteeScheduleController {
    @Resource(name = "scheduleService")
    ScheduleService scheduleService;

    @ApiOperation(value="자유 일정 가져오기", notes = "시작 ~ 종료일 사이의 자유 예약형 일정을 가져옵니다.")
    @GetMapping("free-choice")
    public List<ScheduleDto.ResponseScheduleDto> getFreeChoiceSchedules(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "시작 일자", required = true, defaultValue ="2023-12-10")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @ApiParam(value = "종료 일자", required = true, defaultValue ="2023-12-15")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ){
        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        return scheduleService.getFreeChoiceScheduleList(startDate, endDate, branchId);
    }

    @ApiOperation(value="자유 일정 가져오기 - klass 상세", notes = "시작 ~ 종료일 사이의 자유 예약형 일정을 가져옵니다.")
    @GetMapping("free-choice/{klassId}")
    public List<ScheduleDto.ResponseScheduleDto> getFreeChoiceSchedules(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "시작 일자", required = true, defaultValue ="2023-12-10")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @ApiParam(value = "종료 일자", required = true, defaultValue ="2023-12-15")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @ApiParam(value = "klassId", required = true, defaultValue ="1") @PathVariable Long klassId
    ){
        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        return scheduleService.getFreeChoiceSchedule(startDate, endDate, branchId, klassId);
    }

    @ApiOperation(value = "예약 하기 위한 일정 목록", notes = "예약 화면에서 사전개설형 일정내역 목록")
    @GetMapping("limited")
    public ScheduleTuteeDto.ResponseScheduleTuteeListDto getLimitList (
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "클래스 종류(ALL, RESERVED, INDIVIDUAL, GROUP)", required = true, defaultValue ="ALL") @RequestParam(required = true, defaultValue ="ALL") ScheduleMemberTypeEnum scheduleMemberTypeEnum,
            @ApiParam(value = "페이지 번호", required = true, defaultValue = "1") Integer page,
            @ApiParam(value = "페이지당 항목 수", required = true, defaultValue = "10") Integer maxResults
    ) {
//      ALL,INDIVIDUAL,GROUP 같은 서비스를 이용하고, RESERVED는 따로 분류한다.
//      memberType의 Free 값은 자유 예약형을 위해 만들어 두어서 사용하지 않는다.
        switch (scheduleMemberTypeEnum) {
            case ALL :
//                소유한 ticket이 가지고 있는 모든 schedule 검색 중, max_tutee_num >=0
                return ScheduleMockupDto.getScheduleTuteeListMockup();
            case RESERVED :
//                tutee가 예약한 schedule검색
                return ScheduleMockupDto.getScheduleTuteeListMockup();
            case INDIVIDUAL :
//                소유한 ticket이 가지고 있는 모든 schedule 검색 중, max_tutee_num = 1
                return ScheduleMockupDto.getScheduleTuteeListMockup();
            case GROUP :
//                소유한 ticket이 가지고 있는 모든 schedule 검색 중, max_tutee_num > 1
                return ScheduleMockupDto.getScheduleTuteeListMockup();
        }

        throw new NotFoundException("정확한 타입을 입력해주세요");
    }

    @ApiOperation(value = "일정에서 예약할 강의 클릭시 보여질 상세 내역", notes = "일정에서 예약할 강의 클릭시 보여질 상세 내역")
    @GetMapping("{scheduleId}")
    public ScheduleTuteeDto.ResponseReservationDto getSchedule(
            @ApiParam(value = "일정 ID",required = true, defaultValue = "1")@PathVariable Integer scheduleId
    ){
        return ScheduleMockupDto.getReseponseReservedDetailMockup();
    }
}

