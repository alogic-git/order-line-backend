package com.orderline.schedule.model.entity;

import com.orderline.basic.model.entity.BaseTimeEntity;
import com.orderline.basic.utils.TimeFunction;
import com.orderline.branch.model.entity.Branch;
import com.orderline.common.user.model.entity.User;
import com.orderline.klass.enums.KlassColorTypeEnum;
import com.orderline.klass.enums.KlassLevelTypeEnum;
import com.orderline.klass.model.entity.Klass;
import com.orderline.schedule.enums.ScheduleMemberTypeEnum;
import com.orderline.schedule.enums.ScheduleStatusTypeEnum;
import com.orderline.schedule.enums.ScheduleTypeEnum;
import com.orderline.schedule.model.dto.ScheduleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.ZonedDateTime;

@DynamicUpdate
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Where(clause = "delete_yn = 0")
@Entity
@Table(name="schedule")
public class Schedule extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "klass_id")
    private Klass klass;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repeat_schedule_id")
    private RepeatSchedule repeatSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_tutor_id")
    private User mainTutor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_tutor_id")
    private User subTutor;

    @Column(name = "klass_name")
    private String klassName;

    @Column(name = "start_dt")
    private ZonedDateTime startDt;

    @Column(name = "end_dt")
    private ZonedDateTime endDt;

    @Column(name = "min_tutee_num")
    private Integer minTuteeNum;

    @Column(name = "max_tutee_num")
    private Integer maxTuteeNum;

    @Column(name = "current_tutee_num")
    private Integer currentTuteeNum;

    @Column(name = "waiting_current_tutee_num")
    private Integer waitingCurrentTuteeNum;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_type")
    private ScheduleStatusTypeEnum statusType;

    @Column(name = "private_yn")
    private Boolean privateYn;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_type")
    private ScheduleTypeEnum scheduleType;

    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private KlassLevelTypeEnum level;

    @Enumerated(EnumType.STRING)
    @Column(name = "color_cd_type")
    private KlassColorTypeEnum colorCdType;

    @Column(name = "private_memo")
    private String privateMemo;

    @Column(name = "public_memo")
    private String publicMemo;

    @Column(name = "reservation_enable_yn")
    private Boolean reservationEnableYn;

    @Column(name = "reservation_enable_time")
    private Integer reservationEnableTime;

    @Column(name = "cancel_enable_time")
    private Integer cancelEnableTime;

    @Column(name = "min_tutee_lack_cancel_time")
    private Integer minTuteeLackCancelTime;

    @Column(name = "cancel_yn")
    private Boolean cancelYn;

    public void cancelSchedule(){
        this.cancelYn = true;
    }

    public String getMainTutorName(){
        String nickName = this.klass.getMainTutor().getNickname();

        if (StringUtils.isEmpty(nickName)){
            return this.mainTutor.getName();
        }

        return nickName;
    }

    public String getSubTutorName(){
        String nickName = this.klass.getSubTutor().getNickname();

        if (StringUtils.isEmpty(nickName)){
            return this.subTutor.getName();
        }

        return nickName;
    }

    public ScheduleMemberTypeEnum getScheduleMemberType(){
        if (this.maxTuteeNum == 1){
            return ScheduleMemberTypeEnum.INDIVIDUAL;
        }

        return ScheduleMemberTypeEnum.GROUP;
    }

    public void decreaseCurrentTuteeNum(Integer tuteeNum){
        if (this.currentTuteeNum > tuteeNum){
            this.currentTuteeNum -= tuteeNum;
        } else {
            this.currentTuteeNum = 0;
        }
    }

    public void increaseCurrentTuteeNum(Integer tuteeNum){
        this.currentTuteeNum += tuteeNum;
    }

    public void decreaseWaitingCurrentTuteeNum(Integer tuteeNum){
        if (this.waitingCurrentTuteeNum > tuteeNum) {
            this.waitingCurrentTuteeNum -= tuteeNum;
        } else {
            this.waitingCurrentTuteeNum = 0;
        }
    }

    public void increaseWaitingCurrentTuteeNum(Integer tuteeNum){
        this.waitingCurrentTuteeNum += tuteeNum;
    }

    public void updateSchedule(ScheduleDto.RequestScheduleDto requestScheduleDto, User mainTutor, User subTutor){
        this.mainTutor = mainTutor;
        this.subTutor = subTutor;
        this.startDt = TimeFunction.toZonedDateTime(requestScheduleDto.getStartDt());
        this.endDt = TimeFunction.toZonedDateTime(requestScheduleDto.getEndDt());
        this.statusType = requestScheduleDto.getStatusType();
        this.privateYn = requestScheduleDto.getPrivateYn();
        this.privateMemo = requestScheduleDto.getPrivateMemo();
        this.publicMemo = requestScheduleDto.getPublicMemo();
        this.reservationEnableYn = requestScheduleDto.getReservationEnableYn();
        this.reservationEnableTime = requestScheduleDto.getReservationEnableTime();
        this.cancelEnableTime = requestScheduleDto.getCancelEnableTime();
        this.minTuteeLackCancelTime = requestScheduleDto.getMinTuteeLackCancelTime();
    }

}
