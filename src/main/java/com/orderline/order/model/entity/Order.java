package com.orderline.order.model.entity;

import com.orderline.basic.model.entity.BaseTimeEntity;
import com.orderline.basic.utils.TimeFunction;
import com.orderline.material.model.dto.ProductDto;
import com.orderline.order.enums.OrderStatusEnum;
import com.orderline.order.model.dto.OrderDto;
import com.orderline.site.model.entity.Site;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
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
@Table(name="orders")
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id")
    private Site site;

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

    public void deleteOrder(){
        this.deleteYn = true;
    }

    public void updateExpectedDt(ZonedDateTime expectedDt){
        this.expectedDt = expectedDt;
    }

    public void updateTotalPrice(int totalPrice){
        this.totalPrice = totalPrice;
    }

    public void updateAddress(String address){
        this.address = address;
    }

    public void updateSpecifics(String specifics){
        this.specifics = specifics;
    }

    public void updateOrder(OrderDto.RequestCreateOrderDto requestOrderDto){
        this.address = requestOrderDto.getAddress();
        this.specifics = requestOrderDto.getSpecifics();
        this.managerName = requestOrderDto.getManagerName();
        this.emergencyYn = requestOrderDto.getEmergencyYn();
        this.status = requestOrderDto.getStatus();
        this.requestDt = TimeFunction.toZonedDateTime(requestOrderDto.getRequestDt());
        this.orderDt = TimeFunction.toZonedDateTime(requestOrderDto.getOrderDt());
        this.expectedDt = TimeFunction.toZonedDateTime(requestOrderDto.getExpectedDt());
    }
}