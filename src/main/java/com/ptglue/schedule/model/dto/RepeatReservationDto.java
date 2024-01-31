package com.ptglue.schedule.model.dto;

import com.ptglue.schedule.enums.RepeatScheduleStatusTypeEnum;
import com.ptglue.schedule.model.entity.RepeatReservation;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RepeatReservationDto {

    @Getter
    @Builder
    public static class ResponseRepeatReservationDto {

        @ApiModelProperty(value = "반복 예약 id", example = "1")
        private Long repeatReservationId;

        @ApiModelProperty(value = "지점 id", example = "1")
        private Long branchId;

        @ApiModelProperty(value = "반복 일정 id", example = "1")
        private Long repeatScheduleId;

        @ApiModelProperty(value = "시작 일자", example = "2023-11-08")
        private LocalDate startDate;

        @ApiModelProperty(value = "종료 일자", example = "2023-11-08")
        private LocalDate endDate;

        @ApiModelProperty(value = "상태")
        private RepeatScheduleStatusTypeEnum statusType;

        @ApiModelProperty(value = "일요일 예약 시간")
        private List<String> sunStartEndTime;

        @ApiModelProperty(value = "월요일 예약 시간")
        private List<String> monStartEndTime;

        @ApiModelProperty(value = "화요일 예약 시간")
        private List<String> tueStartEndTime;

        @ApiModelProperty(value = "수요일 예약 시간")
        private List<String> wedStartEndTime;

        @ApiModelProperty(value = "목요일 예약 시간")
        private List<String> thrStartEndTime;

        @ApiModelProperty(value = "금요일 예약 시간")
        private List<String> friStartEndTime;

        @ApiModelProperty(value = "토요일 예약 시간")
        private List<String> satStartEndTime;

        public static ResponseRepeatReservationDto toDto(final RepeatReservation repeatReservation){
            return ResponseRepeatReservationDto.builder()
                    .repeatReservationId(repeatReservation.getId())
                    .branchId(repeatReservation.getBranch().getId())
                    .repeatScheduleId(repeatReservation.getRepeatSchedule().getId())
                    .startDate(repeatReservation.getStartDate())
                    .endDate(repeatReservation.getEndDate())
                    .statusType(repeatReservation.getStatusType())
                    .sunStartEndTime(repeatReservation.getSunStartEndTime() != null ? Arrays.asList(repeatReservation.getSunStartEndTime().split(",")) : null)
                    .monStartEndTime(repeatReservation.getMonStartEndTime() != null ? Arrays.asList(repeatReservation.getMonStartEndTime().split(",")) : null)
                    .tueStartEndTime(repeatReservation.getTueStartEndTime() != null ? Arrays.asList(repeatReservation.getTueStartEndTime().split(",")) : null)
                    .wedStartEndTime(repeatReservation.getWedStartEndTime() != null ? Arrays.asList(repeatReservation.getWedStartEndTime().split(",")) : null)
                    .thrStartEndTime(repeatReservation.getThrStartEndTime() != null ? Arrays.asList(repeatReservation.getThrStartEndTime().split(",")) : null)
                    .friStartEndTime(repeatReservation.getFriStartEndTime() != null ? Arrays.asList(repeatReservation.getFriStartEndTime().split(",")) : null)
                    .satStartEndTime(repeatReservation.getSatStartEndTime() != null ? Arrays.asList(repeatReservation.getSatStartEndTime().split(",")) : null)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ResponseRepeatReservationListDto {

        @ApiModelProperty(value = "반복 예약 목록")
        private List<ResponseRepeatReservationDto> results;

        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static ResponseRepeatReservationListDto build(Page<ResponseRepeatReservationDto> responseDtoPage, Integer currentPage, Integer maxResults) {
            return ResponseRepeatReservationListDto.builder()
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
    public static class RequestRepeatReservationDto{
        @ApiModelProperty(value = "시작 일자", example = "2023-11-08")
        private LocalDate startDate;

        @ApiModelProperty(value = "종료 일자", example = "2023-11-08")
        private LocalDate endDate;

        @ApiModelProperty(value = "일요일 예약 시간")
        private List<String> sunStartEndTime;

        @ApiModelProperty(value = "월요일 예약 시간")
        private List<String> monStartEndTime;

        @ApiModelProperty(value = "화요일 예약 시간")
        private List<String> tueStartEndTime;

        @ApiModelProperty(value = "수요일 예약 시간")
        private List<String> wedStartEndTime;

        @ApiModelProperty(value = "목요일 예약 시간")
        private List<String> thrStartEndTime;

        @ApiModelProperty(value = "금요일 예약 시간")
        private List<String> friStartEndTime;

        @ApiModelProperty(value = "토요일 예약 시간")
        private List<String> satStartEndTime;

        public RepeatReservation toEntity(RepeatReservation repeatReservation){
            return RepeatReservation.builder()
                    .id(repeatReservation.getId())
                    .repeatSchedule(repeatReservation.getRepeatSchedule())
                    .branch(repeatReservation.getBranch())
                    .startDate(startDate)
                    .endDate(endDate)
                    .sunStartEndTime(sunStartEndTime == null ? null : String.join(",",sunStartEndTime))
                    .monStartEndTime(monStartEndTime == null ? null : String.join(",",monStartEndTime))
                    .tueStartEndTime(tueStartEndTime == null ? null : String.join(",",tueStartEndTime))
                    .wedStartEndTime(wedStartEndTime == null ? null : String.join(",",wedStartEndTime))
                    .thrStartEndTime(thrStartEndTime == null ? null : String.join(",",thrStartEndTime))
                    .friStartEndTime(friStartEndTime == null ? null : String.join(",",friStartEndTime))
                    .satStartEndTime(satStartEndTime == null ? null : String.join(",",satStartEndTime))
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ResponseRepeatReservationCheckDto{
        @ApiModelProperty(value = "예약 가능 여부")
        private String available;

        @ApiModelProperty(value = "상세 메세지")
        private String message;

        public static ResponseRepeatReservationCheckDto toDto(String available, String message){
            return ResponseRepeatReservationCheckDto.builder()
                    .available(available)
                    .message(message)
                    .build();
        }
    }
}
