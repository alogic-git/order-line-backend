package com.ptglue.schedule.model.dto;

import com.ptglue.basic.model.dto.CommonDto;
import com.ptglue.basic.utils.TimeFunction;
import com.ptglue.klass.enums.KlassColorTypeEnum;
import com.ptglue.klass.enums.KlassLevelTypeEnum;
import com.ptglue.klass.model.dto.KlassMockupDto;
import com.ptglue.schedule.enums.*;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

public class ScheduleMockupDto {

    public static List<ScheduleDto.ResponseScheduleListDto> getListMockup(){
        List<ScheduleDto.ResponseScheduleKlassListDto> scheduleKlassList
                = Arrays.asList(ScheduleMockupDto.getScheduleKlassListMockup1(),
                ScheduleMockupDto.getScheduleKlassListMockup2(),
                ScheduleMockupDto.getScheduleKlassListMockup3(),
                ScheduleMockupDto.getScheduleKlassListMockup4());

        SortedMap<LocalDate, List<ScheduleDto.ResponseScheduleKlassListDto>> scheduleKlassListGroupByDate = scheduleKlassList.stream()
                .collect(Collectors.groupingBy(ScheduleDto.ResponseScheduleKlassListDto::getDate, TreeMap::new, Collectors.toList()));

        List<ScheduleDto.ResponseScheduleListDto> scheduleList = new ArrayList<>();
        for (LocalDate date : scheduleKlassListGroupByDate.keySet()) {
            scheduleList.add(ScheduleDto.ResponseScheduleListDto.toDto(scheduleKlassListGroupByDate.get(date)));
        }
        return scheduleList;
    }

    public static ScheduleDto.ResponseScheduleDto getMockup() {
        return ScheduleDto.ResponseScheduleDto.builder()
                .scheduleId(1L)
                .branchId(101L)
                .klassId(201L)
                .mainTutorId(401L)
                .subTutorId(402L)
                .klassName("Sample Class")
                .startDt(TimeFunction.toUnixTime(ZonedDateTime.now()))
                .endDt(TimeFunction.toUnixTime(ZonedDateTime.now().plusHours(2)))
                .minTuteeNum(5)
                .maxTuteeNum(20)
                .currentTuteeNum(10)
                .waitingCurrentTuteeNum(3)
                .statusType(ScheduleStatusTypeEnum.BEFORE_RECEIPT)
                .privateYn(false)
                .scheduleType(ScheduleTypeEnum.NORMAL)
                .level(KlassLevelTypeEnum.MIDDLE)
                .colorCdType(CommonDto.ColorCodeDto.toDto(KlassColorTypeEnum.CORAL))
                .privateMemo("Private Memo")
                .publicMemo("Public Memo")
                .reservationEnableYn(true)
                .reservationEnableTime(30)
                .cancelEnableTime(60)
                .minTuteeLackCancelTime(120)
                .cancelYn(false)
                .build();
    }

    public static List<ScheduleDto.ResponseScheduleDto> getScheduleListMockup() {
        return Arrays.asList(getMockup(), getMockup());
    }

    public static ScheduleDto.ResponseSchedulePagingListDto getSchedulePagingListMockup() {
        return ScheduleDto.ResponseSchedulePagingListDto.builder()
                .results(getScheduleListMockup())
                .currentPage(0)
                .maxResults(10)
                .totalPages(1)
                .totalElements(2L)
                .build();
    }

    public static ScheduleDto.ResponseScheduleDto getOffScheduleMockup() {
        return ScheduleDto.ResponseScheduleDto.builder()
                .scheduleId(1L)
                .branchId(101L)
                .klassId(201L)
                .mainTutorId(401L)
                .subTutorId(402L)
                .klassName("Sample Class")
                .startDt(TimeFunction.toUnixTime(ZonedDateTime.now()))
                .endDt(TimeFunction.toUnixTime(ZonedDateTime.now().plusHours(2)))
                .minTuteeNum(5)
                .maxTuteeNum(20)
                .currentTuteeNum(10)
                .waitingCurrentTuteeNum(3)
                .statusType(ScheduleStatusTypeEnum.BEFORE_RECEIPT)
                .privateYn(false)
                .scheduleType(ScheduleTypeEnum.OFF)
                .level(KlassLevelTypeEnum.HIGH)
                .colorCdType(CommonDto.ColorCodeDto.toDto(KlassColorTypeEnum.CORAL))
                .privateMemo("Private Memo")
                .publicMemo("Public Memo")
                .reservationEnableYn(true)
                .reservationEnableTime(30)
                .cancelEnableTime(60)
                .minTuteeLackCancelTime(120)
                .cancelYn(false)
                .build();
    }

