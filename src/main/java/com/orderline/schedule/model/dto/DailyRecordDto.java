package com.orderline.schedule.model.dto;


import io.swagger.annotations.ApiModelProperty;
import com.orderline.branch.model.entity.Branch;
import com.orderline.schedule.model.entity.DailyRecord;
import com.orderline.schedule.model.entity.Reservation;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DailyRecordDto {

    @Getter
    @Builder
    public static class ResponseDailyRecordDto{
        @ApiModelProperty(name = "일일 기록 id", example = "1")
        private Long id;

        @ApiModelProperty(name = "지점 id", example = "1")
        private Long branchId;

        @ApiModelProperty(name = "예약 id", example = "1")
        private Long reservationId;

        @ApiModelProperty(name = "노트", example = "노트 입니다.")
        private String note;

        @ApiModelProperty(name = "보관 여부")
        private Boolean archiveYn;

        public static ResponseDailyRecordDto toDto(final DailyRecord dailyRecord){
            return ResponseDailyRecordDto.builder()
                    .id(dailyRecord.getId())
                    .branchId(dailyRecord.getBranch().getId())
                    .reservationId(dailyRecord.getReservation().getId())
                    .note(dailyRecord.getNote())
                    .archiveYn(dailyRecord.getArchiveYn())
                    .build();
        }
    }

    @Builder
    @Getter
    public static class ResponseDailyRecordListDto {

        @ApiModelProperty(value = "일일 기록 리스트")
        private List<DailyRecordDto.ResponseDailyRecordDto> results;

        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static DailyRecordDto.ResponseDailyRecordListDto build(Page<DailyRecordDto.ResponseDailyRecordDto> responseDtoPage, Integer currentPage, Integer maxResults){
            return DailyRecordDto.ResponseDailyRecordListDto.builder()
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
    public static class RequestDailyRecordDto {
        @ApiModelProperty(name = "예약 id", example = "1")
        private Long reservationId;

        @ApiModelProperty(name = "노트", example = "노트 입니다")
        private String note;

        public DailyRecord toEntity(Branch branch, Reservation reservation){
            return DailyRecord.builder()
                    .branch(branch)
                    .reservation(reservation)
                    .note(note)
                    .build();
        }
    }
}