package com.orderline.schedule.model.dto;

import com.orderline.basic.utils.TimeFunction;
import com.orderline.klass.enums.KlassLevelTypeEnum;
import com.orderline.schedule.enums.ScheduleMemberTypeEnum;
import com.orderline.klass.enums.KlassColorTypeEnum;
import com.orderline.schedule.enums.ReservationStatusTypeEnum;
import com.orderline.schedule.enums.ScheduleStatusTypeEnum;
import com.orderline.ticket.model.dto.TicketMockupDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

public class ReservationMockupDto {
    public static ReservationDto.ResponseReservationTuteeDto getReservationDetailMockup() {
        return ReservationDto.ResponseReservationTuteeDto.builder()
                .reservationId(1L)
                .tuteeId(1L)
                .tuteeName("고배배")
                .tuteeNum(1)
                .reservationCount(1)
                .statusType(ReservationStatusTypeEnum.ATTENDANCE)
                .cancelYn(false)
                .ticketDto(TicketMockupDto.getTicketMockup())
                .build();
    }

    public static ReservationDto.ResponseReservationTuteeDto getReservationDetailMockup2() {
        return ReservationDto.ResponseReservationTuteeDto.builder()
                .reservationId(2L)
                .tuteeId(2L)
                .tuteeName("노다지")
                .tuteeNum(1)
                .reservationCount(1)
                .statusType(ReservationStatusTypeEnum.CONFIRMATION)
                .cancelYn(false)
                .ticketDto(TicketMockupDto.getTicketMockup2())
                .build();
    }

    public static ReservationDto.ResponseReservationTuteeListDto getReservationDetailListMockup() {
        return ReservationDto.ResponseReservationTuteeListDto.builder()
                .results(Arrays.asList(getReservationDetailMockup(), getReservationDetailMockup2()))
                .currentPage(0)
                .maxResults(10)
                .totalPages(1)
                .totalElements(2L)
                .build();
    }

    public static ReservationDto.ResponseTuteeReservationDto getReservationTuteeMockup1() {
        return ReservationDto.ResponseTuteeReservationDto.builder()
                .reservationId(1L)
                .scheduleId(1L)
                .klassId(1L)
                .klassName("클래스 이름")
                .startDt(1701241200L)
                .endDt(1701244800L)
                .statusType(ReservationStatusTypeEnum.ATTENDANCE)
                .signUrl("")
                .build();
    }

    public static ReservationDto.ResponseTuteeReservationDto getReservationTuteeMockup2() {
        return ReservationDto.ResponseTuteeReservationDto.builder()
                .reservationId(2L)
                .scheduleId(2L)
                .klassId(1L)
                .klassName("클래스 이름")
                .startDt(1701231200L)
                .endDt(1701234800L)
                .statusType(ReservationStatusTypeEnum.ATTENDANCE)
                .signUrl("")
                .build();
    }

    public static ReservationDto.ResponseTuteeReservationDto getReservationTuteeMockup3() {
        return ReservationDto.ResponseTuteeReservationDto.builder()
                .reservationId(3L)
                .scheduleId(3L)
                .klassId(1L)
                .klassName("클래스 이름")
                .startDt(1698562800L)
                .endDt(1698566400L)
                .statusType(ReservationStatusTypeEnum.ATTENDANCE)
                .signUrl("")
                .build();
    }

    public static ReservationDto.ResponseTuteeReservationDto getReservationTuteeMockup4() {
        return ReservationDto.ResponseTuteeReservationDto.builder()
                .reservationId(4L)
                .scheduleId(4L)
                .klassId(1L)
                .klassName("클래스 이름")
                .startDt(1698552000L)
                .endDt(1698555600L)
                .statusType(ReservationStatusTypeEnum.ATTENDANCE)
                .signUrl("")
                .build();
    }

    public static ReservationDto.ResponseTuteeReservationDto getTuteeReservationMockup1() {
        return ReservationDto.ResponseTuteeReservationDto.builder()
                .reservationId(1L)
                .scheduleId(1L)
                .klassId(1L)
                .klassName("클래스 이름")
                .startDt(1701241200L)
                .endDt(1701244800L)
                .statusType(ReservationStatusTypeEnum.ATTENDANCE)
                .signUrl("")
                .build();
    }

    public static ReservationDto.ResponseTuteeReservationDto getTuteeReservationMockup2() {
        return ReservationDto.ResponseTuteeReservationDto.builder()
                .reservationId(2L)
                .scheduleId(2L)
                .klassId(1L)
                .klassName("클래스 이름")
                .startDt(1701231200L)
                .endDt(1701234800L)
                .statusType(ReservationStatusTypeEnum.ATTENDANCE)
                .signUrl("")
                .build();
    }

    public static ReservationDto.ResponseTuteeReservationDto getTuteeReservationMockup3() {
        return ReservationDto.ResponseTuteeReservationDto.builder()
                .reservationId(3L)
                .scheduleId(3L)
                .klassId(1L)
                .klassName("클래스 이름")
                .startDt(1698562800L)
                .endDt(1698566400L)
                .statusType(ReservationStatusTypeEnum.ATTENDANCE)
                .signUrl("")
                .build();
    }

    public static ReservationDto.ResponseTuteeReservationDto getTuteeReservationMockup4() {
        return ReservationDto.ResponseTuteeReservationDto.builder()
                .reservationId(4L)
                .scheduleId(4L)
                .klassId(1L)
                .klassName("클래스 이름")
                .startDt(1698552000L)
                .endDt(1698555600L)
                .statusType(ReservationStatusTypeEnum.ATTENDANCE)
                .signUrl("")
                .build();
    }

