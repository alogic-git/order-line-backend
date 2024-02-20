package com.orderline.order.model.entity;

import com.orderline.basic.model.entity.BaseTimeEntity;
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
@Table(name="order")
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    private String address;

    private String specifics;

    @Column(name = "manager_name")
    private String managerName;

    @Column(name = "emergency_yn")
    private Boolean emergencyYn;

    @Column(name = "total_price")
    private int totalPrice;

    @Column(name = "order_dt")
    private ZonedDateTime orderDt;

    public void deleteOrder(){
        this.deleteYn = true;
    }

    public void updateName(String name){
        this.name = name;
    }

    public void updateAddress(String address){
        this.address = address;
    }

    public void updateSpecifics(String specifics){
        this.specifics = specifics;
    }

}