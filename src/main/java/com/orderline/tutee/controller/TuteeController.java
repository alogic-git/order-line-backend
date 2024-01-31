package com.orderline.tutee.controller;

import com.orderline.basic.enums.order.TuteeOrderEnum;
import com.orderline.branch.enums.StateTypeEnum;
import com.orderline.basic.enums.order.TicketOrderEnum;
import com.orderline.branch.model.dto.BranchTuteeDto;
import com.orderline.tutee.service.TuteeService;
import com.orderline.common.user.enums.UserRoleEnum;
import com.orderline.common.user.model.dto.UserDto;
import com.orderline.schedule.model.dto.RepeatReservationDto;
import com.orderline.schedule.model.dto.ReservationDto;
import com.orderline.schedule.service.RepeatReservationService;
import com.orderline.schedule.service.ReservationService;
import com.orderline.basic.enums.order.TicketPauseHistoryOrderEnum;
import com.orderline.ticket.model.dto.TicketDto;
import com.orderline.ticket.model.dto.TicketPauseHistoryDto;
import com.orderline.ticket.service.TicketPauseHistoryService;
import com.orderline.ticket.service.TicketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.orderline.basic.model.dto.ApiResponseDto;
import com.orderline.tutor.service.TutorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.orderline.basic.utils.Constants.DEFAULT_PAGE_NUM;
import static com.orderline.basic.utils.Constants.DEFAULT_PAGE_SIZE;

