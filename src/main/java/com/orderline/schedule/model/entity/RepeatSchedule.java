package com.orderline.schedule.model.entity;

import com.orderline.schedule.enums.RepeatScheduleStatusTypeEnum;
import com.orderline.schedule.model.dto.RepeatScheduleDto;
import com.orderline.basic.model.entity.BaseTimeEntity;
import com.orderline.branch.model.entity.Branch;
import com.orderline.klass.model.entity.Klass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

@DynamicUpdate
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Where(clause = "delete_yn = 0")
@Entity
@Table(name="repeat_schedule")
public class RepeatSchedule extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "klass_id")
    private Klass klass;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_type")
    private RepeatScheduleStatusTypeEnum statusType;

    @Column(name = "sun_start_end_time")
    private String sunStartEndTime;

    @Column(name = "mon_start_end_time")
    private String monStartEndTime;

    @Column(name = "tue_start_end_time")
    private String tueStartEndTime;

    @Column(name = "wed_start_end_time")
    private String wedStartEndTime;

    @Column(name = "thr_start_end_time")
    private String thrStartEndTime;

    @Column(name = "fri_start_end_time")
    private String friStartEndTime;

    @Column(name = "sat_start_end_time")
    private String satStartEndTime;

    @Column(name = "archive_yn")
    private Boolean archiveYn;

    public void archiveRepeatSchedule(){
        this.archiveYn = true;
    }

    public void updateRepeatSchedule(RepeatScheduleDto.RequestRepeatScheduleDto requestRepeatScheduleDto, Klass klass) {
        this.klass = klass;
        this.startDate = requestRepeatScheduleDto.getStartDate();
        this.endDate = requestRepeatScheduleDto.getEndDate();
        this.statusType = requestRepeatScheduleDto.getStatusType();
        this.sunStartEndTime = String.join(",", requestRepeatScheduleDto.getSunStartEndTime());
        this.monStartEndTime = String.join(",", requestRepeatScheduleDto.getMonStartEndTime());
        this.tueStartEndTime = String.join(",", requestRepeatScheduleDto.getTueStartEndTime());
        this.wedStartEndTime = String.join(",", requestRepeatScheduleDto.getWedStartEndTime());
        this.thrStartEndTime = String.join(",", requestRepeatScheduleDto.getThrStartEndTime());
        this.friStartEndTime = String.join(",", requestRepeatScheduleDto.getFriStartEndTime());
        this.satStartEndTime = String.join(",", requestRepeatScheduleDto.getSatStartEndTime());
    }
}