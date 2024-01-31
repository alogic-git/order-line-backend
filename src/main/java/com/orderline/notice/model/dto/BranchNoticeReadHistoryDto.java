package com.orderline.notice.model.dto;

import com.orderline.notice.model.entity.BranchNoticeReadHistory;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BranchNoticeReadHistoryDto {
    @Getter
    @Builder
    public static class ResponseBranchNoticeReadHistoryDto{
        @ApiModelProperty(value = "공지 확인 기록 ID")
        private Long id;

        @ApiModelProperty(value = "지점 ID")
        private Long branchId;

        @ApiModelProperty(value = "지점 공지 ID")
        private Long branchNoticeId;

        @ApiModelProperty(value = "사용자 ID")
        private Long userId;


        public static ResponseBranchNoticeReadHistoryDto toDto(BranchNoticeReadHistory branchNoticeReadHistory){
            return ResponseBranchNoticeReadHistoryDto.builder()
                    .id(branchNoticeReadHistory.getId())
                    .branchId(branchNoticeReadHistory.getBranch().getId())
                    .branchNoticeId(branchNoticeReadHistory.getBranchNotice().getId())
                    .userId(branchNoticeReadHistory.getUser().getId())
                    .build();
        }
    }
}
