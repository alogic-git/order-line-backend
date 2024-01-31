package com.orderline.schedule.model.dto;

import com.orderline.basic.model.dto.CommonDto;
import com.orderline.basic.utils.TimeFunction;
import com.orderline.klass.enums.KlassLevelTypeEnum;
import com.orderline.klass.model.dto.KlassDto;
import com.orderline.schedule.enums.ScheduleTypeEnum;
import com.orderline.schedule.model.entity.Schedule;
import io.swagger.annotations.ApiModelProperty;

import com.orderline.branch.model.entity.Branch;
import com.orderline.common.user.model.entity.User;
import com.orderline.klass.model.entity.Klass;
import com.orderline.schedule.enums.ScheduleStatusTypeEnum;
import com.orderline.schedule.model.entity.RepeatSchedule;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;

import java.time.LocalDate;

import java.util.*;
import java.util.stream.Collectors;

import java.time.ZonedDateTime;
import java.util.List;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScheduleDto {

    @Getter
    @Builder
    public static class ResponseScheduleDto{
        @ApiModelProperty(value = "스케쥴 id")
        private Long scheduleId;

        @ApiModelProperty(value = "지점 id")
        private Long branchId;

        @ApiModelProperty(value = "클래스 id")
        private Long klassId;

        @ApiModelProperty(value = "담당 강사 id")
        private Long mainTutorId;

        @ApiModelProperty(value = "보조 강사 id")
        private Long subTutorId;
        
        @ApiModelProperty(value = "클래스 명")
        private String klassName;

        @ApiModelProperty(value = "시작 시각")
        private Long startDt;

        @ApiModelProperty(value = "종료 시각")
        private Long endDt;

        @ApiModelProperty(value = "최소 정원")
        private Integer minTuteeNum;

        @ApiModelProperty(value = "최대 정원")
        private Integer maxTuteeNum;

        @ApiModelProperty(value = "예약 회원 수")
        private Integer currentTuteeNum;

        @ApiModelProperty(value = "대기 예약 회원 수")
        private Integer waitingCurrentTuteeNum;

        @ApiModelProperty(value = "예약 상태")
        private ScheduleStatusTypeEnum statusType;

        @ApiModelProperty(value = "공개 상태(비공개/공개)")
        private Boolean privateYn;

        @ApiModelProperty(value = "스케쥴 종류(일반, 휴무, 상담)")
        private ScheduleTypeEnum scheduleType;

        @ApiModelProperty(value = "난이도")
        private KlassLevelTypeEnum level;

        @ApiModelProperty(value = "메인 색상")
        private CommonDto.ColorCodeDto colorCdType;

        @ApiModelProperty(value = "비공개 메모")
        private String privateMemo;

        @ApiModelProperty(value = "공개 메모")
        private String publicMemo;

        @ApiModelProperty(value = "예약 허용 여부")
        private Boolean reservationEnableYn;

        @ApiModelProperty(value = "예약 가능 시간")
        private Integer reservationEnableTime;

        @ApiModelProperty(value = "취소 가능 시간")
        private Integer cancelEnableTime;

        @ApiModelProperty(value = "최소 정원 미 충족시 자동 취소 시간")
        private Integer minTuteeLackCancelTime;

        @ApiModelProperty(value = "취소 여부")
        private Boolean cancelYn;

        public static ResponseScheduleDto toDto(final Schedule schedule){
            return ResponseScheduleDto.builder()
                    .scheduleId(schedule.getId())
                    .branchId(schedule.getBranch().getId())
                    .klassId(schedule.getKlass() == null ? null : schedule.getKlass().getId())
                    .klassName(schedule.getKlassName() == null ? null : schedule.getKlassName())
//                    .repeatScheduleId(schedule.getRepeatSchedule() == null ? null : schedule.getRepeatSchedule().getId())
                    .mainTutorId(schedule.getMainTutor() == null ? null : schedule.getMainTutor().getId())
                    .subTutorId(schedule.getSubTutor() == null ? null : schedule.getSubTutor().getId())
//                    .startDt(schedule.getStartDt())
//                    .endDt(schedule.getEndDt())
                    .startDt(TimeFunction.toUnixTime(schedule.getStartDt()))
                    .endDt(TimeFunction.toUnixTime(schedule.getEndDt()))
                    .minTuteeNum(schedule.getMinTuteeNum() == null ? null : schedule.getMinTuteeNum())
                    .maxTuteeNum(schedule.getMaxTuteeNum() == null ? null : schedule.getMaxTuteeNum())
                    .currentTuteeNum(schedule.getCurrentTuteeNum() == null ? null : schedule.getCurrentTuteeNum())
                    .statusType(schedule.getStatusType() == null ? null : schedule.getStatusType())
                    .privateYn(schedule.getPrivateYn())
                    .level(schedule.getLevel() == null ? null : schedule.getLevel())
                    .colorCdType(schedule.getColorCdType() == null ? null : CommonDto.ColorCodeDto.toDto(schedule.getColorCdType()))
                    .privateMemo(schedule.getPrivateMemo())
                    .publicMemo(schedule.getPublicMemo())
                    .reservationEnableYn(schedule.getReservationEnableYn() == null ? null : schedule.getReservationEnableYn())
                    .reservationEnableTime(schedule.getReservationEnableTime() == null ? null : schedule.getReservationEnableTime())
                    .cancelEnableTime(schedule.getCancelEnableTime() == null ? null : schedule.getCancelEnableTime())
                    .minTuteeLackCancelTime(schedule.getMinTuteeLackCancelTime() == null ? null : schedule.getMinTuteeLackCancelTime())
                    .cancelYn(schedule.getCancelYn() == null ? null : schedule.getCancelYn())
                    .build();
        }

        public static ResponseScheduleDto toDto(final Klass klass, final ZonedDateTime startDt, final ZonedDateTime endDt){
            return ResponseScheduleDto.builder()
                    .branchId(klass.getBranch().getId())
                    .mainTutorId(klass.getMainTutor().getId())
                    .subTutorId(klass.getSubTutor() != null ? klass.getSubTutor().getId() : klass.getMainTutor().getId())
                    .klassId(klass.getId())
                    .klassName(klass.getKlassName())
                    .startDt(TimeFunction.toUnixTime(startDt))
                    .endDt(TimeFunction.toUnixTime(endDt))
                    .minTuteeNum(klass.getMinTuteeNum())
                    .maxTuteeNum(klass.getMaxTuteeNum())
                    .currentTuteeNum(0)
                    .waitingCurrentTuteeNum(0)
                    .statusType(ScheduleStatusTypeEnum.BEING_RECEIPT)
                    .level(klass.getLevel())
                    .colorCdType(CommonDto.ColorCodeDto.toDto(klass.getColorCdType()))
                    .publicMemo("")
                    .reservationEnableTime(klass.getReservationEnableTime())
                    .cancelEnableTime(klass.getCancelEnableTime())
                    .minTuteeLackCancelTime(klass.getMinTuteeLackCancelTime())
                    .build();
        }
    }

    @Builder
    @Getter
    public static class ResponseScheduleListDto {

        @ApiModelProperty(value = "날짜")
        private LocalDate date;

        @ApiModelProperty(value = "클래스 list")
        private List<ResponseScheduleKlassListDto> scheduleKlassList;

        public static ResponseScheduleListDto toDto(List<ScheduleDto.ResponseScheduleKlassListDto> scheduleKlassList) {
            return ResponseScheduleListDto.builder()
                    .date(scheduleKlassList.get(0).getDate())
                    .scheduleKlassList(scheduleKlassList)
                    .build();
        }

        public static List<ResponseScheduleListDto> toDtoList(List<ScheduleDto.ResponseScheduleKlassListDto> scheduleKlassList) {
            SortedMap<LocalDate, List<ScheduleDto.ResponseScheduleKlassListDto>> scheduleKlassListGroupByDate = scheduleKlassList
                    .stream()
                    .collect(Collectors.groupingBy(ScheduleDto.ResponseScheduleKlassListDto::getDate, TreeMap::new, Collectors.toList()));

            List<ResponseScheduleListDto> scheduleList = new ArrayList<>();

            for (LocalDate date : scheduleKlassListGroupByDate.keySet()) {
                scheduleList.add(ResponseScheduleListDto.toDto(scheduleKlassListGroupByDate.get(date)));
            }
            return scheduleList;
        }

    }

    @Builder
    @Getter
    public static class ResponseSchedulePagingListDto {

        @ApiModelProperty(value = "일정 리스트")
        private List<ScheduleDto.ResponseScheduleDto> results;

        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static ScheduleDto.ResponseSchedulePagingListDto build(Page<ScheduleDto.ResponseScheduleDto> responseDtoPage, Integer currentPage, Integer maxResults) {
            return ScheduleDto.ResponseSchedulePagingListDto.builder()
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
    public static class ResponseScheduleKlassListDto {

        @ApiModelProperty(value = "일정 id")
        private Long scheduleId;

        @ApiModelProperty(value = "날짜")
        private LocalDate date;

        @ApiModelProperty(value = "일정 시작 시간")
        private Long startDt;

        @ApiModelProperty(value = "일정 종료 시간")
        private Long endDt;

        @ApiModelProperty(value = "정원")
        private Integer maxTuteeNum;

        @ApiModelProperty(value = "현재 예약 인원")
        private Integer currentTuteeNum;

        @ApiModelProperty(value = "스케쥴 종류(일반, 휴무, 상담)")
        private ScheduleTypeEnum scheduleType;

        @ApiModelProperty(value = "클래스 list")
        private KlassDto.ResponseKlassDto klassDto;

        public static ResponseScheduleKlassListDto toDto(final Schedule schedule){
            return ResponseScheduleKlassListDto.builder()
                    .scheduleId(schedule.getId())
                    .date(schedule.getStartDt().toLocalDate())
                    .startDt(TimeFunction.toUnixTime(schedule.getStartDt()))
                    .endDt(TimeFunction.toUnixTime(schedule.getEndDt()))
                    .maxTuteeNum(schedule.getMaxTuteeNum())
                    .currentTuteeNum(schedule.getCurrentTuteeNum())
                    .scheduleType(schedule.getScheduleType())
                    .klassDto(KlassDto.ResponseKlassDto.toDto(schedule.getKlass()))
                    .build();
        }

    }

    @Getter
    @Builder
    public static class RequestScheduleDto {

        @NotNull(message = "담당 강사를 선택해주세요.")
        @ApiModelProperty(value = "담당 강사 ID(필수)", example = "263")
        private Long mainTutorId;

        @ApiModelProperty(value = "보조 강사 ID", example = "264")
        private Long subTutorId;

        @NotNull(message = "수업을 입력해주세요.")
        @ApiModelProperty(value = "클래스 ID", example = "6")
        private Long klassId;

        @ApiModelProperty(value = "반복 일정 ID", example = "5")
        private Long repeatScheduleId;

        @ApiModelProperty(value = "시작 시각", example = "1700704800000")
        private Long startDt;

        @ApiModelProperty(value = "종료 시각", example = "1700708400000")
        private Long endDt;

        @ApiModelProperty(value = "예약 상태", example = "BEFORE_RECEIPT")
        private ScheduleStatusTypeEnum statusType;

        @ApiModelProperty(value = "공개 상태", example = "false")
        private Boolean privateYn;

        @ApiModelProperty(value = "비공개 메모", example = "비공개 노트 예시")
        private String privateMemo;

        @ApiModelProperty(value = "공개 메모", example = "공개 메모 예시")
        private String publicMemo;

        @ApiModelProperty(value = "예약 허용 여부", example = "false")
        private Boolean reservationEnableYn;

        @ApiModelProperty(value = "예약 가능 시간", example = "")
        private Integer reservationEnableTime;

        @ApiModelProperty(value = "취소 가능 시간", example = "")
        private Integer cancelEnableTime;

        @ApiModelProperty(value = "최소 정원 미 충족시 자동 취소 시간", example = "")
        private Integer minTuteeLackCancelTime;

        public Schedule toEntity(Branch branch, User mainTutor, User subTutor, Klass klass, RepeatSchedule repeatSchedule){
            return Schedule.builder()
                    .mainTutor(mainTutor)
                    .subTutor(subTutor)
                    .branch(branch)
                    .klass(klass)
                    .repeatSchedule(repeatSchedule)
                    .klassName(klass.getKlassName())
                    .startDt(TimeFunction.toZonedDateTime(startDt))
                    .endDt(TimeFunction.toZonedDateTime(endDt))
                    .minTuteeNum(klass.getMinTuteeNum())
                    .maxTuteeNum(klass.getMaxTuteeNum())
                    .statusType(statusType)
                    .privateYn(privateYn)
                    .scheduleType(ScheduleTypeEnum.NORMAL)
                    .level(klass.getLevel())
                    .colorCdType(klass.getColorCdType())
                    .privateMemo(privateMemo)
                    .publicMemo(publicMemo)
                    .reservationEnableYn(reservationEnableYn)
                    .reservationEnableTime(reservationEnableTime)
                    .cancelEnableTime(cancelEnableTime)
                    .minTuteeLackCancelTime(minTuteeLackCancelTime)
                    .build();
        }

    }

    @Builder
    @Getter
    public static class RequestOffScheduleWithDateDto{

        private List<LocalDate> offDate;

        private List<RequestOffScheduleDto> offScheduleDtoList;
    }

    @Getter
    @Builder
    public static class RequestOffScheduleDto {

        @ApiModelProperty(value = "반복 일정 ID", example = "207")
        private Long repeatScheduleId;

        @ApiModelProperty(value = "시작 시각", example = "1700704800000")
        private Long startDt;

        @ApiModelProperty(value = "종료 시각", example = "1700708400000")
        private Long endDt;

        @ApiModelProperty(value = "공개 상태", example = "false")
        private Boolean privateYn;

        @ApiModelProperty(value = "비공개 메모", example = "비공개 노트 예시")
        private String privateMemo;

        @ApiModelProperty(value = "공개 메모", example = "공개 메모 예시")
        private String publicMemo;

        @ApiModelProperty(value = "자동 수강권 기간 연장", example = "false")
        private Boolean autoExtendTicketYn;

        public Schedule toEntity(Branch branch, RepeatSchedule repeatSchedule){
            return Schedule.builder()
                    .branch(branch)
                    .repeatSchedule(repeatSchedule)
                    .startDt(TimeFunction.toZonedDateTime(startDt))
                    .endDt(TimeFunction.toZonedDateTime(endDt))
                    .scheduleType(ScheduleTypeEnum.OFF)
                    .privateYn(privateYn)
                    .privateMemo(privateMemo)
                    .publicMemo(publicMemo)
                    .build();
        }
    }
}