package com.orderline.notice.model.dto;

import com.orderline.notice.enums.NoticeTypeEnum;
import com.orderline.notice.enums.NotificationTypeEnum;
import com.orderline.notice.enums.NoticeTargetTypeEnum;
import com.orderline.notice.model.entity.Notice;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeDto {

    @Getter
    @Builder
    public static class ResponseNoticeDto{
        @ApiModelProperty(value = "공지 ID")
        private Long id;

        @ApiModelProperty(value = "공지 타입")
        private NoticeTypeEnum noticeType;

        @ApiModelProperty(value = "알림 타입")
        private NotificationTypeEnum notificationType;

        @ApiModelProperty(value = "타겟 대상")
        private NoticeTargetTypeEnum targetType;

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

        public static ResponseNoticeDto toDto(Notice notice){
            return ResponseNoticeDto.builder()
                    .id(notice.getId())
//                    .noticeType(notice.getNoticeType())
//                    .notificationType(notice.getNotificationType())
//                    .targetType(notice.getTargetType())
                    .title(notice.getTitle())
                    .contents(notice.getContents())
//                    .popupDisplayYn(notice.getPopupDisplayYn())
//                    .importantYn(notice.getImportantYn())
//                    .commentYn(notice.getCommentYn())
//                    .likeYn(notice.getLikeYn())
//                    .hits(notice.getHits())
//                    .like(notice.getLike())
                    .build();
        }
    }
}
