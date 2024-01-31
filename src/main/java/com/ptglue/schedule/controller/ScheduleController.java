package com.ptglue.schedule.controller;

import com.ptglue.basic.model.dto.ApiResponseDto;
import com.ptglue.schedule.model.dto.*;
import com.ptglue.schedule.service.RepeatScheduleService;
import com.ptglue.schedule.service.ReservationService;
import com.ptglue.schedule.service.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Api(tags = {"60. Schedule"})
@RestController
@RequestMapping(path = {"manager/schedule", "tutor/schedule"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class ScheduleController {
    @Resource(name = "scheduleService")
    ScheduleService scheduleService;

    @Resource(name = "reservationService")
    ReservationService reservationService;

    @ApiOperation(value = "월간/주간/일간 일정 목록 조회", notes = "일정 목록을 조회합니다.")
    @GetMapping("")
    public List<ScheduleDto.ResponseScheduleListDto> getList(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "시작일자", required = true, defaultValue = "2023-11-01") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate startDate,
            @ApiParam(value = "종료일자", required = true, defaultValue = "2023-11-30") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate endDate,
            @ApiParam(value = "담당 강사 id", required = false, defaultValue = "263") @RequestParam(required = false) Long mainTutorId) {

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        ZonedDateTime startDt = startDate.atTime(0,0,0).atZone(ZoneId.systemDefault());
        ZonedDateTime endDt = endDate.plusDays(1).atTime(0,0,0).atZone(ZoneId.systemDefault());
        List<ScheduleDto.ResponseScheduleListDto> responseScheduleDtoList = new ArrayList<>();

        if (mainTutorId == null || mainTutorId == 0L){
            responseScheduleDtoList = scheduleService.getList(branchId, startDt, endDt);
        }else {
            responseScheduleDtoList = scheduleService.getListByMainTutorId(branchId, mainTutorId, startDt, endDt);
        }

        return responseScheduleDtoList;
    }

    @ApiOperation(value = "일정 상세 조회 - 기본 정보", notes = "선택한 일정의 기본 정보를 조회합니다.")
    @GetMapping("{scheduleId}")
    public ScheduleDto.ResponseScheduleDto get(
            @ApiParam(value = "schedule id", required = true, defaultValue = "1") @PathVariable Long scheduleId) {

        return scheduleService.get(scheduleId);
    }

    @ApiOperation(value = "일정 상세 조회 - 반복 일정 ", notes = "선택한 일정의 반복 정보를 조회합니다.")
    @GetMapping("{scheduleId}/repeat-schedule")
    public RepeatScheduleDto.ResponseRepeatScheduleDto getRepeatSchedule(
            @ApiParam(value = "schedule id", required = true, defaultValue = "1") @PathVariable Long scheduleId) {

        return scheduleService.getRepeatSchedule(scheduleId);
    }

    @ApiOperation(value = "일정 상세 조회 - 일정 예약한 회원 목록", notes = "선택한 일정의 예약한 회원 목록을 조회합니다.")
    @GetMapping("{scheduleId}/reservation")
    public ReservationDto.ResponseReservationTuteeListDto getReservationDetailList(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "schedule id", required = true, defaultValue = "1") @PathVariable Long scheduleId,
            @ApiParam(value = "페이지 번호", required = true, defaultValue = "0") Integer page,
            @ApiParam(value = "페이지당 항목 수", required = true, defaultValue = "10") Integer maxResults) {

        Pageable pageable = PageRequest.of(page, maxResults);

        Page<ReservationDto.ResponseReservationTuteeDto> responseReservationTuteePage = reservationService.getReservationListByScheduleId(scheduleId, pageable);
        return ReservationDto.ResponseReservationTuteeListDto.build(responseReservationTuteePage, page, maxResults);
    }

    @ApiOperation(value = "일정 상세 - 회원 예약/출석 상태 변경")
    @PatchMapping("{scheduleId}/reservation")
    public ResponseEntity<Void> updateReservationAttendanceStatus(
            @ApiParam(value = "일정 id", required = true, defaultValue = "1") @PathVariable Long scheduleId,
            @Valid @RequestBody ReservationDto.RequestUpdateReservationAttendanceStatusDto requestReservationAttendanceDto){

        reservationService.updateReservationStatus(scheduleId, requestReservationAttendanceDto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "일정 추가 - 겹치는 시간 확인")
    @GetMapping("duplicate-check")
    public List<ScheduleDto.ResponseScheduleDto> checkDuplicateSchedule(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "시작일자", required = true, defaultValue = "1698786000000") @RequestParam Long startDt,
            @ApiParam(value = "종료일자", required = true, defaultValue = "1698793200000") @RequestParam Long endDt){

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");

        return scheduleService.getDuplicateCheck(branchId ,startDt, endDt);
    }

    @ApiOperation(value = "일정 등록", notes = "일정을 등록합니다.")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ScheduleDto.ResponseScheduleDto> create(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "일정 정보", required = true) @RequestBody @Valid ScheduleDto.RequestScheduleDto requestScheduleDto) {

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        ScheduleDto.ResponseScheduleDto scheduleInfo = scheduleService.createSchedule(branchId, requestScheduleDto);
        return ApiResponseDto.createdResponseEntity(scheduleInfo.getScheduleId(), scheduleInfo);
    }

    @ApiOperation(value = "일정 취소", notes = "일정을 취소 처리 합니다.")
    @PatchMapping("{scheduleId}/cancel")
    public ScheduleDto.ResponseScheduleDto cancel(
            @ApiParam(value = "일정 id", required = true, defaultValue = "1") @PathVariable Long scheduleId
    ) {
        return scheduleService.cancel(scheduleId);
    }

    @ApiOperation(value = "일정 수정", notes = "일정을 수정 합니다.")
    @PatchMapping("{scheduleId}")
    public ScheduleDto.ResponseScheduleDto update(
            @ApiParam(value = "일정 id", required = true, defaultValue = "1") @PathVariable Long scheduleId,
            @RequestBody @Valid ScheduleDto.RequestScheduleDto requestScheduleDto
    ) {
        return scheduleService.update(scheduleId, requestScheduleDto);
    }

    @ApiOperation(value = "휴무 일정 등록", notes = "휴무 일정을 등록합니다.")
    @PostMapping("off")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ScheduleDto.ResponseScheduleDto> createOffSchedule(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "일정 정보", required = true) @RequestBody @Valid ScheduleDto.RequestOffScheduleDto requestOffScheduleDto) {

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        ScheduleDto.ResponseScheduleDto scheduleInfo;
        //반복일정이 없는 경우
        if (requestOffScheduleDto.getRepeatScheduleId() == null ) {
            scheduleInfo = scheduleService.createOffSchedule(branchId, requestOffScheduleDto);
        //반복일정이 있는 경우
        } else {
            scheduleInfo = scheduleService.createRepeatOffSchedule(branchId, requestOffScheduleDto);
        }
        String url = ServletUriComponentsBuilder.fromCurrentRequest().toUriString().replace("off", scheduleInfo.getScheduleId().toString());
        return ApiResponseDto.createdResponseEntity(url, scheduleInfo);
    }
}