@Api(tags = {"12.Tutee"}) // 넘버링 확인 필요
@RestController
@RequestMapping(path = {"manager/tutee", "tutor/tutee"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class TuteeController {

    @Resource(name = "tuteeService")
    TuteeService tuteeService;

    @Resource(name = "tutorService")
    TutorService tutorService;

    @Resource(name = "ticketService")
    TicketService ticketService;

    @Resource(name = "repeatReservationService")
    RepeatReservationService repeatReservationService;

    @Resource(name = "ticketPauseHistoryService")
    TicketPauseHistoryService ticketPauseHistoryService;

    @Resource(name = "reservationService")
    ReservationService reservationService;

    @ApiOperation(value = "3.0.0_회원 HOME", notes = "정렬, 검색 포함하여 token의 지점의 회원 목록을 조회합니다.")
    @GetMapping("")
    public BranchTuteeDto.ResponseBranchTuteeListDto getList(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "진행중/종료", defaultValue = "ONGOING") @RequestParam(required = false, defaultValue = "ONGOING") StateTypeEnum stateType,
            @ApiParam(value = "검색할 단어", defaultValue = "%") @RequestParam(required = false, defaultValue = "%") String searchWord,
            @ApiParam(value = "정렬", defaultValue = "TUTEE_NAME_ASC") @RequestParam(required = false, defaultValue = "TUTEE_NAME_ASC") TuteeOrderEnum orderBy,
            @ApiParam(value = "페이지 번호", defaultValue = DEFAULT_PAGE_NUM) Integer page,
            @ApiParam(value = "페이지 크기", defaultValue = DEFAULT_PAGE_SIZE) Integer maxResults) {

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        Sort sort = orderBy.getSort();
        Pageable pageable = PageRequest.of(page, maxResults, sort);

        if (!searchWord.equals("%")) {
            searchWord = '%' + searchWord + '%';
        }
        Page<BranchTuteeDto.ResponseBranchTuteeDto> branchUserList = tuteeService.getList(stateType, branchId, searchWord, pageable);
        return BranchTuteeDto.ResponseBranchTuteeListDto.build(branchUserList, page, maxResults);
    }

    @ApiOperation(value = "3.1.1 -> 회원검색결과(현재 선택 지점에 등록된 회원인지 조회)", notes = "휴대폰번호로 지점의 회원인지 조회합니다.")
    @GetMapping("search/phone")
    public UserDto.ResponseUser getBranchUser(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "휴대폰 번호", required = true, defaultValue = "01012345678") @RequestParam String phone){

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        return tutorService.getBranchUser(branchId, UserRoleEnum.TUTEE, phone);
    }

    @ApiOperation(value = "3.1.0_회원 추가 -> case 1 : PTERS아이디가 있는 회원")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BranchTuteeDto.ResponseBranchTuteeDto> createBranchTutee(
            HttpServletRequest httpServletRequest,
            @RequestBody @Valid BranchTuteeDto.RequestBranchTuteeDto requestBranchTuteeDto) {

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        BranchTuteeDto.ResponseBranchTuteeDto branchUserInfo = tuteeService.createBranchTutee(branchId, requestBranchTuteeDto);
        Long branchUserRoleId = branchUserInfo.getBranchUserRoleId();
        return ApiResponseDto.createdResponseEntity(branchUserRoleId, branchUserInfo);
    }

    @ApiOperation(value = "3.1.2 -> 회원 등록페이지 case 2: 수강권 입력 후 / 3.2.1.1_수강권 재등록")
    @PostMapping("{userId}/ticket")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TicketDto.ResponseTicketDto> createTicket(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "userId id", required = true, defaultValue = "1") @PathVariable Long userId,
            @RequestBody @Valid TicketDto.RequestTicketDto requestTicketDto) {

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        TicketDto.ResponseTicketDto ticketDto = ticketService.createTicket(branchId, requestTicketDto);
        return ApiResponseDto.createdResponseEntity(ticketDto.getTicketId(), ticketDto);
    }

    @ApiOperation(value = "3.2.0_회원상세 -> 기본정보", notes = "지점 회원을 상세 조회합니다.")
    @GetMapping("{userId}")
    public BranchTuteeDto.ResponseBranchTuteeDto get(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "userId", required = true, defaultValue = "1") @PathVariable Long userId){

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        return tuteeService.get(branchId, userId);
    }

    @ApiOperation(value = "3.2.1_회원상세 -> 수강권", notes = "지점 회원의 수강권을 조회합니다.")
    @GetMapping("{userId}/ticket")
    public TicketDto.ResponseTicketListDto getTicket(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "userId", required = true, defaultValue = "1") @PathVariable Long userId,
            @ApiParam(value = "정렬", defaultValue = "TICKET_START_DT_DESC") @RequestParam(required = false, defaultValue = "TICKET_START_DT_DESC") TicketOrderEnum orderBy,
            @ApiParam(value = "페이지 번호", defaultValue = DEFAULT_PAGE_NUM) Integer page,
            @ApiParam(value = "페이지 크기", defaultValue = DEFAULT_PAGE_SIZE) Integer maxResults) {

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        //정렬 조건 확인
        Sort sort = orderBy.getSort();
        Pageable pageable = PageRequest.of(page, maxResults, sort);
        Page<TicketDto.ResponseTicketDto> ticketDtoPage = tuteeService.getTicketList(branchId, userId, pageable);
        return TicketDto.ResponseTicketListDto.build(ticketDtoPage, page, maxResults);
    }

    @ApiOperation(value = "3.2.0_회원상세 -> 기본정보(선택한 회원의 현재 지점 연결 해제)", notes = "선택한 회원의 현재 지점 연결 해제합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("{userId}/disconnect")
    public BranchTuteeDto.ResponseBranchTuteeDto disconnectTutee(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "userId", required = true, defaultValue = "1") @PathVariable Long userId){

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        return tuteeService.disconnectTutee(branchId, userId);
    }

    @ApiOperation(value = "3.2.0_회원상세 -> 반복 예약 일정", notes = "선택한 회원의 반복 예약 일정을 조회합니다.")
    @GetMapping("{userId}/repeat-reservation")
    public RepeatReservationDto.ResponseRepeatReservationListDto getRepeatSchedule(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "userId", required = true, defaultValue = "1") @PathVariable Long userId,
            @ApiParam(value = "상태", required = true, defaultValue = "ONGOING") @RequestParam(required = false, defaultValue = "ONGOING") StateTypeEnum statusType,
            @ApiParam(value = "페이지 번호", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
            @ApiParam(value = "페이지당 항목 수", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer maxResults){

        //정렬추가. 정렬방식 논의하기. order enum도 추가? 피그마엔 정렬 설정은 없음
        Sort sort = Sort.by(Sort.Direction.ASC, "endDate");// start-end-reg 순으로 정렬

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        Pageable pageable = PageRequest.of(page, maxResults, sort);
        Page<RepeatReservationDto.ResponseRepeatReservationDto> repeatReservationDtoPage = repeatReservationService.getRepeatReservationList(branchId, userId, statusType, pageable);
        return RepeatReservationDto.ResponseRepeatReservationListDto.build(repeatReservationDtoPage, page, maxResults);
    }

    @ApiOperation(value = "3.2.0_회원상세 -> 반복 일정 삭제", notes = "선택한 회원의 반복 일정을 삭제합니다.")
    @PatchMapping("{userId}/repeat-reservation/{repeatReservationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteRepeatReservation(
            @ApiParam(value = "userId", required = true, defaultValue = "1") @PathVariable Long userId,
            @ApiParam(value = "반복 예약 id", required = true, defaultValue = "1") @PathVariable Long repeatReservationId){

        reservationService.deleteRepeatReservation(repeatReservationId);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "3.2.2_회원상세 일시정지 이력 조회")
    @GetMapping("{userId}/ticket-pause-history")
    public TicketPauseHistoryDto.ResponseTicketPauseHistoryListDto getPauseList(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "userId", required = true, defaultValue = "1") @PathVariable Long userId,
            @ApiParam(value = "일시 정지 진행 중 인 것만 확인(ONGOING) / 전체 이력 확인 (ALL)", required = true, defaultValue = "ONGOING") @RequestParam(required = true, defaultValue = "ONGOING") StateTypeEnum statusType,
            @ApiParam(value = "정렬", required = true, defaultValue = "PAUSE_START_DT_DESC") @RequestParam(required = false, defaultValue = "START_DT_DESC") TicketPauseHistoryOrderEnum orderBy,
            @ApiParam(value = "페이지 번호", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
            @ApiParam(value = "페이지당 항목 수", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer maxResults){

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        Sort sort = orderBy.getSort();
        Pageable pageable = PageRequest.of(page, maxResults, sort);
        Page<TicketPauseHistoryDto.ResponseTicketPauseHistoryDto> ticketPauseHistoryDtos = ticketPauseHistoryService.getList(branchId, userId, statusType, pageable);
        return TicketPauseHistoryDto.ResponseTicketPauseHistoryListDto.build(ticketPauseHistoryDtos, page, maxResults);
    }

    @ApiOperation(value = "지점의 회원의 예약 일정 조회 (시간순 정렬)", notes = "지점의 회원의 예약 일정을 조회 합니다.")
    @GetMapping("{userId}/reservation")
    public ReservationDto.ResponseTuteeReservationListDto getReservationTuteeList(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "회원 id", required = true, defaultValue = "1") @PathVariable Long userId,
            @ApiParam(value = "페이지 번호", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
            @ApiParam(value = "페이지당 항목 수", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer maxResults){

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        Sort sort = Sort.by(Sort.Direction.DESC, "schedule.startDt");
        Pageable pageable = PageRequest.of(page, maxResults, sort);
        Page<ReservationDto.ResponseTuteeReservationDto> tuteeReservationDtoPage = reservationService.getTuteeReservationList(branchId, userId, pageable);
        return ReservationDto.ResponseTuteeReservationListDto.build(tuteeReservationDtoPage, page, maxResults);
    }

    @ApiOperation(value = "지점의 회원의 예약 일정 조회 (월별 정렬)", notes = "지점의 회원의 예약 일정을 조회 합니다.")
    @GetMapping("{userId}/reservation/monthly")
    public ReservationDto.ResponseMonthlyTuteeReservationListDto getReservationTuteeListMonthly(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "회원 id", required = true, defaultValue = "1") @PathVariable Long userId,
            @ApiParam(value = "페이지 번호", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
            @ApiParam(value = "페이지당 항목 수", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer maxResults){

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        Sort sort = Sort.by(Sort.Direction.DESC, "schedule.startDt");
        Pageable pageable = PageRequest.of(page, maxResults, sort);
        Page<ReservationDto.ResponseTuteeReservationDto> tuteeReservationDtoPage = reservationService.getTuteeReservationList(branchId, userId, pageable);
        return ReservationDto.ResponseMonthlyTuteeReservationListDto.build(tuteeReservationDtoPage, page, maxResults);
    }

    @ApiOperation(value = "지점의 회원의 예약 일정 조회 (티켓별 정렬)", notes = "지점의 회원의 예약 일정을 조회 합니다.")
    @GetMapping("{userId}/reservation/ticket")
    public ReservationDto.ResponseTuteeReservationByTicketListDto getReservationTuteeListByTicket(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "회원 id", required = true, defaultValue = "1") @PathVariable Long userId,
            @ApiParam(value = "페이지 번호", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
            @ApiParam(value = "페이지당 항목 수", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer maxResults){

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        Sort sort = Sort.by(Sort.Direction.ASC, "ticket.id");
        Pageable pageable = PageRequest.of(page, maxResults, sort);
        Page<ReservationDto.ResponseTuteeReservationDto> tuteeReservationDtoPage = reservationService.getTuteeReservationList(branchId, userId, pageable);
        return ReservationDto.ResponseTuteeReservationByTicketListDto.build(tuteeReservationDtoPage, page, maxResults);
    }
}