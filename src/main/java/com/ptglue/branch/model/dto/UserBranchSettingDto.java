package com.ptglue.branch.model.dto;

import com.ptglue.branch.model.entity.UserBranchSetting;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserBranchSettingDto {

    @Getter
    @Builder
    public static class ResponseAlarmSettingDto{
        @ApiModelProperty(value = "지점 알람설정 ID", notes = "해당 지점에 알람설정하는 테이블의 Id")
        private Long branchSettingId;

        @ApiModelProperty(value = "회원이 push알람 허용할지 여부", notes = "회원이 push알람 허용")
        private Boolean alarmChangeScheduleToMeTuteeRoleYn;

        @ApiModelProperty(value = "스태프가 push알람 허용할지 여부", notes = "스태프가 push알람 허용")
        private Boolean alarmChangeScheduleToMeStaffRoleYn;

        @ApiModelProperty(value = "일정 시작 전 알림", notes = "일정 시작 전에 알림 시간 설정(단위: 분)")
        private Integer alarmNoticeScheduleStartTime;

        @ApiModelProperty(value = "수강권 만료 전 알림", notes = "수강 권 만료 전에 알림 시간 설정(단위: 분)")
        private Integer alarmNoticeTicketEndDate;

        public static ResponseAlarmSettingDto toDto(UserBranchSetting userBranchSetting){
            return ResponseAlarmSettingDto.builder()
                    .branchSettingId(userBranchSetting.getId())
                    .alarmChangeScheduleToMeTuteeRoleYn(userBranchSetting.getAlarmChangeScheduleToMeTuteeRoleYn())
                    .alarmChangeScheduleToMeStaffRoleYn(userBranchSetting.getAlarmChangeScheduleToMeStaffRoleYn())
                    .alarmNoticeScheduleStartTime(userBranchSetting.getAlarmNoticeScheduleStartTime())
                    .alarmNoticeTicketEndDate(userBranchSetting.getAlarmNoticeTicketEndDate())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class RequestTuteeAlarmSettingDto{
        @ApiModelProperty(value = "회원이 push알람 허용할지 여부", notes = "회원이 push알람 허용")
        private Boolean alarmChangeScheduleToMeTuteeRoleYn;

        @ApiModelProperty(value = "일정 시작 전 알림", notes = "일정 시작 전에 알림 시간 설정(단위: 분)")
        private Integer alarmNoticeScheduleStartTime;

        @ApiModelProperty(value = "수강권 만료 전 알림", notes = "수강 권 만료 전에 알림 시간 설정(단위: 분)")
        private Integer alarmNoticeTicketEndDate;
    }

    @Getter
    @Builder
    public static class RequestStaffAlarmSettingDto{
        @ApiModelProperty(value = "스태프가 push알람 허용할지 여부", notes = "스태프가 push알람 허용")
        private Boolean alarmChangeScheduleToMeStaffRoleYn;

        @ApiModelProperty(value = "일정 시작 전 알림", notes = "일정 시작 전에 알림 시간 설정(단위: 분)")
        private Integer alarmNoticeScheduleStartTime;

        @ApiModelProperty(value = "수강권 만료 전 알림", notes = "수강 권 만료 전에 알림 시간 설정(단위: 분)")
        private Integer alarmNoticeTicketEndDate;
    }
}