    public static ScheduleDto.ResponseScheduleKlassListDto getScheduleKlassListMockup1() {
        return ScheduleDto.ResponseScheduleKlassListDto.builder()
                .scheduleId(1L)
                .date(LocalDate.of(2023, 5, 11))
                .startDt(TimeFunction.toUnixTime(ZonedDateTime.of(LocalDateTime.of(2023, 5, 11, 10, 0), ZoneId.of("Asia/Seoul"))))
                .endDt(TimeFunction.toUnixTime(ZonedDateTime.of(LocalDateTime.of(2023, 5, 11, 12, 0), ZoneId.of("Asia/Seoul"))))
                .maxTuteeNum(20)
                .currentTuteeNum(10)
                .klassDto(KlassMockupDto.getKlassMockup1())
                .build();
    }

    public static ScheduleDto.ResponseScheduleKlassListDto getScheduleKlassListMockup2() {
        return ScheduleDto.ResponseScheduleKlassListDto.builder()
                .scheduleId(2L)
                .date(LocalDate.of(2023, 5, 15))
                .startDt(TimeFunction.toUnixTime(ZonedDateTime.of(LocalDateTime.of(2023, 5, 15, 10, 0), ZoneId.of("Asia/Seoul"))))
                .endDt(TimeFunction.toUnixTime(ZonedDateTime.of(LocalDateTime.of(2023, 5, 15, 12, 0), ZoneId.of("Asia/Seoul"))))
                .maxTuteeNum(4)
                .currentTuteeNum(1)
                .klassDto(KlassMockupDto.getKlassMockup1())
                .build();
    }

    public static ScheduleDto.ResponseScheduleKlassListDto getScheduleKlassListMockup3() {
        return ScheduleDto.ResponseScheduleKlassListDto.builder()
                .scheduleId(3L)
                .date(LocalDate.of(2023, 5, 15))
                .startDt(TimeFunction.toUnixTime(ZonedDateTime.of(LocalDateTime.of(2023, 5, 15, 14, 0), ZoneId.of("Asia/Seoul"))))
                .endDt(TimeFunction.toUnixTime(ZonedDateTime.of(LocalDateTime.of(2023, 5, 15, 18, 0), ZoneId.of("Asia/Seoul"))))
                .maxTuteeNum(24)
                .currentTuteeNum(11)
                .klassDto(KlassMockupDto.getKlassMockup1())
                .build();
    }

    public static ScheduleDto.ResponseScheduleKlassListDto getScheduleKlassListMockup4() {
        return ScheduleDto.ResponseScheduleKlassListDto.builder()
                .scheduleId(4L)
                .date(LocalDate.of(2023, 5, 15))
                .startDt(TimeFunction.toUnixTime(ZonedDateTime.of(LocalDateTime.of(2023, 5, 15, 16, 0), ZoneId.of("Asia/Seoul"))))
                .endDt(TimeFunction.toUnixTime(ZonedDateTime.of(LocalDateTime.of(2023, 5, 15, 17, 0), ZoneId.of("Asia/Seoul"))))
                .maxTuteeNum(10)
                .currentTuteeNum(1)
                .klassDto(KlassMockupDto.getKlassMockup2())
                .build();
    }


