package com.orderline.notice.model.dto;

import com.orderline.notice.enums.NoticeTypeEnum;
import com.orderline.notice.enums.NotificationTypeEnum;
import com.orderline.notice.enums.BranchNoticeTargetTypeEnum;
import com.orderline.notice.model.entity.BranchNotice;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BranchNoticeDto {
    @Getter
    @Builder
    public static class ResponseBranchNoticeDto{
        @ApiModelProperty(value = "지점내 공지 ID")
        private Long id;

        @ApiModelProperty(value = "지점 ID")
        private Long branchId;

        @ApiModelProperty(value = "공지 타입")
        private NoticeTypeEnum noticeType;

        @ApiModelProperty(value = "알림 타입")
        private NotificationTypeEnum notificationType;

        @ApiModelProperty(value = "타겟 대상")
        private BranchNoticeTargetTypeEnum targetType;

        @ApiModelProperty(value = "제목")
        private String title;

        @ApiModelProperty(value = "내용")
        private String contents;

        @ApiModelProperty(value = "팝업 노출 여부")
        private Boolean popupDisplayYn;

        @ApiModelProperty(value = "중요 여부")
        private Boolean importantYn;

        @ApiModelProperty(value = "댓글 사용 여부")
        private Boolean commentYn;

        @ApiModelProperty(value = "좋아요 사용 여부")
        private Boolean likeYn;

        @ApiModelProperty(value = "조회수")
        private Integer hits;

        @ApiModelProperty(value = "좋아요수")
        private Integer like;

        public static BranchNoticeDto.ResponseBranchNoticeDto toDto(BranchNotice branchNotice){
            return ResponseBranchNoticeDto.builder()
                    .id(branchNotice.getId())
                    .branchId(branchNotice.getBranch().getId())
                    .noticeType(branchNotice.getNoticeType())
                    .notificationType(branchNotice.getNotificationType())
                    .targetType(branchNotice.getTargetType())
                    .title(branchNotice.getTitle())
                    .contents(branchNotice.getContents())
                    .popupDisplayYn(branchNotice.getPopupDisplayYn())
                    .importantYn(branchNotice.getImportantYn())
                    .commentYn(branchNotice.getCommentYn())
                    .likeYn(branchNotice.getLikeYn())
                    .hits(branchNotice.getHits())
                    .like(branchNotice.getLike())
                    .build();
        }
    }

    @Builder
    @Getter
    public static class ResponseBranchNoticeListDto {
        @ApiModelProperty(value = "클래스 리스트")
        private List<BranchNoticeDto.ResponseBranchNoticeDto> results;

        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static BranchNoticeDto.ResponseBranchNoticeListDto build(Page<BranchNoticeDto.ResponseBranchNoticeDto> responseDtoPage, Integer currentPage, Integer maxResults){
            return BranchNoticeDto.ResponseBranchNoticeListDto.builder()
                    .results(responseDtoPage.getContent())
                    .maxResults(maxResults)
                    .currentPage(currentPage)
                    .totalPages(responseDtoPage.getTotalPages())
                    .totalElements(responseDtoPage.getTotalElements())
                    .build();
        }
    }
}
