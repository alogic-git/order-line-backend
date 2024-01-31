package com.ptglue.klass.model.entity;

import com.ptglue.basic.model.entity.BaseTimeEntity;
import com.ptglue.branch.model.entity.Branch;
import com.ptglue.branch.model.entity.BranchUserRole;
import com.ptglue.klass.enums.KlassColorTypeEnum;
import com.ptglue.klass.enums.KlassLevelTypeEnum;
import com.ptglue.klass.enums.KlassStartTimeTypeEnum;
import com.ptglue.klass.model.dto.KlassDto;
import com.ptglue.schedule.enums.ScheduleMemberTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
@Table(name = "klass")
public class Klass extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Column(name = "klass_name")
    private String klassName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_tutor_id")
    private BranchUserRole mainTutor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_tutor_id")
    private BranchUserRole subTutor;

    @Column(name = "min_tutee_num")
    private Integer minTuteeNum;

    @Column(name = "max_tutee_num")
    private Integer maxTuteeNum;

    @Column(name = "klass_time")
    private Integer klassTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "start_time")
    private KlassStartTimeTypeEnum startTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private KlassLevelTypeEnum level;

    @Enumerated(EnumType.STRING)
    @Column(name = "color_cd_type")
    private KlassColorTypeEnum colorCdType;

    @Column(name = "tutee_count")
    private Integer tuteeCount;

    @Column(name = "private_memo")
    private String privateMemo;

    @Column(name = "public_memo")
    private String publicMemo;

    @Column(name = "reservation_count")
    private Integer reservationCount;

    @Column(name = "reservation_enable_yn")
    private Boolean reservationEnableYn;

    @Column(name = "reservation_auto_accept_yn")
    private Boolean reservationAutoAcceptYn;

    @Column(name = "reservation_enable_time")
    private Integer reservationEnableTime;

    @Column(name = "cancel_enable_time")
    private Integer cancelEnableTime;

    @Column(name = "min_tutee_lack_cancel_time")
    private Integer minTuteeLackCancelTime;

    @Column(name = "waiting_tutee_num")
    private Integer waitingTuteeNum;

    @Column(name = "waiting_reservation_cancel_time")
    private Integer waitingReservationCancelTime;

    @Column(name = "duplicate_reservation_yn")
    private Boolean duplicateReservationYn;

    @Column(name = "reservation_start_time")
    private String reservationStartTime;

    @Column(name = "check_in_enable_before_time")
    private Integer checkInEnableBeforeTime;

    @Column(name = "check_in_enable_after_time")
    private Integer checkInEnableAfterTime;

    @Column(name = "check_out_enable_before_time")
    private Integer checkOutEnableBeforeTime;

    @Column(name = "check_out_enable_after_time")
    private Integer checkOutEnableAfterTime;

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

    public void archiveKlass(){
        this.archiveYn = true;
    }

    public void updateArchive(Boolean archiveYn) {
        this.archiveYn = archiveYn;
    }

    public void delete() {
        this.deleteYn = true;
    }


    public ScheduleMemberTypeEnum getMemberType(){
        if (this.maxTuteeNum == 0){
            return ScheduleMemberTypeEnum.INDIVIDUAL;
        }

        return ScheduleMemberTypeEnum.GROUP;
    }

    public String getMainTutorNickname(){
        String nickName = this.mainTutor.getNickname();

        if (StringUtils.isEmpty(nickName)){
            return this.mainTutor.getUser().getName();
        }

        return nickName;
    }

    public String getSubTutorNickname(){
        String nickName = this.subTutor.getNickname();

        if (StringUtils.isEmpty(nickName)){
            return this.subTutor.getUser().getName();
        }

        return nickName;
    }

    public void updateFreeKlass(KlassDto.RequestFreeKlassDto RequestFreeKlassDto, BranchUserRole mainTutor, BranchUserRole subTutor){
        this.mainTutor = mainTutor;
        this.subTutor = subTutor;
        this.klassName = RequestFreeKlassDto.getKlassName();
        this.minTuteeNum = RequestFreeKlassDto.getMinTuteeNum();
        this.maxTuteeNum = RequestFreeKlassDto.getMaxTuteeNum();
        this.klassTime = RequestFreeKlassDto.getKlassTime();
        this.startTime = RequestFreeKlassDto.getStartTime();
        this.level = RequestFreeKlassDto.getLevel();
        this.colorCdType = RequestFreeKlassDto.getColorCdType();
        this.privateMemo = RequestFreeKlassDto.getPrivateMemo();
        this.publicMemo = RequestFreeKlassDto.getPublicMemo();
        this.reservationCount = RequestFreeKlassDto.getReservationCount();
        this.minTuteeLackCancelTime = RequestFreeKlassDto.getMinTuteeLackCancelTime();
        this.sunOperatingHours = String.join(",",RequestFreeKlassDto.getSunOperatingHours());
        this.monOperatingHours = String.join(",",RequestFreeKlassDto.getMonOperatingHours());
        this.tueOperatingHours = String.join(",",RequestFreeKlassDto.getTueOperatingHours());
        this.wedOperatingHours = String.join(",",RequestFreeKlassDto.getWedOperatingHours());
        this.thrOperatingHours = String.join(",",RequestFreeKlassDto.getThrOperatingHours());
        this.friOperatingHours = String.join(",",RequestFreeKlassDto.getFriOperatingHours());
        this.satOperatingHours = String.join(",",RequestFreeKlassDto.getSatOperatingHours());
    }

    public void updateFreeKlassSetting(KlassDto.RequestFreeKlassSettingDto RequestFreeKlassSettingDto){
        this.reservationEnableYn = RequestFreeKlassSettingDto.getReservationEnableYn();
        this.reservationEnableTime = RequestFreeKlassSettingDto.getReservationEnableTime();
        this.cancelEnableTime = RequestFreeKlassSettingDto.getCancelEnableTime();
        this.minTuteeLackCancelTime = RequestFreeKlassSettingDto.getMinTuteeLackCancelTime();
        this.waitingTuteeNum = RequestFreeKlassSettingDto.getWaitingTuteeNum();
        this.waitingReservationCancelTime = RequestFreeKlassSettingDto.getWaitingReservationCancelTime();
        this.duplicateReservationYn = RequestFreeKlassSettingDto.getDuplicateReservationYn();
        this.reservationStartTime = RequestFreeKlassSettingDto.getReservationStartTime();
        this.checkInEnableBeforeTime = RequestFreeKlassSettingDto.getCheckInEnableBeforeTime();
        this.checkInEnableAfterTime = RequestFreeKlassSettingDto.getCheckInEnableAfterTime();
        this.checkOutEnableBeforeTime = RequestFreeKlassSettingDto.getCheckOutEnableBeforeTime();
        this.checkOutEnableAfterTime = RequestFreeKlassSettingDto.getCheckOutEnableAfterTime();
    }

    public void updateLimitedKlass(KlassDto.RequestLimitedKlassDto RequestLimitedKlassDto, BranchUserRole mainTutor, BranchUserRole subTutor){
        this.mainTutor = mainTutor;
        this.subTutor = subTutor;
        this.klassName = RequestLimitedKlassDto.getKlassName();
        this.minTuteeNum = RequestLimitedKlassDto.getMinTuteeNum();
        this.maxTuteeNum = RequestLimitedKlassDto.getMaxTuteeNum();
        this.klassTime = RequestLimitedKlassDto.getKlassTime();
        this.level = RequestLimitedKlassDto.getLevel();
        this.colorCdType = RequestLimitedKlassDto.getColorCdType();
        this.privateMemo = RequestLimitedKlassDto.getPrivateMemo();
        this.publicMemo = RequestLimitedKlassDto.getPublicMemo();
        this.sunOperatingHours = String.join(",",RequestLimitedKlassDto.getSunOperatingHours());
        this.monOperatingHours = String.join(",",RequestLimitedKlassDto.getMonOperatingHours());
        this.tueOperatingHours = String.join(",",RequestLimitedKlassDto.getTueOperatingHours());
        this.wedOperatingHours = String.join(",",RequestLimitedKlassDto.getWedOperatingHours());
        this.thrOperatingHours = String.join(",",RequestLimitedKlassDto.getThrOperatingHours());
        this.friOperatingHours = String.join(",",RequestLimitedKlassDto.getFriOperatingHours());
        this.satOperatingHours = String.join(",",RequestLimitedKlassDto.getSatOperatingHours());
    }

    public void updateLimitedKlassSetting(KlassDto.RequestLimitedKlassSettingDto RequestLimitedKlassSettingDto){
        this.reservationEnableYn = RequestLimitedKlassSettingDto.getReservationEnableYn();
        this.reservationEnableTime = RequestLimitedKlassSettingDto.getReservationEnableTime();
        this.cancelEnableTime = RequestLimitedKlassSettingDto.getCancelEnableTime();
        this.minTuteeLackCancelTime = RequestLimitedKlassSettingDto.getMinTuteeLackCancelTime();
        this.waitingTuteeNum = RequestLimitedKlassSettingDto.getWaitingTuteeNum();
        this.waitingReservationCancelTime = RequestLimitedKlassSettingDto.getWaitingReservationCancelTime();
        this.duplicateReservationYn = RequestLimitedKlassSettingDto.getDuplicateReservationYn();
        this.reservationStartTime = RequestLimitedKlassSettingDto.getReservationStartTime();
        this.checkInEnableBeforeTime = RequestLimitedKlassSettingDto.getCheckInEnableBeforeTime();
        this.checkInEnableAfterTime = RequestLimitedKlassSettingDto.getCheckInEnableAfterTime();
        this.checkOutEnableBeforeTime = RequestLimitedKlassSettingDto.getCheckOutEnableBeforeTime();
        this.checkOutEnableAfterTime = RequestLimitedKlassSettingDto.getCheckOutEnableAfterTime();
    }


}