    public static RepeatScheduleDto.ResponseRepeatScheduleDto getRepeatScheduleMockup() {
        return RepeatScheduleDto.ResponseRepeatScheduleDto.builder()
                .repeatScheduleId(1L)
                .branchId(101L)
                .klassId(201L)
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 12, 31))
                .statusType(RepeatScheduleStatusTypeEnum.BEFORE)
                .sunStartEndTime(Arrays.asList("09:00~18:00"))
                .monStartEndTime(Arrays.asList("09:00~18:00"))
                .tueStartEndTime(Arrays.asList("09:00~18:00"))
                .wedStartEndTime(Arrays.asList("09:00~18:00"))
                .thrStartEndTime(Arrays.asList("09:00~18:00"))
                .friStartEndTime(Arrays.asList("09:00~18:00"))
                .satStartEndTime(Arrays.asList("09:00~18:00", "19:00~22:00"))
                .archiveYn(false)
                .build();
    }

    private static ScheduleTuteeDto.ResponseReservationTuteeByTicketIdDto getTimestampMockup(ReservationStatusTypeEnum statusType, Integer day) {
        return ScheduleTuteeDto.ResponseReservationTuteeByTicketIdDto.builder()
                .statusType(statusType)
                .klassName("강의" + day)
                .startDt(TimeFunction.toUnixTime(ZonedDateTime.of(LocalDate.of(2023, 12, day), LocalTime.now(), ZoneId.of("Asia/Seoul"))))
                .endDt(TimeFunction.toUnixTime(ZonedDateTime.of(LocalDate.of(2023, 12, day), LocalTime.now().plusHours(1), ZoneId.of("Asia/Seoul"))))
                .build();
    }

    public static ScheduleTuteeDto.ResponseReservationTuteeListByTicketIdDto getAttendanceStamp() {
        List<ScheduleTuteeDto.ResponseReservationTuteeByTicketIdDto> timestampList = Arrays.asList(
                getTimestampMockup(ReservationStatusTypeEnum.ATTENDANCE, 1),
                getTimestampMockup(ReservationStatusTypeEnum.ABSENCE, 2),
                getTimestampMockup(ReservationStatusTypeEnum.ATTENDANCE, 3),
                getTimestampMockup(ReservationStatusTypeEnum.ATTENDANCE, 4),
                getTimestampMockup(ReservationStatusTypeEnum.ABSENCE, 5),
                getTimestampMockup(ReservationStatusTypeEnum.ATTENDANCE, 6)
        );

        return ScheduleTuteeDto.ResponseReservationTuteeListByTicketIdDto.builder()
                .results(timestampList)
                .maxResults(5)
                .currentPage(1)
                .totalPages(1)
                .totalElements(5L)
                .build();
    }

    private static ReservationTuteeDto.ResponseReservationTuteeDto getScheduleReservationTuteeMockup1() {
        return ReservationTuteeDto.ResponseReservationTuteeDto.builder()
                .reservationId(1L)
                .scheduleId(1L)
                .ticketId(1L)
                .klassId(1L)
                .klassName("예약 하는날1")
                .startDt(TimeFunction.toUnixTime(ZonedDateTime.of(LocalDate.of(2023, 12, 12), LocalTime.now(), ZoneId.of("Asia/Seoul"))))
                .endDt(TimeFunction.toUnixTime(ZonedDateTime.of(LocalDate.of(2023, 12, 12), LocalTime.now().plusHours(2), ZoneId.of("Asia/Seoul"))))
                .mainTutorNickName("봉명진")
                .subTutorNickName("예승빈")
                .colorCdType(KlassColorTypeEnum.PACIFIC)
                .scheduleStatusTypeEnum(ScheduleStatusTypeEnum.BEING_RECEIPT)
                .reservationStatusTypeEnum(ReservationStatusTypeEnum.CONFIRMATION)
                .scheduleMemberType(ScheduleMemberTypeEnum.GROUP)
                .currentTuteeNum(3)
                .maxTuteeNum(12)
                .level(KlassLevelTypeEnum.LOW)
                .ticketName("강 강의")
                .reservationEnableYn(false)
                .minTuteeLackCancelTime(20)
                .cancelEnableTime(30)
                .build();
    }

    private static ReservationTuteeDto.ResponseReservationTuteeDto getScheduleReservationTuteeMockup2() {
        return ReservationTuteeDto.ResponseReservationTuteeDto.builder()
                .reservationId(2L)
                .scheduleId(2L)
                .ticketId(2L)
                .klassId(2L)
                .klassName("예약 하는날2")
                .startDt(TimeFunction.toUnixTime(ZonedDateTime.of(LocalDate.of(2023, 12, 19), LocalTime.now(), ZoneId.of("Asia/Seoul"))))
                .endDt(TimeFunction.toUnixTime(ZonedDateTime.of(LocalDate.of(2023, 12, 19), LocalTime.now().plusHours(1), ZoneId.of("Asia/Seoul"))))
                .mainTutorNickName("모빌에")
                .subTutorNickName("노동은")
                .colorCdType(KlassColorTypeEnum.BLUEBERRY)
                .scheduleStatusTypeEnum(ScheduleStatusTypeEnum.BEING_RECEIPT)
                .reservationStatusTypeEnum(ReservationStatusTypeEnum.WAIT)
                .scheduleMemberType(ScheduleMemberTypeEnum.INDIVIDUAL)
                .currentTuteeNum(0)
                .maxTuteeNum(9)
                .level(KlassLevelTypeEnum.HIGH)
                .ticketName("강강강 강의")
                .reservationEnableYn(false)
                .minTuteeLackCancelTime(40)
                .cancelEnableTime(30)
                .build();
    }

    private static ReservationTuteeDto.ResponseReservationTuteeDto getScheduleReservationTuteeMockup3() {
        return ReservationTuteeDto.ResponseReservationTuteeDto.builder()
                .reservationId(3L)
                .scheduleId(3L)
                .ticketId(3L)
                .klassId(3L)
                .klassName("예약 하는날3")
                .startDt(TimeFunction.toUnixTime(ZonedDateTime.of(LocalDate.of(2023, 12, 30), LocalTime.now(), ZoneId.of("Asia/Seoul"))))
                .endDt(TimeFunction.toUnixTime(ZonedDateTime.of(LocalDate.of(2023, 12, 30), LocalTime.now().plusHours(4), ZoneId.of("Asia/Seoul"))))
                .mainTutorNickName("영광은")
                .subTutorNickName("이에서")
                .colorCdType(KlassColorTypeEnum.CORAL)
                .scheduleStatusTypeEnum(ScheduleStatusTypeEnum.AFTER_RECEIPT)
                .reservationStatusTypeEnum(ReservationStatusTypeEnum.WAIT)
                .scheduleMemberType(ScheduleMemberTypeEnum.INDIVIDUAL)
                .currentTuteeNum(20)
                .maxTuteeNum(100)
                .level(KlassLevelTypeEnum.MIDDLE)
                .ticketName("강강 강의")
                .reservationEnableYn(false)
                .minTuteeLackCancelTime(40)
                .cancelEnableTime(30)
                .build();
    }

    public static ReservationTuteeDto.ResponseReservationTuteeListDto getScheduleReservationTuteeListMockup() {
        List<ReservationTuteeDto.ResponseReservationTuteeDto> reservationList = Arrays.asList(
                getScheduleReservationTuteeMockup1(),
                getScheduleReservationTuteeMockup2(),
                getScheduleReservationTuteeMockup3()
        );

        return ReservationTuteeDto.ResponseReservationTuteeListDto.builder()
                .results(reservationList)
                .maxResults(3)
                .currentPage(1)
                .totalPages(1)
                .totalElements(3L)
                .build();
    }

    private static ScheduleTuteeDto.ResponseScheduleTuteeDto getScheduleMockup1() {
        return ScheduleTuteeDto.ResponseScheduleTuteeDto.builder()
                .scheduleId(1L)
                .ticketId(1L)
                .klassId(1L)
                .klassName("강의 하는날1")
                .startDt(TimeFunction.toUnixTime(ZonedDateTime.of(LocalDate.of(2023, 12, 12), LocalTime.now(), ZoneId.of("Asia/Seoul"))))
                .endDt(TimeFunction.toUnixTime(ZonedDateTime.of(LocalDate.of(2023, 12, 12), LocalTime.now().plusHours(4), ZoneId.of("Asia/Seoul"))))
                .mainTutorNickName("문기태")
                .subTutorNickName("임해원")
                .colorCdType(KlassColorTypeEnum.LIME)
                .scheduleMemberType(ScheduleMemberTypeEnum.INDIVIDUAL)
                .scheduleStatusTypeEnum(ScheduleStatusTypeEnum.BEING_RECEIPT)
                .currentTuteeNum(3)
                .maxTuteeNum(12)
                .ticketName("티켓 사용10")
                .level(KlassLevelTypeEnum.LOW)
                .reservationEnableYn(true)
                .cancelEnableTime(30)
                .minTuteeLackCancelTime(40)
                .build();
    }

    private static ScheduleTuteeDto.ResponseScheduleTuteeDto getScheduleMockup2() {
        return ScheduleTuteeDto.ResponseScheduleTuteeDto.builder()
                .scheduleId(2L)
                .ticketId(2L)
                .klassId(2L)
                .klassName("강의 하는날2")
                .startDt(TimeFunction.toUnixTime(ZonedDateTime.of(LocalDate.of(2023, 12, 19), LocalTime.now(), ZoneId.of("Asia/Seoul"))))
                .endDt(TimeFunction.toUnixTime(ZonedDateTime.of(LocalDate.of(2023, 12, 19), LocalTime.now().plusHours(1), ZoneId.of("Asia/Seoul"))))
                .mainTutorNickName("허원희")
                .subTutorNickName("최연석")
                .colorCdType(KlassColorTypeEnum.EMERALD)
                .scheduleMemberType(ScheduleMemberTypeEnum.GROUP)
                .scheduleStatusTypeEnum(ScheduleStatusTypeEnum.BEING_RECEIPT)
                .currentTuteeNum(0)
                .maxTuteeNum(9)
                .ticketName("티켓 사용1")
                .level(KlassLevelTypeEnum.HIGH)
                .reservationEnableYn(true)
                .cancelEnableTime(30)
                .minTuteeLackCancelTime(40)
                .build();
    }

    private static ScheduleTuteeDto.ResponseScheduleTuteeDto getScheduleMockup3() {
        return ScheduleTuteeDto.ResponseScheduleTuteeDto.builder()
                .scheduleId(3L)
                .ticketId(3L)
                .klassId(3L)
                .klassName("강의 하는날3")
                .startDt(TimeFunction.toUnixTime(ZonedDateTime.of(LocalDate.of(2023, 12, 30), LocalTime.now(), ZoneId.of("Asia/Seoul"))))
                .endDt(TimeFunction.toUnixTime(ZonedDateTime.of(LocalDate.of(2023, 12, 30), LocalTime.now().plusHours(4), ZoneId.of("Asia/Seoul"))))
                .mainTutorNickName("최동하")
                .subTutorNickName("이시준")
                .colorCdType(KlassColorTypeEnum.POMEGRANATE)
                .scheduleMemberType(ScheduleMemberTypeEnum.INDIVIDUAL)
                .scheduleStatusTypeEnum(ScheduleStatusTypeEnum.BEING_RECEIPT)
                .currentTuteeNum(20)
                .maxTuteeNum(100)
                .ticketName("티켓 사용1")
                .level(KlassLevelTypeEnum.MIDDLE)
                .reservationEnableYn(true)
                .cancelEnableTime(30)
                .minTuteeLackCancelTime(40)
                .build();
    }


    private static ScheduleTuteeDto.ResponseScheduleTuteeDto getScheduleMockup4() {
        return ScheduleTuteeDto.ResponseScheduleTuteeDto.builder()
                .scheduleId(4L)
                .ticketId(4L)
                .klassId(4L)
                .klassName("강의 하는날4")
                .startDt(TimeFunction.toUnixTime(ZonedDateTime.of(LocalDate.of(2023, 12, 30), LocalTime.now().minusHours(3), ZoneId.of("Asia/Seoul"))))
                .endDt(TimeFunction.toUnixTime(ZonedDateTime.of(LocalDate.of(2023, 12, 30), LocalTime.now().minusHours(2), ZoneId.of("Asia/Seoul"))))
                .mainTutorNickName("영광은")
                .subTutorNickName("이에서")
                .colorCdType(KlassColorTypeEnum.OLIVE)
                .scheduleMemberType(ScheduleMemberTypeEnum.INDIVIDUAL)
                .scheduleStatusTypeEnum(ScheduleStatusTypeEnum.AFTER_RECEIPT)
                .currentTuteeNum(10)
                .maxTuteeNum(30)
                .ticketName("티켓 사용15")
                .level(KlassLevelTypeEnum.MIDDLE)
                .reservationEnableYn(true)
                .cancelEnableTime(30)
                .minTuteeLackCancelTime(40)
                .build();
    }

    public static ScheduleTuteeDto.ResponseScheduleTuteeListDto getScheduleTuteeListMockup() {
        List<ScheduleTuteeDto.ResponseScheduleTuteeDto> reservationList = Arrays.asList(
                getScheduleMockup1(),
                getScheduleMockup2(),
                getScheduleMockup3(),
                getScheduleMockup4()
        );

        return ScheduleTuteeDto.ResponseScheduleTuteeListDto.builder()
                .results(reservationList)
                .maxResults(4)
                .currentPage(1)
                .totalPages(1)
                .totalElements(4L)
                .build();
    }

    public static ScheduleTuteeDto.ResponseReservationDto getReseponseReservedDetailMockup() {
        return ScheduleTuteeDto.ResponseReservationDto.builder()
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
                .cancelEnableTime(30)
                .minTuteeLackCancelTime(20)
                .klassPublicMemo("klass public note, 강의에서 설명해주는 내용, 클래스 설명")
                .schedulePublicMemo("schedule public note, 일정에서 설명해주는 내용, 해당 일정 설명")
                .ticketName("강강강 강의")
                .availableReservationCount(30)
                .remainReservationCount(30)
                .totalReservationCount(60)
                .startDt(TimeFunction.toUnixTime(ZonedDateTime.of(LocalDate.of(2023, 12, 30), LocalTime.now().minusHours(3), ZoneId.of("Asia/Seoul"))))
                .endDt(TimeFunction.toUnixTime(ZonedDateTime.of(LocalDate.of(2023, 12, 30), LocalTime.now().minusHours(2), ZoneId.of("Asia/Seoul"))))
                .build();
    }
}
