package com.ptglue.schedule.model.dto;

import com.ptglue.common.user.model.entity.User;
import com.ptglue.schedule.enums.RepeatScheduleStatusTypeEnum;
import com.ptglue.schedule.model.entity.RepeatReservation;
import io.swagger.annotations.ApiModelProperty;
import com.ptglue.branch.model.entity.Branch;
import com.ptglue.klass.model.entity.Klass;
import com.ptglue.schedule.model.entity.RepeatSchedule;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RepeatScheduleDto {
    @Getter
    @Builder
    public static class ResponseRepeatScheduleDto {

        @ApiModelProperty(value = "반복 예약 id", example = "1")
        private Long repeatScheduleId;

        @ApiModelProperty(value = "지점 id", example = "1")
        private Long branchId;

        @ApiModelProperty(value = "클래스 id", example = "1")
        private Long klassId;

        @ApiModelProperty(value = "클래스 이름", example = "1")
        private String klassName;

        @ApiModelProperty(value = "시작일자", example = "2023-11-08")
        private LocalDate startDate;

        @ApiModelProperty(value = "종료일자", example = "2023-11-08")
        private LocalDate endDate;

        @ApiModelProperty(value = "상태")
        private RepeatScheduleStatusTypeEnum statusType;

        @ApiModelProperty(value = "일요일 영업시간")
        private List<String> sunStartEndTime;

        @ApiModelProperty(value = "월요일 영업시간")
        private List<String> monStartEndTime;

        @ApiModelProperty(value = "화요일 영업시간")
        private List<String> tueStartEndTime;

        @ApiModelProperty(value = "수요일 영업시간")
        private List<String> wedStartEndTime;

        @ApiModelProperty(value = "목요일 영업시간")
        private List<String> thrStartEndTime;

        @ApiModelProperty(value = "금요일 영업시간")
        private List<String> friStartEndTime;

        @ApiModelProperty(value = "토요일 영업시간")
        private List<String> satStartEndTime;

        @ApiModelProperty(value = "보관 여부")
        private Boolean archiveYn;

        public static ResponseRepeatScheduleDto toDto(final RepeatSchedule repeatSchedule) {
            return ResponseRepeatScheduleDto.builder()
                    .repeatScheduleId(repeatSchedule.getId())
                    .branchId(repeatSchedule.getBranch().getId())
                    .klassId(repeatSchedule.getKlass().getId())
                    .klassName(repeatSchedule.getKlass().getKlassName())
                    .startDate(repeatSchedule.getStartDate())
                    .endDate(repeatSchedule.getEndDate())
                    .statusType(repeatSchedule.getStatusType())
                    .sunStartEndTime(repeatSchedule.getSunStartEndTime() == null? null :Arrays.asList(repeatSchedule.getSunStartEndTime().split(",")))
                    .monStartEndTime(repeatSchedule.getMonStartEndTime() == null? null :Arrays.asList(repeatSchedule.getMonStartEndTime().split(",")))
                    .tueStartEndTime(repeatSchedule.getTueStartEndTime() == null? null :Arrays.asList(repeatSchedule.getTueStartEndTime().split(",")))
                    .wedStartEndTime(repeatSchedule.getWedStartEndTime() == null? null :Arrays.asList(repeatSchedule.getWedStartEndTime().split(",")))
                    .thrStartEndTime(repeatSchedule.getThrStartEndTime() == null? null :Arrays.asList(repeatSchedule.getThrStartEndTime().split(",")))
                    .friStartEndTime(repeatSchedule.getFriStartEndTime() == null? null :Arrays.asList(repeatSchedule.getFriStartEndTime().split(",")))
                    .satStartEndTime(repeatSchedule.getSatStartEndTime() == null? null :Arrays.asList(repeatSchedule.getSatStartEndTime().split(",")))
                    .archiveYn(repeatSchedule.getArchiveYn())
                    .build();
        }

    }

    @Getter
    @Builder
    public static class ResponseRepeatScheduleListDto {

        @ApiModelProperty(value = "반복 예약 목록")
        private List<ResponseRepeatScheduleDto> results;

        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static ResponseRepeatScheduleListDto build(Page<ResponseRepeatScheduleDto> responseDtoPage, Integer currentPage, Integer maxResults) {
            return ResponseRepeatScheduleListDto.builder()
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
    public static class  RequestRepeatScheduleDto{

        @ApiModelProperty(value = "지점 id")
        private Long branchId;

        @ApiModelProperty(value = "클래스 id")
        private Long klassId;

        @ApiModelProperty(value = "시작일자")
        private LocalDate startDate;

        @ApiModelProperty(value = "종료일자")
        private LocalDate endDate;

        @ApiModelProperty(value = "상태")
        private RepeatScheduleStatusTypeEnum statusType;

        @ApiModelProperty(value = "일요일 수업시간")
        private List<String> sunStartEndTime;

        @ApiModelProperty(value = "월요일 수업시간")
        private List<String> monStartEndTime;

        @ApiModelProperty(value = "화요일 수업시간")
        private List<String> tueStartEndTime;

        @ApiModelProperty(value = "수요일 수업시간")
        private List<String> wedStartEndTime;

        @ApiModelProperty(value = "목요일 수업시간")
        private List<String> thrStartEndTime;

        @ApiModelProperty(value = "금요일 수업시간")
        private List<String> friStartEndTime;

        @ApiModelProperty(value = "토요일 수업시간")
        private List<String> satStartEndTime;

        public RepeatSchedule toEntity(Branch branch, Klass klass){
            return RepeatSchedule.builder()
                    .branch(branch)
                    .klass(klass)
                    .startDate(startDate)
                    .endDate(endDate)
                    .statusType(statusType)
                    .sunStartEndTime(String.join(",", sunStartEndTime))
                    .monStartEndTime(String.join(",", monStartEndTime))
                    .tueStartEndTime(String.join(",", tueStartEndTime))
                    .wedStartEndTime(String.join(",", wedStartEndTime))
                    .thrStartEndTime(String.join(",", thrStartEndTime))
                    .friStartEndTime(String.join(",", friStartEndTime))
                    .satStartEndTime(String.join(",", satStartEndTime))
                    .build();
        }

        public static RepeatReservation toRepeatReservationEntity(RepeatSchedule repeatSchedule, User tutee){
            return RepeatReservation.builder()
                    .branch(repeatSchedule.getBranch())
                    .tutee(tutee)
                    .repeatSchedule(repeatSchedule)
                    .startDate(repeatSchedule.getStartDate())
                    .endDate(repeatSchedule.getEndDate())
                    .statusType(repeatSchedule.getStatusType())
                    .sunStartEndTime(repeatSchedule.getSunStartEndTime())
                    .monStartEndTime(repeatSchedule.getMonStartEndTime())
                    .tueStartEndTime(repeatSchedule.getTueStartEndTime())
                    .wedStartEndTime(repeatSchedule.getWedStartEndTime())
                    .thrStartEndTime(repeatSchedule.getThrStartEndTime())
                    .friStartEndTime(repeatSchedule.getFriStartEndTime())
                    .satStartEndTime(repeatSchedule.getSatStartEndTime())
                    .build();
        }
    }
}
