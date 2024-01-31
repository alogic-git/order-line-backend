package com.orderline.ticket.model.entity;

import com.orderline.basic.model.entity.BaseTimeEntity;
import com.orderline.common.user.model.entity.User;
import com.orderline.product.model.entity.Product;
import com.orderline.ticket.enums.TicketPaidStatusTypeEnum;
import com.orderline.ticket.enums.TicketStatusTypeEnum;
import com.orderline.ticket.model.dto.TicketDto;
import com.orderline.branch.model.entity.Branch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

@DynamicUpdate
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
@Where(clause = "delete_yn = 0")
@Table(name = "ticket")
public class Ticket extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "ticket_name")
    private String ticketName;

    @Column(name = "period")
    private Integer period;

    @Column(name = "price")
    private Integer price;

    @Column(name = "total_paid_price")
    private Integer totalPaidPrice;

    @Column(name = "total_refund_price")
    private Integer totalRefundPrice;

    @Column(name = "available_reservation_count")
    private Integer availableReservationCount;

    @Column(name = "available_other_branch_reservation_count")
    private Integer availableOtherBranchReservationCount;

    @Column(name = "remain_reservation_count")
    private Integer remainReservationCount;

    @Column(name = "remain_other_branch_reservation_count")
    private Integer remainOtherBranchReservationCount;

    @Column(name = "total_reservation_count")
    private Integer totalReservationCount;

    @Column(name = "total_other_branch_reservation_count")
    private Integer totalOtherBranchReservationCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_type")
    private TicketStatusTypeEnum statusType;

    @Enumerated(EnumType.STRING)
    @Column(name = "paid_status_type")
    private TicketPaidStatusTypeEnum paidStatusType;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "private_memo")
    private String privateMemo;

    @Column(name = "public_memo")
    private String publicMemo;

    @Column(name = "stop_enable_yn")
    private Boolean stopEnableYn;

    @Column(name = "stop_enable_date")
    private Integer stopEnableDate;

    @Column(name = "stop_enable_total_date")
    private Integer stopEnableTotalDate;

    @Column(name = "stop_enable_count")
    private Integer stopEnableCount;

    @Column(name = "current_stop_enable_total_date")
    private Integer currentStopEnableTotalDate;

    @Column(name = "current_stop_enable_count")
    private Integer currentStopEnableCount;

    @Column(name = "archive_yn")
    private Boolean archiveYn;

    @Formula("(SELECT count(*) FROM product_klass WHERE product_klass.product_id = product_id AND delete_yn = 0)")
    private Integer innerKlass;

    public void archiveTicket() { this.archiveYn = true; }

    public void extendEndDate(Integer extendDays) { this.endDate = this.endDate.plusDays(extendDays); }

    public void decreaseRemainReservationCount(Integer reservationCount) {
        if (this.remainReservationCount >= reservationCount){
            this.remainReservationCount -= reservationCount;
        }else {
            this.remainReservationCount = -1;
        }
    }

    public void decreaseRemainOtherBranchReservationCount(Integer reservationCount) {
        if (this.remainOtherBranchReservationCount >= reservationCount){
            this.remainOtherBranchReservationCount -= reservationCount;
        }else {
            this.remainOtherBranchReservationCount = -1;
        }
    }

    public void increaseAvailableReservationCount(Integer reservationCount) {
        this.availableReservationCount += reservationCount;
    }

    public void increaseAvailableOtherBranchReservationCount(Integer reservationCount) {
        this.availableOtherBranchReservationCount += reservationCount;
    }

    public void updateTicket(TicketDto.RequestTicketDto requestTicketDto, Branch branch, Product product, User user){
        this.branch = branch;
        this.product = product;
        this.user = user;
        this.ticketName = requestTicketDto.getTicketName();
        this.period = requestTicketDto.getPeriod();
        this.price = requestTicketDto.getPrice();
        this.totalPaidPrice = requestTicketDto.getTotalPaidPrice();
        this.totalRefundPrice = requestTicketDto.getTotalRefundPrice();
        this.availableReservationCount = requestTicketDto.getAvailableReservationCount();
        this.remainReservationCount = requestTicketDto.getRemainReservationCount();
        this.totalReservationCount = requestTicketDto.getTotalReservationCount();
        this.statusType = requestTicketDto.getStatusType();
        this.paidStatusType = requestTicketDto.getPaidStatusType();
        this.startDate = requestTicketDto.getStartDate();
        this.endDate = requestTicketDto.getEndDate();
        this.privateMemo = requestTicketDto.getPrivateMemo();
        this.publicMemo = requestTicketDto.getPublicMemo();
        this.stopEnableYn = requestTicketDto.getStopEnableYn();
        this.stopEnableDate = requestTicketDto.getStopEnableDate();
        this.stopEnableTotalDate = requestTicketDto.getStopEnableTotalDate();
        this.stopEnableCount = requestTicketDto.getStopEnableCount();
        this.currentStopEnableTotalDate = requestTicketDto.getCurrentStopEnableTotalDate();
        this.currentStopEnableCount = requestTicketDto.getCurrentStopEnableCount();
    }

}