    public static ReservationDto.ResponseTuteeReservationListDto getTuteeReservationListMockup() {
        return ReservationDto.ResponseTuteeReservationListDto.builder()
                .results(Arrays.asList(getTuteeReservationMockup1(), getTuteeReservationMockup2()))
                .currentPage(0)
                .maxResults(10)
                .totalPages(1)
                .totalElements(2L)
                .build();
    }

    public static ReservationDto.ResponseMonthlyTuteeReservationDto getMonthlyTuteeReservationMockup() {
        return ReservationDto.ResponseMonthlyTuteeReservationDto.builder()
                .month("2023년 11월")
                .reservations(Arrays.asList(getTuteeReservationMockup1(), getTuteeReservationMockup2()))
                .build();
    }

    public static ReservationDto.ResponseMonthlyTuteeReservationDto getMonthlyTuteeReservationMockup2() {
        return ReservationDto.ResponseMonthlyTuteeReservationDto.builder()
                .month("2023년 10월")
                .reservations(Arrays.asList(getTuteeReservationMockup3(), getTuteeReservationMockup4()))
                .build();
    }

    public static ReservationDto.ResponseTuteeReservationByTicketDto getTuteeReservationByTicketMockup1() {
        return ReservationDto.ResponseTuteeReservationByTicketDto.builder()
                .ticketDto(TicketMockupDto.getTicketMockup())
                .reservations(Arrays.asList(getTuteeReservationMockup1(), getTuteeReservationMockup2()))
                .build();
    }

    public static ReservationDto.ResponseTuteeReservationByTicketDto getTuteeReservationByTicketMockup2() {
        return ReservationDto.ResponseTuteeReservationByTicketDto.builder()
                .ticketDto(TicketMockupDto.getTicketMockup2())
                .reservations(Arrays.asList(getTuteeReservationMockup3(), getTuteeReservationMockup4()))
                .build();
    }

    public static ReservationDto.ResponseTuteeReservationByTicketListDto getTuteeReservationByTicketListMockup() {
        return ReservationDto.ResponseTuteeReservationByTicketListDto.builder()
                .results(Arrays.asList(getTuteeReservationByTicketMockup1(), getTuteeReservationByTicketMockup2()))
                .currentPage(0)
                .maxResults(10)
                .totalPages(1)
                .totalElements(2L)
                .build();
    }

    public static ReservationDto.ResponseMonthlyTuteeReservationListDto getReservationTuteeListMonthlyMockup() {
        return ReservationDto.ResponseMonthlyTuteeReservationListDto.builder()
                .results(Arrays.asList(getMonthlyTuteeReservationMockup(), getMonthlyTuteeReservationMockup2()))
                .currentPage(0)
                .maxResults(10)
                .totalPages(1)
                .totalElements(2L)
                .build();
    }

    public static ReservationTuteeDto.ResponseReservationDto getReseponseReservedDetailMockup() {
        return ReservationTuteeDto.ResponseReservationDto.builder()
                .reservationId(1L)
                .scheduleId(3L)
                .ticketId(3L)
                .klassName("전신 하는날")
                .mainTutorNickName("missing")
                .subTutorNickName("fisz")
                .scheduleStatusType(ScheduleStatusTypeEnum.AFTER_RECEIPT)
                .reservationStatusType(ReservationStatusTypeEnum.WAIT)
                .scheduleMemberType(ScheduleMemberTypeEnum.INDIVIDUAL)
                .level(KlassLevelTypeEnum.LOW)
                .currentTuteeNum(1)
                .maxTuteeNum(10)
                .minTuteeNum(1)
                .waitingCurrentTuteeNum(10)
                .waitingTuteeNum(10)
                .cancelEnableTime(15)
                .minTuteeLackCancelTime(15)
                .klassPublicMemo("klass public note, 강의에서 설명해주는 내용, 클래스 설명")
                .schedulePublicMemo("schedule public note, 일정에서 설명해주는 내용, 해당 일정 설명")
                //                .reservationPublicMemo("reservation public note, 예약한곳에 저장된 내용")
                .ticketName("강강강 강의")
                .availableReservationCount(30)
                .remainReservationCount(30)
                .totalReservationCount(60)
                .startDt(TimeFunction.toUnixTime(ZonedDateTime.of(LocalDate.of(2023, 12, 30), LocalTime.now().minusHours(3), ZoneId.of("Asia/Seoul"))))
                .endDt(TimeFunction.toUnixTime(ZonedDateTime.of(LocalDate.of(2023, 12, 30), LocalTime.now().minusHours(2), ZoneId.of("Asia/Seoul"))))
                .build();
    }

    public static ReservationDto.ResponseAttendanceReservationDto getAttendanceReservationMockup() {
        return ReservationDto.ResponseAttendanceReservationDto.builder()
                .reservationId(1L)
                .branchId(1L)
                .klassId(1L)
                .scheduleId(1L)
                .ticketId(1L)
                .tuteeId(1L)
                .tuteeNum(1)
                .reservationCount(1)
                .statusType(ReservationStatusTypeEnum.CONFIRMATION)
                .tuteeName("다지")
                .klassName("클래스 이름")
                .colorCdType(KlassColorTypeEnum.CORAL)
                .startDt(1701241200L)
                .endDt(1701244800L)
                .mainTutorName("메인 튜터")
                .currentTuteeNum(10)
                .maxTuteeNum(20)
                .level(KlassLevelTypeEnum.HIGH)
                .build();
    }

    public static List<ReservationDto.ResponseAttendanceReservationDto>  getAttendanceReservationListMockup() {
        return Arrays.asList(getAttendanceReservationMockup(), getAttendanceReservationMockup());
    }
}

