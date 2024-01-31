package com.ptglue.schedule.model.dto;

import com.ptglue.basic.utils.TimeFunction;
import com.ptglue.klass.enums.KlassColorTypeEnum;
import com.ptglue.klass.enums.KlassLevelTypeEnum;
import com.ptglue.schedule.enums.ScheduleMemberTypeEnum;
import com.ptglue.schedule.enums.ReservationStatusTypeEnum;
import com.ptglue.schedule.enums.ScheduleStatusTypeEnum;
import com.ptglue.schedule.model.entity.Reservation;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationTuteeDto {
    @Getter
    @Builder
    public static class RequestReservationTuteeDto {
        @ApiModelProperty(value = "일정 ID", notes = "schedule테이블, 일정 ID", example = "1")
        private Long scheduleId;

        @ApiModelProperty(value = "수강권 ID", notes = "ticket테이블, 수강권 ID", example = "1")
        private Long ticketId;

        @ApiModelProperty(value = "강의 ID", notes = "klass테이블, 강의 ID", example = "1")
        private Long klassId;

        @ApiModelProperty(value = "수강권 차감 횟수", example = "1")
        private Integer reservationCount;
    }


    @Getter
    @Builder
    public static class ResponseReservationDto {
        @ApiModelProperty(value = "예약 id", notes = "reservation테이블, ID")
        private Long reservationId;

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

//        ui에 표현해주기 애매모호한 상황이라 일단은 주석처리.
//        @ApiModelProperty(value = "예약 설명", notes = "reservation테이블, 강사가 해당 예약 관련하여 설명 해주는 메모")
//        private String reservationPublicMemo;

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

        public static ReservationTuteeDto.ResponseReservationDto toReservationDto(final Reservation reservation) {
            return ResponseReservationDto.builder()
                    .reservationId(reservation.getId())
                    .klassName(reservation.getSchedule().getKlass().getKlassName())
                    .mainTutorNickName(reservation.getSchedule().getMainTutorName())
                    .subTutorNickName(reservation.getSchedule().getSubTutorName())
                    .reservationStatusType(reservation.getStatusType())
                    .scheduleMemberType(reservation.getSchedule().getScheduleMemberType())
                    .level(reservation.getSchedule().getLevel())
                    .currentTuteeNum(reservation.getSchedule().getCurrentTuteeNum())
                    .maxTuteeNum(reservation.getSchedule().getMaxTuteeNum())
                    .minTuteeNum(reservation.getSchedule().getMinTuteeNum())
                    .waitingCurrentTuteeNum(reservation.getSchedule().getWaitingCurrentTuteeNum())
                    .waitingTuteeNum(reservation.getSchedule().getKlass().getWaitingTuteeNum())
                    .cancelEnableTime(reservation.getSchedule().getCancelEnableTime())
                    .minTuteeLackCancelTime(reservation.getSchedule().getMinTuteeLackCancelTime())
                    .klassPublicMemo(reservation.getSchedule().getKlass().getPublicMemo())
                    .schedulePublicMemo(reservation.getSchedule().getPublicMemo())
//                    .reservationPublicMemo(reservation.getPublicMemo())
                    .ticketName(reservation.getTicket().getTicketName())
                    .availableReservationCount(reservation.getTicket().getAvailableReservationCount())
                    .remainReservationCount(reservation.getTicket().getRemainReservationCount())
                    .startDt(TimeFunction.toUnixTime(reservation.getSchedule().getStartDt()))
                    .endDt(TimeFunction.toUnixTime(reservation.getSchedule().getEndDt()))
                    .totalReservationCount(reservation.getTicket().getTotalReservationCount())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ResponseReservationTuteeDto{
        @ApiModelProperty(value = "예약 ID", notes ="reservation테이블, 예약 ID")
        private Long reservationId;

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

        @ApiModelProperty(value = "예약 예약 상태", notes = "schedule테이블, 일정 상태 확인")
        private ReservationStatusTypeEnum reservationStatusTypeEnum;

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

        public static ReservationTuteeDto.ResponseReservationTuteeDto toReservationDto(Reservation reservation) {
            return ReservationTuteeDto.ResponseReservationTuteeDto.builder()
                    .reservationId(reservation.getId())
                    .scheduleId(reservation.getSchedule().getId())
                    .ticketId(reservation.getTicket().getId())
                    .klassId(reservation.getKlass().getId())
                    .klassName(reservation.getKlass().getKlassName())
                    .startDt(TimeFunction.toUnixTime(reservation.getSchedule().getStartDt()))
                    .endDt(TimeFunction.toUnixTime(reservation.getSchedule().getEndDt()))
                    .mainTutorNickName(reservation.getSchedule().getMainTutorName())
                    .subTutorNickName(reservation.getSchedule().getSubTutorName())
                    .scheduleMemberType(reservation.getSchedule().getScheduleMemberType())
                    .colorCdType(reservation.getSchedule().getColorCdType())
                    .level(reservation.getSchedule().getLevel())
                    .ticketName(reservation.getTicket().getTicketName())
                    .cancelEnableTime(reservation.getSchedule().getCancelEnableTime())
                    .currentTuteeNum(reservation.getSchedule().getCurrentTuteeNum())
                    .maxTuteeNum(reservation.getSchedule().getMaxTuteeNum())
                    .reservationEnableYn(reservation.getSchedule().getReservationEnableYn())
                    .minTuteeLackCancelTime(reservation.getSchedule().getMinTuteeLackCancelTime())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ResponseReservationTuteeListDto {
        @ApiModelProperty(value = "예약 리스트")
        private List<ReservationTuteeDto.ResponseReservationTuteeDto> results;

        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static ReservationTuteeDto.ResponseReservationTuteeListDto build(Page<ReservationTuteeDto.ResponseReservationTuteeDto> responseDtoPage, Integer currentPage, Integer maxResults) {
            return ReservationTuteeDto.ResponseReservationTuteeListDto.builder()
                    .results(responseDtoPage.getContent())
                    .maxResults(maxResults)
                    .currentPage(currentPage)
                    .totalPages(responseDtoPage.getTotalPages())
                    .totalElements(responseDtoPage.getTotalElements())
                    .build();
        }
    }
}
