package com.ptglue.schedule.controller;

import com.ptglue.basic.model.dto.ApiResponseDto;
import com.ptglue.schedule.model.dto.RepeatReservationDto;
import com.ptglue.schedule.service.RepeatReservationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Api(tags = {"65. RepeatReservation"})
@RestController
@RequestMapping(path = {"manager/reservation/repeat-reservation", "tutor/reservation/repeat-reservation"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class RepeatReservationController {
    @Resource(name = "repeatReservationService")
    RepeatReservationService repeatReservationService;

    @ApiOperation(value = "반복 예약 생성 가능 확인", notes = "반복 예약 추가 가능여부를 확인 합니다.")
    @GetMapping("check/{reservationId}")
    public RepeatReservationDto.ResponseRepeatReservationCheckDto check(
        @ApiParam(value = "예약 Id", required = true, defaultValue = "1") @PathVariable Long reservationId
    ){
        return repeatReservationService.check(reservationId);
    }

    @ApiOperation(value = "반복 예약 추가", notes = "반복 예약을 추가합니다. 예약을 먼저 생성 후, 반복 예약 생성해주어야 합니다.")
    @PostMapping("{reservationId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RepeatReservationDto.ResponseRepeatReservationDto> create(
            @ApiParam(value = "예약 Id", required = true, defaultValue = "1") @PathVariable Long reservationId
    ){
        RepeatReservationDto.ResponseRepeatReservationDto responseRepeatReservationDto = repeatReservationService.create(reservationId);

        return ApiResponseDto.createdResponseEntity(responseRepeatReservationDto.getRepeatReservationId(), responseRepeatReservationDto);
    }

    @ApiOperation(value = "반복 예약 수정",notes = "반복 예약을 수정합니다.")
    @PatchMapping("{repeat-reservationId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RepeatReservationDto.ResponseRepeatReservationDto> update(
            @ApiParam(value = "예약 Id", required = true, defaultValue = "1") @PathVariable Long repeatReservationId,
            @RequestBody @Valid RepeatReservationDto.RequestRepeatReservationDto requestRepeatReservationDto
    ){
        RepeatReservationDto.ResponseRepeatReservationDto responseRepeatReservationDto = repeatReservationService.update(repeatReservationId, requestRepeatReservationDto);

        return ApiResponseDto.createdResponseEntity(responseRepeatReservationDto.getRepeatReservationId(), responseRepeatReservationDto);
    }

    @ApiOperation(value = "반복 예약 삭제",notes = "반복 예약을 삭제합니다.")
    @DeleteMapping("{repeat-reservationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(
            @ApiParam(value = "반복 예약 Id", required = true, defaultValue = "1") @PathVariable Long repeatReservationId
    ){
        repeatReservationService.delete(repeatReservationId);

        return ResponseEntity.noContent().build();
    }
}
