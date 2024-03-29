package com.orderline.material.model.entity;

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
@Table(name="material_history")
public class MaterialHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "material_id")
    private Long materialId;

    @Column(name = "name")
    private String name;

    @Column(name = "specifics")
    private String specifics;

    @Column(name = "quantity")
    private int quantity;

    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status;

    @Column(name = "total_price")
    private int totalPrice;

    @Column(name = "request_dt")
    private ZonedDateTime requestDt;

    @Column(name = "expected_dt")
    private ZonedDateTime expectedDt;
}
