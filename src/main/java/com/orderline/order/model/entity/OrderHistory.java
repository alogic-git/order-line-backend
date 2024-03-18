package com.orderline.order.model.entity;

import com.orderline.basic.model.entity.BaseTimeEntity;
import com.orderline.order.enums.OrderStatusEnum;
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
@DynamicInsert    //null field 지워줌
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
@Where(clause = "delete_yn = 0")
@Table(name="order_history")
public class OrderHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "site_id")
    private Long siteId;

    @Column(name = "address")
    private String address;

    @Column(name = "specifics")
    private String specifics;

    @Column(name = "manager_name")
    private String managerName;

    @Column(name = "emergency_yn")
    private Boolean emergencyYn;

    @Column(name = "total_price")
    private int totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status;

    @Column(name = "request_dt")
    private ZonedDateTime requestDt;

    @Column(name = "order_dt")
    private ZonedDateTime orderDt;

    @Column(name = "expected_dt")
    private ZonedDateTime expectedDt;

}
