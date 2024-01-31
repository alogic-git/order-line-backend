package com.orderline.schedule.controller;

import com.orderline.basic.model.dto.ApiResponseDto;
import com.orderline.schedule.model.dto.DailyRecordDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.orderline.schedule.service.DailyRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Api(tags = {"64. DailyRecord"})
@RestController
@RequestMapping(path = {"daily-record"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class DailyRecordController {
    @Resource(name = "dailyRecordService")
    DailyRecordService dailyRecordService;

    @ApiOperation(value = "일일 기록 상세 조회", notes = "선택한 일일 기록을 상세 조회 합니다.")
    @GetMapping("{dailyRecordId}")
    public DailyRecordDto.ResponseDailyRecordDto get(
            @ApiParam(value = "dailyRecordId", required = true, defaultValue = "1") @PathVariable Long dailyRecordId
    ){

        return dailyRecordService.get(dailyRecordId);
    }

    @ApiOperation(value = "일일 기록 추가", notes = "일일 기록을 새로 추가 합니다.")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DailyRecordDto.ResponseDailyRecordDto> create(
            HttpServletRequest httpServletRequest,
            @RequestBody @Valid DailyRecordDto.RequestDailyRecordDto requestDailyRecord
    ){
        Long branchId = (Long) httpServletRequest.getAttribute("branchId");

        DailyRecordDto.ResponseDailyRecordDto dailyRecordInfo =  dailyRecordService.create(branchId, requestDailyRecord);

        return ApiResponseDto.createdResponseEntity(dailyRecordInfo.getId(), dailyRecordInfo);
    }

    @ApiOperation(value = "일일 기록 수정")
    @PatchMapping("{dailyRecordId}")
    public DailyRecordDto.ResponseDailyRecordDto update(
            @ApiParam(value = "일일 기록 id", required = true, defaultValue = "1") @PathVariable Long dailyRecordId,
            @RequestBody @Valid DailyRecordDto.RequestDailyRecordDto requestDailyRecordDto
    ){
        return dailyRecordService.update(dailyRecordId, requestDailyRecordDto);
    }
}
