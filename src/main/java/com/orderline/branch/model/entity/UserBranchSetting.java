package com.orderline.branch.model.entity;

import com.orderline.basic.model.entity.BaseTimeEntity;
import com.orderline.common.user.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@DynamicUpdate
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Where(clause = "delete_yn = 0")
@Entity
@Table(name = "user_branch_setting")
public class UserBranchSetting extends BaseTimeEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "branch_id")
        private Branch branchId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        private User userId;

        @Column(name = "alarm_change_schedule_to_me_tutee_role_yn")
        private Boolean alarmChangeScheduleToMeTuteeRoleYn;

        @Column(name = "alarm_change_schedule_to_me_staff_role_yn")
        private Boolean alarmChangeScheduleToMeStaffRoleYn;

        @Column(name = "alarm_notice_schedule_start_time")
        private Integer alarmNoticeScheduleStartTime;

        @Column(name = "alarm_notice_ticket_end_date")
        private Integer alarmNoticeTicketEndDate;

        public void updateAlarmChangeScheduleToMeTuteeRoleYn(Boolean alarmChangeScheduleToMeTuteeRoleYn){
                this.alarmChangeScheduleToMeTuteeRoleYn = alarmChangeScheduleToMeTuteeRoleYn;
        }

        public void updateAlarmChangeScheduleToMeStaffRoleYn(Boolean alarmChangeScheduleToMeStaffRoleYn){
                this.alarmChangeScheduleToMeStaffRoleYn = alarmChangeScheduleToMeStaffRoleYn;
        }

        public void updateAlarmNoticeScheduleStartTime(Integer alarmNoticeScheduleStartTime){
                this.alarmNoticeScheduleStartTime = alarmNoticeScheduleStartTime;
        }
        
        public void updateAlarmNoticeTicketEndDate(Integer alarmNoticeTicketEndDate){
                this.alarmNoticeTicketEndDate = alarmNoticeTicketEndDate;
        }
}


