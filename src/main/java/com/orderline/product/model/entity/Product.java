package com.orderline.product.model.entity;

import com.orderline.basic.model.entity.BaseTimeEntity;
import com.orderline.branch.model.entity.Branch;
import com.orderline.product.model.dto.ProductDto;
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
@Table(name = "product")
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "reservation_count")
    private Integer reservationCount;

    @Column(name = "period")
    private Integer period;

    @Column(name = "price")
    private Integer price;

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

    @Column(name = "daily_duplicate_reservation_num")
    private Integer dailyDuplicateReservationNum;

    @Column(name = "daily_reservation_enable_num")
    private Integer dailyReservationEnableNum;

    @Column(name = "daily_cancel_enable_num")
    private Integer dailyCancelEnableNum;

    @Column(name = "weekly_reservation_enable_num")
    private Integer weeklyReservationEnableNum;

    @Column(name = "weekly_cancel_enable_num")
    private Integer weeklyCancelEnableNum;

    @Column(name = "auto_finish_yn")
    private Boolean autoFinishYn;

    @Column(name = "archive_yn")
    private Boolean archiveYn;

    public void archiveProduct(){
        this.archiveYn = true;
    }

    public void updateArchive(Boolean archiveYn) {
        this.archiveYn = archiveYn;
    }

    public void delete() {
        this.deleteYn = true;
    }

    public void updateProduct(ProductDto.RequestProductDto requestProductDto){
        this.productName = requestProductDto.getProductName();
        this.reservationCount = requestProductDto.getReservationCount();
        this.period = requestProductDto.getPeriod();
        this.price = requestProductDto.getPrice();
        this.privateMemo = requestProductDto.getPrivateMemo();
        this.publicMemo = requestProductDto.getPublicMemo();
        this.stopEnableYn = requestProductDto.getStopEnableYn();
        this.stopEnableDate = requestProductDto.getStopEnableDate();
        this.stopEnableTotalDate = requestProductDto.getStopEnableTotalDate();
        this.stopEnableCount = requestProductDto.getStopEnableCount();
        this.dailyDuplicateReservationNum = requestProductDto.getDailyDuplicateReservationNum();
        this.dailyReservationEnableNum = requestProductDto.getDailyReservationEnableNum();
        this.dailyCancelEnableNum = requestProductDto.getDailyCancelEnableNum();
        this.weeklyReservationEnableNum = requestProductDto.getWeeklyReservationEnableNum();
        this.weeklyCancelEnableNum = requestProductDto.getWeeklyCancelEnableNum();
        this.autoFinishYn = requestProductDto.getAutoFinishYn();
    }
}
