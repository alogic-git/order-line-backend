package com.ptglue.schedule.controller;

import com.ptglue.basic.model.dto.ApiResponseDto;
import com.ptglue.schedule.model.dto.HolidayDto;
import com.ptglue.schedule.service.HolidayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Api(tags = {"63. Holiday"})
@RestController
@RequestMapping(path = {"holiday"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class HolidayController {
    @Resource(name = "holidayService")
    HolidayService holidayService;

    @ApiOperation(value = "휴일 상세 조회", notes = "선택한 휴일을 상세 조회 합니다.")
    @GetMapping("{holidayId}")
    public HolidayDto.ResponseHolidayDto get(
            @ApiParam(value = "holidayId", required = true, defaultValue = "1") @PathVariable Long holidayId
    ){
        return holidayService.get(holidayId);
    }

    @ApiOperation(value = "휴일 목록 조회", notes = "휴일 목록을 조회 합니다.")
    @GetMapping("")
    public List<HolidayDto.ResponseHolidayDto> getList(
            @ApiParam(value = "시작일자", required = true, defaultValue = "2023-11-01") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate startDate,
            @ApiParam(value = "종료일자", required = true, defaultValue = "2023-11-30") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate endDate
    ){
        return holidayService.getList(startDate, endDate);
    }

    @ApiOperation(value = "휴일 추가", notes = "휴일을 새로 추가 합니다.")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<HolidayDto.ResponseHolidayDto> create(
            @RequestBody @Valid HolidayDto.RequestHolidayDto requestHolidayDto
    ){
        HolidayDto.ResponseHolidayDto holidayInfo =  holidayService.create(requestHolidayDto);

        return ApiResponseDto.createdResponseEntity(holidayInfo.getId(), holidayInfo);
    }

    @ApiOperation(value = "휴일 수정", notes = "휴일을 수정 합니다.")
    @PatchMapping("{holidayId}")
    public HolidayDto.ResponseHolidayDto update(
            @ApiParam(value = "휴일 id", required = true, defaultValue = "1") @PathVariable Long holidayId,
            @RequestBody @Valid HolidayDto.RequestHolidayDto requestHolidayDto
    ){
        return holidayService.update(holidayId, requestHolidayDto);
    }
}
