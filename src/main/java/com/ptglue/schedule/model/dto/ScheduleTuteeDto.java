package com.ptglue.schedule.model.dto;

import com.ptglue.basic.utils.TimeFunction;
import com.ptglue.schedule.enums.ScheduleMemberTypeEnum;
import com.ptglue.schedule.enums.ScheduleStatusTypeEnum;
import com.ptglue.ticket.model.entity.Ticket;
import com.ptglue.klass.enums.KlassColorTypeEnum;
import com.ptglue.klass.enums.KlassLevelTypeEnum;
import com.ptglue.schedule.enums.ReservationStatusTypeEnum;
import com.ptglue.schedule.model.entity.Reservation;
import com.ptglue.schedule.model.entity.Schedule;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.domain.Page;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScheduleTuteeDto {
    @Getter
    @Builder
    public static class ResponseReservationTuteeByTicketIdDto {
        @ApiModelProperty(value = "예약 상태")
        private ReservationStatusTypeEnum statusType;

        @ApiModelProperty(value = "강의 이름")
        private String klassName;

        @ApiModelProperty(value = "일정 시작 일자", notes = "시작 일자")
        private Long startDt;

        @ApiModelProperty(value = "일정 종료 일자", notes = "종료 일자")
        private Long endDt;

        public static ResponseReservationTuteeByTicketIdDto toDto(Reservation reservation) {
            return ResponseReservationTuteeByTicketIdDto.builder()
                    .statusType(reservation.getStatusType())
                    .klassName(reservation.getKlass().getKlassName())
                    .startDt(TimeFunction.toUnixTime(reservation.getSchedule().getStartDt()))
                    .endDt(TimeFunction.toUnixTime(reservation.getSchedule().getEndDt()))
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ResponseReservationTuteeListByTicketIdDto {
        @ApiModelProperty(value = "목록 리스트")
        private List<ScheduleTuteeDto.ResponseReservationTuteeByTicketIdDto> results;

        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static ScheduleTuteeDto.ResponseReservationTuteeListByTicketIdDto build(Page<ScheduleTuteeDto.ResponseReservationTuteeByTicketIdDto> responseDtoPage, Integer currentPage, Integer maxResults) {
            return ScheduleTuteeDto.ResponseReservationTuteeListByTicketIdDto.builder()
                    .results(responseDtoPage.getContent())
                    .maxResults(maxResults)
                    .currentPage(currentPage)
                    .totalPages(responseDtoPage.getTotalPages())
                    .totalElements(responseDtoPage.getTotalElements())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ResponseTuteeAbsenceReservationCount{
        @ApiModelProperty(value = "결석 횟수")
        private Integer absenceReservationCount;

        public static ScheduleTuteeDto.ResponseTuteeAbsenceReservationCount toDto(Integer absenceReservationCount) {
            return ResponseTuteeAbsenceReservationCount.builder()
                    .absenceReservationCount(absenceReservationCount)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ResponseScheduleTuteeDto{
        @ApiModelProperty(value = "일정 ID", notes ="schedule테이블, 일정 ID")
        private Long scheduleId;

        @ApiModelProperty(value = "수강권 ID", notes ="ticket테이블, 수강권 ID")
        private Long ticketId;

        @ApiModelProperty(value = "클래스 ID", notes ="klass테이블, 강의 ID")
        private Long klassId;

        @ApiModelProperty(value = "클래스 이름", notes ="klass테이블, 클래스명")
        private String klassName;

        @ApiModelProperty(value = "예약 시작 일자", notes = "schedule테이블, 예약 시작 일자")
        private Long startDt;

        @ApiModelProperty(value = "예약 종료 일자", notes = "schedule테이블, 예약 종료 일자")
        private Long endDt;

        @ApiModelProperty(value = "담당 강사 별명", notes = "klass테이블, 담당 강사 nickname이 없으면 name, 있으면 nickname")
        private String mainTutorNickName;

        @ApiModelProperty(value = "보조 강사 별명", notes = "klass테이블, 보조 강사 nickname이 없으면 name, 있으면 nickname")
        private String subTutorNickName;

        @ApiModelProperty(value = "일정 예약 상태", notes = "reservation테이블, 예약 상태 확인")
        private ScheduleStatusTypeEnum scheduleStatusTypeEnum;

        @ApiModelProperty(value = "수업 인원 타입(개인수업, 사전개설형)", notes = "klass 테이블, max_tutee_num이 1이면 개인수업 초과면 단체수업")
        private ScheduleMemberTypeEnum scheduleMemberType;

        @ApiModelProperty(value = "색상 종류", notes = "schedule 테이블, 색상")
        private KlassColorTypeEnum colorCdType;

        @ApiModelProperty(value = "수업 난이도", notes = "klass테이블, 수업 난이도")
        private KlassLevelTypeEnum level;

        @ApiModelProperty(value = "수강권 이름",notes = "ticket테이블, 수강권 이름")
        private String ticketName;

        @ApiModelProperty(value = "취소 가능 시간",notes = "schedule테이블, 취소 가능 시간")
        private Integer cancelEnableTime;

        @ApiModelProperty(value = "예약한 회원의 수", notes = "schedule테이블, 예약한 회원의 수")
        private Integer currentTuteeNum;

        @ApiModelProperty(value = "정원", notes = "schedule테이블, 참여 가능 인원")
        private Integer maxTuteeNum;

        @ApiModelProperty(value = "예약 허용 여부", notes = "schedule테이블, 예약 허용 여부")
        private Boolean reservationEnableYn;

        @ApiModelProperty(value = "최소 정원 미 충족시 자동 취소 시간")
        private Integer minTuteeLackCancelTime;

        public static ScheduleTuteeDto.ResponseScheduleTuteeDto toScheduleDto(Schedule schedule, Ticket ticket) {
            return ResponseScheduleTuteeDto.builder()
                    .scheduleId(schedule.getId())
                    .ticketId(ticket.getId())
                    .klassId(schedule.getKlass().getId())
                    .klassName(schedule.getKlass().getKlassName())
                    .startDt(TimeFunction.toUnixTime(schedule.getStartDt()))
                    .endDt(TimeFunction.toUnixTime(schedule.getEndDt()))
                    .mainTutorNickName(schedule.getMainTutorName())
                    .subTutorNickName(schedule.getSubTutorName())
                    .scheduleMemberType(schedule.getScheduleMemberType())
                    .colorCdType(schedule.getColorCdType())
                    .level(schedule.getLevel())
                    .ticketName(ticket.getTicketName())
                    .cancelEnableTime(schedule.getCancelEnableTime())
                    .currentTuteeNum(schedule.getCurrentTuteeNum())
                    .maxTuteeNum(schedule.getMaxTuteeNum())
                    .reservationEnableYn(schedule.getReservationEnableYn())
                    .minTuteeLackCancelTime(schedule.getMinTuteeLackCancelTime())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ResponseScheduleTuteeListDto {
        @ApiModelProperty(value = "예약 리스트")
        private List<ScheduleTuteeDto.ResponseScheduleTuteeDto> results;

        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static ScheduleTuteeDto.ResponseScheduleTuteeListDto build(Page<ScheduleTuteeDto.ResponseScheduleTuteeDto> responseDtoPage, Integer currentPage, Integer maxResults) {
            return ScheduleTuteeDto.ResponseScheduleTuteeListDto.builder()
                    .results(responseDtoPage.getContent())
                    .maxResults(maxResults)
                    .currentPage(currentPage)
                    .totalPages(responseDtoPage.getTotalPages())
                    .totalElements(responseDtoPage.getTotalElements())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ResponseReservationDto {
//        일정 클릭시 나오는 상세 목록
        @ApiModelProperty(value = "일정 id", notes = "schedule테이블, ID")
        private Long scheduleId;

        @ApiModelProperty(value = "수강권 id", notes = "ticket테이블, ID")
        private Long ticketId;

        @ApiModelProperty(value = "클래스 이름", notes ="klass테이블, 클래스명")
        private String klassName;

        @ApiModelProperty(value = "담당 강사 별명", notes = "klass테이블, 담당 강사 별명")
        private String mainTutorNickName;

        @ApiModelProperty(value = "보조 강사 별명", notes = "klass테이블, 보조 강사 별명")
        private String subTutorNickName;

        @ApiModelProperty(value = "예약 상태(대기, 확정, 출석, 결석, 취소)", notes = "reservation테이블, 예약 상태(대기, 승낙, 확정)")
        private ReservationStatusTypeEnum reservationStatusType;

        @ApiModelProperty(value = "일정 상태(접수중, 대기접수, 접수완료, 수업중, 종료, 취소)", notes = "schedule테이블, 예약 상태(접수중, 대기접수, 접수완료, 수업중, 종료, 취소)")
        private ScheduleStatusTypeEnum scheduleStatusType;

        @ApiModelProperty(value = "수업 인원 타입(개인수업, 사전개설형)", notes = "klass 테이블, max_tutee_num이 1이면 개인수업 초과면 단체수업")
        private ScheduleMemberTypeEnum scheduleMemberType;

        @ApiModelProperty(value = "수업 난이도", notes = "klass테이블, 수업 난이도")
        private KlassLevelTypeEnum level;

        @ApiModelProperty(value = "예약한 회원의 수", notes = "schedule테이블, 예약한 회원의 수")
        private Integer currentTuteeNum;

        @ApiModelProperty(value = "최대 정원", notes = "schedule테이블, 참여 가능 인원")
        private Integer maxTuteeNum;

        @ApiModelProperty(value = "최소 정원", notes = "schedule테이블, 강의 개설 최소의 조건")
        private Integer minTuteeNum;

        @ApiModelProperty(value = "대기 예약 회원 수", notes = "schedule테이블, 예약 대기중인 회원 수")
        private Integer waitingCurrentTuteeNum;

        @ApiModelProperty(value = "대기 예약 허용 정원", notes = "klass테이블, 예약대기 할 수 있는 최대 정원")
        private Integer waitingTuteeNum;

        @ApiModelProperty(value = "취소 가능 시간", notes = "schedule테이블, 취소 가능 한 시간")
        private Integer cancelEnableTime;

        @ApiModelProperty(value = "최소 정원 미 충족시 자동 취소 시간", notes = "schedule테이블, 최소 정원이 되지 못하면 취소되는 시간")
        private Integer minTuteeLackCancelTime;

        @ApiModelProperty(value = "클래스 설명", notes = "klass테이블, 클래스 설명")
        private String klassPublicMemo;

        @ApiModelProperty(value = "일정 설명", notes = "schedule테이블, 일정 설명")
        private String schedulePublicMemo;

        @ApiModelProperty(value = "수강권 이름", notes = "ticket테이블, 수강권 이름")
        private String ticketName;

        @ApiModelProperty(value = "수강권 이름", notes = "ticket테이블, 수강권 등록 가능한 횟수")
        private Integer availableReservationCount;

        @ApiModelProperty(name = "잔여 예약 횟수", notes = "ticket테이블, 수강권 사용 가능한 횟수")
        private Integer remainReservationCount;

        @ApiModelProperty(value = "예약 시작 일자", notes = "ticket테이블, 예약 시작 일자")
        private Long startDt;

        @ApiModelProperty(value = "예약 종료 일자", notes = "ticket테이블, 예약 종료 일자")
        private Long endDt;

        @ApiModelProperty(value = "총 예약 가능한 횟수", notes = "ticket테이블, 예약 가능한 횟수")
        private Integer totalReservationCount;

        public static ScheduleTuteeDto.ResponseReservationDto toScheduleDto(final Schedule schedule, Ticket ticket) {
            return ScheduleTuteeDto.ResponseReservationDto.builder()
                    .scheduleId(schedule.getId())
                    .ticketId(ticket.getId())
                    .klassName(schedule.getKlass().getKlassName())
                    .mainTutorNickName(schedule.getMainTutorName())
                    .subTutorNickName(schedule.getSubTutorName())
                    .scheduleStatusType(schedule.getStatusType())
                    .level(schedule.getLevel())
                    .scheduleMemberType(schedule.getScheduleMemberType())
                    .currentTuteeNum(schedule.getCurrentTuteeNum())
                    .maxTuteeNum(schedule.getMaxTuteeNum())
                    .minTuteeNum(schedule.getMinTuteeNum())
                    .waitingCurrentTuteeNum(schedule.getWaitingCurrentTuteeNum())
                    .waitingTuteeNum(schedule.getKlass().getWaitingTuteeNum())
                    .cancelEnableTime(schedule.getCancelEnableTime())
                    .minTuteeLackCancelTime(schedule.getMinTuteeLackCancelTime())
                    .klassPublicMemo(schedule.getKlass().getPublicMemo())
                    .schedulePublicMemo(schedule.getPublicMemo())
                    .ticketName(ticket.getTicketName())
                    .availableReservationCount(ticket.getAvailableReservationCount())
                    .remainReservationCount(ticket.getRemainReservationCount())
                    .startDt(TimeFunction.toUnixTime(schedule.getStartDt()))
                    .endDt(TimeFunction.toUnixTime(schedule.getEndDt()))
                    .totalReservationCount(ticket.getTotalReservationCount())
                    .build();
        }
    }
}
