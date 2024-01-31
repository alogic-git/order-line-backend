package com.ptglue.schedule.controller;

import com.ptglue.basic.model.dto.ApiResponseDto;
import com.ptglue.schedule.model.dto.RepeatScheduleDto;
import com.ptglue.schedule.service.RepeatScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Api(tags = {"62. RepeatSchedule"})
@RestController
@RequestMapping(path = {"manager/schedule/repeat-schedule", "tutor/schedule/repeat-schedule"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class RepeatScheduleController {
    @Resource(name = "repeatScheduleService")
    RepeatScheduleService repeatScheduleService;

    @ApiOperation(value = "반복 일정 상세 조회", notes = "선택한 반복 일정을 상세 조회 합니다.")
    @GetMapping("{repeatScheduleId}")
    public RepeatScheduleDto.ResponseRepeatScheduleDto get(
            @ApiParam(value = "repeatScheduleId", required = true, defaultValue = "1") @PathVariable Long repeatScheduleId
    ){
        return repeatScheduleService.get(repeatScheduleId);
    }

    @ApiOperation(value = "반복 일정 추가", notes = "반복 일정을 새로 추가 합니다.")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RepeatScheduleDto.ResponseRepeatScheduleDto> create(
            HttpServletRequest httpServletRequest,
            @RequestBody @Valid RepeatScheduleDto.RequestRepeatScheduleDto requestRepeatScheduleDto
    ){
        Long branchId = (Long) httpServletRequest.getAttribute("branchId");

        RepeatScheduleDto.ResponseRepeatScheduleDto repeatScheduleInfo =  repeatScheduleService.create(branchId, requestRepeatScheduleDto);

        return ApiResponseDto.createdResponseEntity(repeatScheduleInfo.getRepeatScheduleId(), repeatScheduleInfo);
    }

    @ApiOperation(value = "반복 일정 보관함 이동", notes = "반복 일정을 보관함으로 이동 합니다.")
    @PatchMapping("{repeatScheduleId}/archive")
    public RepeatScheduleDto.ResponseRepeatScheduleDto archive(
            @ApiParam(value = "일정 id", required = true, defaultValue = "1") @PathVariable Long repeatScheduleId
    ){
        return repeatScheduleService.archive(repeatScheduleId);
    }
}
