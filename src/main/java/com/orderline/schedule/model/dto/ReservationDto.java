package com.orderline.schedule.model.dto;


import com.orderline.basic.utils.TimeFunction;
import com.orderline.branch.enums.ConnectionTypeEnum;
import com.orderline.klass.enums.KlassColorTypeEnum;
import com.orderline.klass.enums.KlassLevelTypeEnum;
import com.orderline.ticket.model.dto.TicketDto;
import com.orderline.ticket.model.entity.Ticket;
import io.swagger.annotations.ApiModelProperty;
import com.orderline.branch.model.entity.Branch;
import com.orderline.common.user.model.entity.User;
import com.orderline.klass.model.entity.Klass;
import com.orderline.schedule.enums.ReservationStatusTypeEnum;
import com.orderline.schedule.model.entity.Reservation;
import com.orderline.schedule.model.entity.Schedule;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.orderline.basic.utils.TimeFunction.toUnixTime;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationDto {

    @Getter
    @Builder
    public static class ResponseReservationDto {

        @ApiModelProperty(value = "예약 id", example = "1")
        private Long reservationId;

        @ApiModelProperty(value = "지점 id", example = "1")
        private Long branchId;

        @ApiModelProperty(value = "클래스 id", example = "1")
        private Long klassId;

        @ApiModelProperty(value = "일정 id", example = "1")
        private Long scheduleId;

        @ApiModelProperty(value = "수강권 id")
        private Long ticketId;

        @ApiModelProperty(value = "수강권 id")
        private Long repeatReservationId;

        @ApiModelProperty(value = "신청 회원 id", example = "1")
        private Long tuteeId;

        @ApiModelProperty(value = "신청 회원 명")
        private String tuteeName;

        @ApiModelProperty(value = "신청 인원", example = "100")
        private Integer tuteeNum;

        @ApiModelProperty(value = "수강권 차감 횟수", example = "20")
        private Integer reservationCount;

        @ApiModelProperty(value = "예약 상태(대기, 확정, 출석, 결석, 취소)", example = "CONFIRMATION")
        private ReservationStatusTypeEnum statusType;

        @ApiModelProperty(value = "공개 메모", example = "모두에게 보여지는 메모입니다.")
        private String publicMemo;

        @ApiModelProperty(value = "취소 여부")
        private Boolean cancelYn;

        public static ResponseReservationDto toDto(final Reservation reservation) {
            return ResponseReservationDto.builder()
                    .reservationId(reservation.getId())
                    .branchId(reservation.getBranch().getId())
                    .klassId(reservation.getKlass().getId())
                    .scheduleId(reservation.getSchedule().getId())
                    .ticketId(reservation.getTicket().getId())
                    .tuteeId(reservation.getTutee().getId())
                    .repeatReservationId(reservation.getRepeatReservation().getId())
                    .tuteeName(reservation.getTutee().getName())
                    .tuteeNum(reservation.getTuteeNum())
                    .reservationCount(reservation.getReservationCount())
                    .statusType(reservation.getStatusType())
                    .publicMemo(reservation.getPublicMemo())
                    .cancelYn(reservation.getCancelYn())
                    .build();
        }

    }

    @Getter
    @Builder
    public static class ResponseReservationTuteeDto {

        @ApiModelProperty(value = "예약 id", example = "1")
        private Long reservationId;

        @ApiModelProperty(value = "신청 회원 id", example = "1")
        private Long tuteeId;

        @ApiModelProperty(value = "신청 회원 명")
        private String tuteeName;

        @ApiModelProperty(value = "수강생 휴대폰 번호")
        private String phone;

        @ApiModelProperty(value = "수강생 생일")
        private LocalDate birthDate;

        @ApiModelProperty(value = "수강생 성별")
        private String gender;

        @ApiModelProperty(value = "수강생 연결 상태")
        private ConnectionTypeEnum connectionType;

        @ApiModelProperty(value = "신청 인원", example = "100")
        private Integer tuteeNum;

        @ApiModelProperty(value = "수강권 차감 횟수", example = "20")
        private Integer reservationCount;

        @ApiModelProperty(value = "예약 상태(대기, 확정, 출석, 결석, 취소)", example = "확정")
        private ReservationStatusTypeEnum statusType;

        @ApiModelProperty(value = "취소 여부")
        private Boolean cancelYn;

        @ApiModelProperty(value = "ticket 정보" )
        private TicketDto.ResponseTicketDto ticketDto;

        public static ResponseReservationTuteeDto toDto(Reservation reservation, ConnectionTypeEnum connectionTypeEnum){
            return ResponseReservationTuteeDto.builder()
                    .reservationId(reservation.getId())
                    .tuteeId(reservation.getTutee().getId())
                    .tuteeName(reservation.getTutee().getName())
                    .phone(reservation.getTutee().getPhone())
                    .birthDate(reservation.getTutee().getBirthDate())
                    .gender(reservation.getTutee().getGender())
                    .connectionType(connectionTypeEnum)
                    .tuteeNum(reservation.getTuteeNum())
                    .reservationCount(reservation.getReservationCount())
                    .statusType(reservation.getStatusType())
                    .cancelYn(reservation.getCancelYn())
                    .ticketDto(TicketDto.ResponseTicketDto.toDto(reservation.getTicket()))
                    .build();
        }
    }

    @Builder
    @Getter
    public static class ResponseReservationListDto {

        @ApiModelProperty(value = "예약 리스트")
        private List<ReservationDto.ResponseReservationDto> results;

        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static ReservationDto.ResponseReservationListDto build(Page<ReservationDto.ResponseReservationDto> responseDtoPage, Integer currentPage, Integer maxResults){
            return ReservationDto.ResponseReservationListDto.builder()
                    .results(responseDtoPage.getContent())
                    .maxResults(maxResults)
                    .currentPage(currentPage)
                    .totalPages(responseDtoPage.getTotalPages())
                    .totalElements(responseDtoPage.getTotalElements())
                    .build();
        }
    }

    @Builder
    @Getter
    public static class ResponseReservationTuteeListDto {

        @ApiModelProperty(value = "예약 회원 리스트")
        private List<ReservationDto.ResponseReservationTuteeDto> results;

        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static ReservationDto.ResponseReservationTuteeListDto build(Page<ReservationDto.ResponseReservationTuteeDto> responseDtoPage, Integer currentPage, Integer maxResults){
            return ReservationDto.ResponseReservationTuteeListDto.builder()
                    .results(responseDtoPage.getContent())
                    .maxResults(maxResults)
                    .currentPage(currentPage)
                    .totalPages(responseDtoPage.getTotalPages())
                    .totalElements(responseDtoPage.getTotalElements())
                    .build();
        }
    }

    @Builder
    @Getter
    public static class ResponseTuteeReservationDto{

        @ApiModelProperty(value = "예약 id", example = "1")
        private Long reservationId;

        @ApiModelProperty(value = "일정 id", example = "1")
        private Long scheduleId;

        @ApiModelProperty(value = "클래스 id", example = "1")
        private Long klassId;

        @ApiModelProperty(value = "클래스 명", example = "1")
        private String klassName;

        @ApiModelProperty(value = "일정 시작 시각", example = "1701241200")
        private Long startDt;

        @ApiModelProperty(value = "일정 종료 시각", example = "1701244800")
        private Long endDt;

        @ApiModelProperty(value = "예약 상태(대기, 확정, 출석, 결석, 취소)", example = "CONFIRMATION")
        private ReservationStatusTypeEnum statusType;

        @ApiModelProperty(value = "서명 위치")
        private String signUrl;

        @ApiModelProperty(value = "회원 수강권")
        private Long ticketId;

        @ApiModelProperty(value = "회원 수강권")
        private TicketDto.ResponseTicketDto ticketDto;

        public static ReservationDto.ResponseTuteeReservationDto toDto(final Reservation reservation) {
            Schedule schedule = reservation.getSchedule();
            return ResponseTuteeReservationDto.builder()
                    .reservationId(reservation.getId())
                    .scheduleId(schedule.getId())
                    .klassId(schedule.getKlass().getId())
                    .klassName(schedule.getKlassName())
                    .startDt(toUnixTime(schedule.getStartDt()))
                    .endDt(toUnixTime(schedule.getEndDt()))
                    .statusType(reservation.getStatusType())
                    .ticketId(reservation.getTicket().getId())
                    .ticketDto(TicketDto.ResponseTicketDto.toDto(reservation.getTicket()))
                    .build();
        }
    }

    @Builder
    @Getter
    public static class ResponseTuteeReservationListDto{

        @ApiModelProperty(value = "예약 리스트")
        private List<ReservationDto.ResponseTuteeReservationDto> results;

        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static ReservationDto.ResponseTuteeReservationListDto build(Page<ReservationDto.ResponseTuteeReservationDto> responseDtoPage, Integer currentPage, Integer maxResults){
            return ReservationDto.ResponseTuteeReservationListDto.builder()
                    .results(responseDtoPage.getContent())
                    .maxResults(maxResults)
                    .currentPage(currentPage)
                    .totalPages(responseDtoPage.getTotalPages())
                    .totalElements(responseDtoPage.getTotalElements())
                    .build();
        }
    }
    @Builder
    @Getter
    public static class ResponseMonthlyTuteeReservationDto{

        @ApiModelProperty(value = "월별", example = "2023년 12월")
        private String month;

        private List<ReservationDto.ResponseTuteeReservationDto> reservations;

        public static List<ResponseMonthlyTuteeReservationDto> toDto(Map<String, List<ResponseTuteeReservationDto>> reservationMap) {
            List<ResponseMonthlyTuteeReservationDto> responseMonthlyTuteeReservationDtoList = new ArrayList<>();
            reservationMap.forEach(
                (key, value) -> {
                    responseMonthlyTuteeReservationDtoList.add(
                     ReservationDto.ResponseMonthlyTuteeReservationDto.builder()
                        .month(key)
                        .reservations(value)
                        .build());
                }
            );
            return responseMonthlyTuteeReservationDtoList;
        }
    }

    @Builder
    @Getter
    public static class ResponseMonthlyTuteeReservationListDto{

        @ApiModelProperty(value = "예약 리스트")
        private List<ReservationDto.ResponseMonthlyTuteeReservationDto> results;

        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static ReservationDto.ResponseMonthlyTuteeReservationListDto build(Page<ReservationDto.ResponseTuteeReservationDto> responseDtoPage, Integer currentPage, Integer maxResults){

            Map<String, List<ResponseTuteeReservationDto>> reservationMap
                    = responseDtoPage.stream().collect(Collectors.groupingBy(reservationItem -> TimeFunction.toZonedDateTime(reservationItem.getStartDt()).format(DateTimeFormatter.ofPattern("yyyy년 MM월")), LinkedHashMap::new, Collectors.toList()));
            List<ReservationDto.ResponseMonthlyTuteeReservationDto> monthlyTuteeReservationDtoList = ReservationDto.ResponseMonthlyTuteeReservationDto.toDto(reservationMap);

            return ReservationDto.ResponseMonthlyTuteeReservationListDto.builder()
                    .results(monthlyTuteeReservationDtoList)
                    .maxResults(maxResults)
                    .currentPage(currentPage)
                    .totalPages(responseDtoPage.getTotalPages())
                    .totalElements(responseDtoPage.getTotalElements())
                    .build();
        }
    }

    @Builder
    @Getter
    public static class ResponseTuteeReservationByTicketDto{

        @ApiModelProperty(value = "티켓")
        private TicketDto.ResponseTicketDto ticketDto;

        private List<ReservationDto.ResponseTuteeReservationDto> reservations;

        public static List<ResponseTuteeReservationByTicketDto> toDto(Map<Long, List<ResponseTuteeReservationDto>> reservationMap){
            List<ResponseTuteeReservationByTicketDto> responseTuteeReservationByTicketDtoList = new ArrayList<>();
            reservationMap.forEach(
                    (key, value) -> responseTuteeReservationByTicketDtoList.add(
                            ResponseTuteeReservationByTicketDto.builder()
                                    .ticketDto(value.get(0).getTicketDto())
                                    .reservations(value)
                                    .build())
            );
            return responseTuteeReservationByTicketDtoList;
        }
    }

    @Builder
    @Getter
    public static class ResponseTuteeReservationByTicketListDto {

        @ApiModelProperty(value = "예약 리스트")
        private List<ReservationDto.ResponseTuteeReservationByTicketDto> results;

        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static ReservationDto.ResponseTuteeReservationByTicketListDto build(Page<ReservationDto.ResponseTuteeReservationDto> responseDtoPage, Integer currentPage, Integer maxResults) {
            Map<Long, List<ResponseTuteeReservationDto>> reservationMap
                    = responseDtoPage.stream().collect(Collectors.groupingBy(ResponseTuteeReservationDto::getTicketId, LinkedHashMap::new, Collectors.toList()));
            List<ReservationDto.ResponseTuteeReservationByTicketDto> tuteeReservationByTicketDtoList = ReservationDto.ResponseTuteeReservationByTicketDto.toDto(reservationMap);

            return ReservationDto.ResponseTuteeReservationByTicketListDto.builder()
                    .results(tuteeReservationByTicketDtoList)
                    .maxResults(maxResults)
                    .currentPage(currentPage)
                    .totalPages(responseDtoPage.getTotalPages())
                    .totalElements(responseDtoPage.getTotalElements())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ResponseAttendanceReservationDto {

        @ApiModelProperty(value = "예약 id", example = "1")
        private Long reservationId;

        @ApiModelProperty(value = "지점 id", example = "1")
        private Long branchId;

        @ApiModelProperty(value = "클래스 id", example = "1")
        private Long klassId;

        @ApiModelProperty(value = "일정 id", example = "1")
        private Long scheduleId;

        @ApiModelProperty(value = "수강권 id")
        private Long ticketId;

        @ApiModelProperty(value = "신청 회원 id", example = "1")
        private Long tuteeId;

        @ApiModelProperty(value = "신청 인원", example = "1")
        private Integer tuteeNum;

        @ApiModelProperty(value = "수강권 차감 횟수", example = "20")
        private Integer reservationCount;

        @ApiModelProperty(value = "예약 상태(대기, 확정, 출석, 결석, 취소)", example = "CONFIRMATION")
        private ReservationStatusTypeEnum statusType;

        @ApiModelProperty(value = "신청 회원 명")
        private String tuteeName;

        @ApiModelProperty(value = "클래스 명")
        private String klassName;

        @ApiModelProperty(value = "클래스 색상")
        private KlassColorTypeEnum colorCdType;

        @ApiModelProperty(value = "일정 시작 시각")
        private Long startDt;

        @ApiModelProperty(value = "일정 종료 시각")
        private Long endDt;

        @ApiModelProperty(value = "메인 강사명")
        private String mainTutorName;

        @ApiModelProperty(value = "현재 예약 인원")
        private Integer currentTuteeNum;

        @ApiModelProperty(value = "정원")
        private Integer maxTuteeNum;

        @ApiModelProperty(value = "난이도")
        private KlassLevelTypeEnum level;

        public static ReservationDto.ResponseAttendanceReservationDto toDto(final Reservation reservation) {
            return ReservationDto.ResponseAttendanceReservationDto.builder()
                    .reservationId(reservation.getId())
                    .branchId(reservation.getBranch().getId())
                    .klassId(reservation.getKlass().getId())
                    .scheduleId(reservation.getSchedule().getId())
                    .ticketId(reservation.getTicket().getId())
                    .tuteeId(reservation.getTutee().getId())
                    .tuteeNum(reservation.getTuteeNum())
                    .reservationCount(reservation.getReservationCount())
                    .statusType(reservation.getStatusType())
                    .tuteeName(reservation.getTutee().getName())
                    .klassName(reservation.getKlass().getKlassName())
                    .colorCdType(reservation.getKlass().getColorCdType())
                    .startDt(reservation.getSchedule().getStartDt().toEpochSecond())
                    .endDt(reservation.getSchedule().getEndDt().toEpochSecond())
                    .mainTutorName(reservation.getSchedule().getMainTutor().getName())
                    .currentTuteeNum(reservation.getSchedule().getCurrentTuteeNum())
                    .maxTuteeNum(reservation.getSchedule().getMaxTuteeNum())
                    .level(reservation.getSchedule().getLevel())
                    .build();
        }
    }

    @Builder
    @Getter
    public static class RequestReservationDto {
        @NotNull(message = "수업을 입력해주세요.")
        @ApiModelProperty(value = "클래스 ID", example = "1")
        private Long klassId;

        @NotNull(message = "일정을 입력해주세요.")
        @ApiModelProperty(value = "일정 ID", example = "1")
        private Long scheduleId;

        @ApiModelProperty(value = "수강권 ID")
        private Long ticketId;

        @NotNull(message = "신청 회원 입력해주세요.")
        @ApiModelProperty(value = "신청 회원 ID", example = "1")
        private Long tuteeId;

        @ApiModelProperty(value = "예약 상태", example = "CONFIRMATION")
        private ReservationStatusTypeEnum statusType;

        @ApiModelProperty(value = "공개 메모", example = "공개 메모 입니다")
        private String publicMemo;

        public Reservation toEntity(Branch branch, Klass klass, Schedule schedule, Ticket ticket, User tutee){
            return Reservation.builder()
                    .branch(branch)
                    .klass(klass)
                    .schedule(schedule)
                    .ticket(ticket)
                    .tutee(tutee)
                    .reservationCount(klass.getReservationCount())
                    .statusType(statusType)
                    .publicMemo(publicMemo)
                    .build();
        }
    }

    @Builder
    @Getter
    public static class RequestUpdateReservationAttendanceStatusDto{

        @NotNull(message = "출석 상태를 입력해주세요.")
        @ApiModelProperty(value = "출석 상태", example = "ATTENDANCE")
        private ReservationStatusTypeEnum statusType;

        @NotNull(message = "출석 상태를 변경할 예약 id 리스트를 입력해주세요.")
        @ApiModelProperty(value = "예약 리스트")
        private List<Long> reservationIdList;

    }
}
