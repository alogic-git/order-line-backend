package com.ptglue.notice.model.dto;

import com.ptglue.branch.model.entity.BranchUserRole;
import com.ptglue.common.user.enums.SnsTypeEnum;
import com.ptglue.common.user.model.entity.User;
import com.ptglue.notice.enums.NotificationStatusTypeEnum;
import com.ptglue.notice.enums.NotificationTypeEnum;
import com.ptglue.notice.model.entity.NotificationHistory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;

public class NotificationHistoryDto {

    @Builder
    @Getter
    public static class ResponseNotificationDto {

        @ApiModelProperty(value = "Notification ID")
        private Long id;

        @ApiModelProperty(value = "Branch ID")
        private Long branchId;

        @ApiModelProperty(value = "Target User ID")
        private Long targetUserId;

        @ApiModelProperty(value = "Message Id")
        private Long messageId;

        @ApiModelProperty(value = "Original Table Name")
        private String originalTableName;

        @ApiModelProperty(value = "Original Table ID")
        private Long originalTableId;

        @ApiModelProperty(value = "Notification Type")
        private NotificationTypeEnum notificationType;

        @ApiModelProperty(value = "Title")
        private String title;

        @ApiModelProperty(value = "Contents")
        private String contents;

        @ApiModelProperty(value = "Device Info")
        private String deviceInfo;

        @ApiModelProperty(value = "Status Type")
        private NotificationStatusTypeEnum statusType;

        @ApiModelProperty(value = "target user others")
        private Integer others;

        @ApiModelProperty(value = "fail message count")
        private Integer failCount;

        public static ResponseNotificationDto toDto(NotificationHistory notificationHistory){
            return ResponseNotificationDto.builder()
                    .id(notificationHistory.getId())
                    .branchId(notificationHistory.getBranch().getId())
                    .targetUserId(notificationHistory.getTargetUserId())
                    .messageId(notificationHistory.getMessageId())
                    .originalTableName(notificationHistory.getOriginalTableName())
                    .originalTableId(notificationHistory.getOriginalTableId())
                    .notificationType(notificationHistory.getNotificationType())
                    .title(notificationHistory.getTitle())
                    .contents(notificationHistory.getContents())
                    .deviceInfo(notificationHistory.getDeviceInfo())
                    .statusType(notificationHistory.getStatusType())
                    .build();
        }

        public static ResponseNotificationDto toDto(NotificationHistory notificationHistory, Integer others, Integer failCount){
            return ResponseNotificationDto.builder()
                    .id(notificationHistory.getId())
                    .branchId(notificationHistory.getBranch().getId())
                    .targetUserId(notificationHistory.getTargetUserId())
                    .messageId(notificationHistory.getMessageId())
                    .originalTableName(notificationHistory.getOriginalTableName())
                    .originalTableId(notificationHistory.getOriginalTableId())
                    .notificationType(notificationHistory.getNotificationType())
                    .title(notificationHistory.getTitle())
                    .contents(notificationHistory.getContents())
                    .deviceInfo(notificationHistory.getDeviceInfo())
                    .statusType(notificationHistory.getStatusType())
                    .others(others)
                    .failCount(failCount)
                    .build();
        }
    }

    @Builder
    @Getter
    public static class ResponseNotificationListDto {

        @ApiModelProperty(value = "알림 리스트")
        private List<ResponseNotificationDto> results;

        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static NotificationHistoryDto.ResponseNotificationListDto build(Page<NotificationHistoryDto.ResponseNotificationDto> responseDtoPage, Integer currentPage, Integer maxResults){
            return NotificationHistoryDto.ResponseNotificationListDto.builder()
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
    public static class ResponseMessageUserDto{
        @ApiModelProperty(value = "Notification Id")
        private Long notificationId;

        @ApiModelProperty(value = "User Id")
        private Long userId;

        @ApiModelProperty(value = "Phone")
        private String phone;

        @ApiModelProperty(value = "Username")
        private String username;

        @ApiModelProperty(value = "Name")
        private String name;

        @ApiModelProperty(value = "Status Type")
        private NotificationStatusTypeEnum statusType;

        public static ResponseMessageUserDto toDto(NotificationHistory notificationHistory, User user){
            return ResponseMessageUserDto.builder()
                    .notificationId(notificationHistory.getId())
                    .userId(user.getId())
                    .phone(user.getPhone())
                    .username(user.getName().length() < 4
                            ? user.getName().substring(0, user.getName().length() - 1) + "*"
                            : user.getName().substring(0, user.getName().length() - 2) + "**"
                    )
                    .name(user.getName())
                    .statusType(notificationHistory.getStatusType())
                    .build();
        }

        public static ResponseMessageUserDto toUserDto(BranchUserRole branchUserRole){
            return ResponseMessageUserDto.builder()
                    .userId(branchUserRole.getUser().getId())
                    .phone(branchUserRole.getUser().getPhone())
                    .username(branchUserRole.getUser().getName().length() < 4
                            ? branchUserRole.getUser().getName().substring(0, branchUserRole.getUser().getName().length() - 1) + "*"
                            : branchUserRole.getUser().getName().substring(0, branchUserRole.getUser().getName().length() - 2) + "**"
                    )
                    .name(branchUserRole.getUser().getName())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ResponseMessageUserListDto{
        @ApiModelProperty(value = "메세지 수신 리스트")
        private List<ResponseMessageUserDto> results;

        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static ResponseMessageUserListDto build(Page<ResponseMessageUserDto> responseDtoPage, Integer currentPage, Integer maxResults){
            return ResponseMessageUserListDto.builder()
                    .results(responseDtoPage.getContent())
                    .maxResults(maxResults)
                    .currentPage(currentPage)
                    .totalPages(responseDtoPage.getTotalPages())
                    .totalElements(responseDtoPage.getTotalElements())
                    .build();
        }
    }
}
