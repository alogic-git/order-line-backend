package com.orderline.schedule.model.entity;


import com.orderline.schedule.model.dto.DailyRecordDto;
import com.orderline.basic.model.entity.BaseTimeEntity;
import com.orderline.branch.model.entity.Branch;
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
@Table(name="daily_record")
public class DailyRecord extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column(name = "note")
    private String note;

    @Column(name = "archive_yn")
    private Boolean archiveYn;

    public void archiveDailyRecord(){
        this.archiveYn = true;
    }

    public void updateDailyRecord(DailyRecordDto.RequestDailyRecordDto requestDailyRecordDto, Reservation reservation) {
        this.reservation = reservation;
        this.note = requestDailyRecordDto.getNote();
    }
}
