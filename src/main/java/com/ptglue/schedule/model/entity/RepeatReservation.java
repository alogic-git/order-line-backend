package com.ptglue.schedule.model.entity;

import com.ptglue.basic.model.entity.BaseTimeEntity;
import com.ptglue.branch.model.entity.Branch;
import com.ptglue.common.user.model.entity.User;
import com.ptglue.schedule.enums.RepeatScheduleStatusTypeEnum;
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
@Table(name="repeat_reservation")
public class RepeatReservation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutee_id")
    private User tutee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repeat_schedule_id")
    private RepeatSchedule repeatSchedule;

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

    public void deleteRepeatReservation(){
        deleteYn = true;
    }
}
