package com.orderline.schedule.model.entity;


import com.orderline.schedule.model.dto.ReservationDto;
import com.orderline.basic.model.entity.BaseTimeEntity;
import com.orderline.branch.model.entity.Branch;
import com.orderline.common.user.model.entity.User;
import com.orderline.klass.model.entity.Klass;
import com.orderline.schedule.enums.ReservationStatusTypeEnum;
import com.orderline.ticket.model.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Table(name="reservation")
public class Reservation extends BaseTimeEntity{

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
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repeat_reservation_id")
    private RepeatReservation repeatReservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutee_id")
    private User tutee;

    @Column(name = "attendance_dt")
    private ZonedDateTime attendanceDt;

    @Column(name = "tutee_num")
    private Integer tuteeNum;

    @Column(name = "reservation_count")
    private Integer reservationCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_type")
    private ReservationStatusTypeEnum statusType;

    @Column(name = "public_memo")
    private String publicMemo;

    @Column(name = "cancel_yn")
    private Boolean cancelYn;

    public Reservation toEntityByRepeated(Schedule schedule, Ticket ticket, User tutee, RepeatReservation repeatReservation){
        return Reservation.builder()
                .branch(schedule.getBranch())
                .klass(schedule.getKlass())
                .schedule(schedule)
                .ticket(ticket)
                .tutee(tutee)
                .repeatReservation(repeatReservation)
                .tuteeNum(schedule.getCurrentTuteeNum())
                .reservationCount(this.reservationCount)
                .statusType(ReservationStatusTypeEnum.CONFIRMATION)
                .publicMemo(this.publicMemo)
                .cancelYn(false)
                .build();
    }


    public void cancelReservation(){
        this.cancelYn = true;
    }

    public void updateReservationStatus(ReservationStatusTypeEnum statusType){
        this.statusType = statusType;
    }

    public void updateReservation(ReservationDto.RequestReservationDto requestReservationDto, Ticket ticket ,User tutee) {
        this.ticket = ticket;
        this.tutee = tutee;
        this.statusType = requestReservationDto.getStatusType();
        this.publicMemo = requestReservationDto.getPublicMemo();
    }

    public void updateStatus(ReservationStatusTypeEnum statusType){
        this.statusType = statusType;
    }

    public void updateAttendanceDt(ZonedDateTime attendanceDt){
        this.attendanceDt = attendanceDt;
    }
}
