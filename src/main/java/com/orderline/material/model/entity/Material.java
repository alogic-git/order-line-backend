package com.orderline.material.model.entity;

import com.orderline.basic.model.entity.BaseTimeEntity;
import com.orderline.basic.utils.TimeFunction;
import com.orderline.material.model.dto.MaterialDto;
import com.orderline.order.enums.OrderStatusEnum;
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
@Table(name="material")
public class Material extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

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

    public void deleteMaterial(){
        this.deleteYn = true;
    }

    public void updateName(String name){
        this.name = name;
    }

    public void updateSpecifics(String specifics){
        this.specifics = specifics;
    }

    public void updateMaterial(MaterialDto.RequestUpdateMaterialDto requestMaterialDto, Product product){
        this.name = product.getName();
        this.quantity = requestMaterialDto.getQuantity();
        this.specifics = requestMaterialDto.getSpecifics();
        this.totalPrice = product.getUnitPrice() * quantity;
        this.status = requestMaterialDto.getStatus();
        this.requestDt = TimeFunction.toZonedDateTime(requestMaterialDto.getRequestDt());
        this.expectedDt = TimeFunction.toZonedDateTime(requestMaterialDto.getExpectedDt());
    }

}