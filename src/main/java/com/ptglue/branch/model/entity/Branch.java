package com.ptglue.branch.model.entity;

import com.ptglue.basic.model.entity.BaseTimeEntity;
import com.ptglue.branch.enums.NoticeMethodType;
import com.ptglue.branch.enums.ScheduleAutoFinishTypeEnum;
import com.ptglue.common.user.model.entity.User;
import com.ptglue.branch.enums.ReservationTypeEnum;
import com.ptglue.branch.enums.SubjectTypeEnum;
import com.ptglue.branch.model.dto.BranchDto;
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
@Table(name = "branch")
public class Branch extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "owner_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @Column(name = "branch_name")
    private String branchName;

    @Column(name = "image_uri")
    private String imageUri;

    @Enumerated(EnumType.STRING)
    @Column(name = "subject_type")
    private SubjectTypeEnum subjectType;

    @Column(name = "subject_detail")
    private String subjectDetail;

    @Enumerated(EnumType.STRING)
    @Column(name ="reservation_type")
    private ReservationTypeEnum reservationType;

    @Column(name = "tutor_count")
    private Integer tutorCount;

    @Column(name = "tutee_count")
    private Integer tuteeCount;

    @Column(name = "private_memo")
    private String privateMemo;

    @Column(name = "public_memo")
    private String publicMemo;

    @Column(name = "start_week_type")
    private String startWeekType;

    @Column(name = "select_time")
    private Integer selectTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_auto_finish_type")
    private ScheduleAutoFinishTypeEnum scheduleAutoFinishType;

    @Column(name = "ticket_auto_finish_type")
    private Boolean ticketAutoFinishType;

    @Column(name = "sign_upon_attendance_yn")
    private Boolean signUpOnAttendanceYn;

    @Column(name = "reservation_enable_yn")
    private Boolean reservationEnableYn;

    @Column(name = "reservation_available_date")
    private Integer reservationAvailableDate;

    @Column(name = "reservation_num_display_yn")
    private Boolean reservationNumDisplayYn;

    @Column(name = "capacity_num_display_yn")
    private Boolean capacityNumDisplayYn;

    @Column(name = "reservation_enable_times")
    private String reservationEnableTimes;

    @Column(name = "alarm_change_schedule_to_tutee_yn")
    private Boolean alarmChangeScheduleToTuteeYn;

    @Column(name = "alarm_change_schedule_to_tutor_yn")
    private Boolean alarmChangeScheduleToTutorYn;

    @Column(name = "alarm_change_schedule_to_manager_yn")
    private Boolean alarmChangeScheduleToManagerYn;

    @Column(name = "alarm_notice_schedule_start_time")
    private Integer alarmNoticeScheduleStartTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "alarm_notice_schedule_start_time_method_type")
    private NoticeMethodType alarmNoticeScheduleStartTimeMethodType;

    @Column(name = "alarm_notice_ticket_end_date")
    private Integer alarmNoticeTicketEndDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "alarm_notice_ticket_end_date_method_type")
    private NoticeMethodType alarmNoticeTicketEndDateMethodType;

    @Column(name = "sun_operating_hours")
    private String sunOperatingHours;

    @Column(name = "mon_operating_hours")
    private String monOperatingHours;

    @Column(name = "tue_operating_hours")
    private String tueOperatingHours;

    @Column(name = "wed_operating_hours")
    private String wedOperatingHours;

    @Column(name = "thr_operating_hours")
    private String thrOperatingHours;

    @Column(name = "fri_operating_hours")
    private String friOperatingHours;

    @Column(name = "sat_operating_hours")
    private String satOperatingHours;

    @Column(name = "archive_yn")
    private Boolean archiveYn;

    public void deleteBranch() {
        this.deleteYn = true;
    }

    public void archiveBranch() {
        this.archiveYn = true;
    }

    public void updateTuteeNumPlus() {
        this.tuteeCount++;
    }
    public void updateTuteeNumMinus() {
        this.tuteeCount--;
    }
    public void updateTutorNumPlus() {
        this.tutorCount++;
    }
    public void updateTutorNumMinus() {
        this.tutorCount--;
    }

    public void updateBranch(BranchDto.RequestBranchDto requestBranchDto) {
        this.branchName = requestBranchDto.getBranchName();
        this.imageUri = requestBranchDto.getImageUri();
        this.subjectType = requestBranchDto.getSubjectType();
        this.subjectDetail = requestBranchDto.getSubjectDetail();
        //TODO : 전화번호 추가
    }

    public void updateBranchSetting(BranchDto.RequestBranchSettingDto requestBranchSettingDto) {
        this.sunOperatingHours = String.join(",", requestBranchSettingDto.getSunOperatingHours());
        this.monOperatingHours = String.join(",", requestBranchSettingDto.getMonOperatingHours());
        this.tueOperatingHours = String.join(",", requestBranchSettingDto.getTueOperatingHours());
        this.wedOperatingHours = String.join(",", requestBranchSettingDto.getWedOperatingHours());
        this.thrOperatingHours = String.join(",", requestBranchSettingDto.getThrOperatingHours());
        this.friOperatingHours = String.join(",", requestBranchSettingDto.getFriOperatingHours());
        this.satOperatingHours = String.join(",", requestBranchSettingDto.getSatOperatingHours());
        this.startWeekType = requestBranchSettingDto.getStartWeekType();
        this.selectTime = requestBranchSettingDto.getSelectTime();
        this.scheduleAutoFinishType = requestBranchSettingDto.getScheduleAutoFinishType();
        this.ticketAutoFinishType = requestBranchSettingDto.getTicketAutoFinishType();
        this.signUpOnAttendanceYn = requestBranchSettingDto.getSignUpOnAttendanceYn();
        this.reservationEnableYn = requestBranchSettingDto.getReservationEnableYn();
        this.reservationAvailableDate = requestBranchSettingDto.getReservationAvailableDate();
        this.reservationNumDisplayYn = requestBranchSettingDto.getReservationNumDisplayYn();
        this.capacityNumDisplayYn = requestBranchSettingDto.getCapacityNumDisplayYn();
        this.reservationEnableTimes = requestBranchSettingDto.getReservationEnableTimes();
        this.alarmChangeScheduleToTuteeYn = requestBranchSettingDto.getAlarmChangeScheduleToTuteeYn();
        this.alarmChangeScheduleToTutorYn = requestBranchSettingDto.getAlarmChangeScheduleToTutorYn();
        this.alarmChangeScheduleToManagerYn = requestBranchSettingDto.getAlarmChangeScheduleToManagerYn();
        this.alarmNoticeScheduleStartTime = requestBranchSettingDto.getAlarmNoticeScheduleStartTime();
        this.alarmNoticeScheduleStartTimeMethodType = requestBranchSettingDto.getAlarmNoticeScheduleStartTimeMethodType();
        this.alarmNoticeTicketEndDate = requestBranchSettingDto.getAlarmNoticeTicketEndDate();
        this.alarmNoticeTicketEndDateMethodType = requestBranchSettingDto.getAlarmNoticeTicketEndDateMethodType();
    }

}
