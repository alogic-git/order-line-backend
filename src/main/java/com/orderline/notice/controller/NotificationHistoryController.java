package com.orderline.notice.controller;

import com.orderline.notice.enums.NotificationStatusTypeEnum;
import com.orderline.notice.enums.NotificationTypeEnum;
import com.orderline.notice.model.dto.NotificationHistoryDto;
import com.orderline.notice.service.NotificationHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

@Api(tags = {"72.Notification"})
@RestController
@RequestMapping(path = "common/branch/notification", produces = MediaType.APPLICATION_JSON_VALUE)
public class NotificationHistoryController {

    @Resource(name = "notificationHistoryService")
    NotificationHistoryService notificationHistoryService;

    @ApiOperation(value = "알림 목록 조회", notes = "알림 목록 조회")
    @GetMapping("")
    public NotificationHistoryDto.ResponseNotificationListDto getList(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "페이지 번호", required = true, defaultValue = "0") Integer page,
            @ApiParam(value = "페이지당 항목 수", required = true, defaultValue = "10") Integer maxResults,
            @ApiParam(value = "검색할 단어(title, contents 검색 가능)", defaultValue = "%") @RequestParam(required = false, defaultValue = "%") String searchWord,
            @ApiParam(value = "시작일자", required = true, defaultValue = "2023-12-01") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate startDate,
            @ApiParam(value = "종료일자", required = true, defaultValue = "2023-12-31") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate endDate,
            @ApiParam(value = "notificationType", required = true, defaultValue = "PUSH") @RequestParam NotificationTypeEnum notificationType
    ){
        Pageable pageable = PageRequest.of(page, maxResults);
        Long branchId = (Long) httpServletRequest.getAttribute("branchId");

        if (!searchWord.equals("%")) {
            searchWord = '%' + searchWord + '%';
        }

        Page<NotificationHistoryDto.ResponseNotificationDto> notificationHistoryPage = notificationHistoryService.getList(branchId, notificationType, searchWord, startDate, endDate ,pageable);

        return NotificationHistoryDto.ResponseNotificationListDto.build(notificationHistoryPage, page, maxResults);
    }

    @ApiOperation(value = "알림 받은 회원조회", notes = "알림을 받은 사용자를 조회 합니다.")
    @GetMapping("{notificationId}/user")
    public NotificationHistoryDto.ResponseMessageUserListDto getMessageUserList(
            @ApiParam(value = "페이지 번호", required = true, defaultValue = "0") Integer page,
            @ApiParam(value = "페이지당 항목 수", required = true, defaultValue = "10") Integer maxResults,
            @ApiParam(value = "알림 id", required = true, defaultValue = "0") @PathVariable(required = true) Long notificationId,
            @ApiParam(value = "알림 상태", required = false, defaultValue = "ALL") @RequestParam(required = false) NotificationStatusTypeEnum notificationStatusType
    ){
        Pageable pageable = PageRequest.of(page, maxResults);

        Page<NotificationHistoryDto.ResponseMessageUserDto> notificationMessageUserList= notificationHistoryService.getMessageUserList(notificationId, notificationStatusType, pageable);

        return NotificationHistoryDto.ResponseMessageUserListDto.build(notificationMessageUserList, page, maxResults);
    }

    @ApiOperation(value = "지점 내 전송 가능한 사용자 목록 조회", notes = "지점 내 전송 가능한 사용자 목록 조회")
    @GetMapping("user/{scheduleId}")
    public NotificationHistoryDto.ResponseMessageUserListDto getUserListByScheduleId(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "페이지 번호", required = true, defaultValue = "0") Integer page,
            @ApiParam(value = "페이지당 항목 수", required = true, defaultValue = "10") Integer maxResults,
            @ApiParam(value = "일정 ID") @RequestParam(required = false) Long scheduleId
    ){
        Pageable pageable = PageRequest.of(page, maxResults);

        Page<NotificationHistoryDto.ResponseMessageUserDto> notificationDtoPage = notificationHistoryService.getUserListByScheduleId(scheduleId, pageable);

        return NotificationHistoryDto.ResponseMessageUserListDto.build(notificationDtoPage, page, maxResults);
    }
}
