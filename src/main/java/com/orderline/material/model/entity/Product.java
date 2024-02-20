package com.orderline.order.model.entity;

import com.orderline.basic.model.entity.BaseTimeEntity;
import com.orderline.material.enums.ProductStatusEnum;
import com.orderline.material.model.entity.MaterialCompany;
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
@Table(name="product")
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private MaterialCompany materialCompany;

    @Column(name = "name")
    private String name;

    @Column(name = "unit_price")
    private int unitPrice;

    @Column(name = "sale_price")
    private int salePrice;

    @Column(name = "available_stock")
    private int availableStock;

    private String color;

    @Column(name = "image_uri")
    private String imageUri;

    private String specifics;

    private int quantity;

    @Enumerated(EnumType.STRING)
    private ProductStatusEnum status;

    @Column(name = "model_number")
    private int modelNumber;

    @Column(name = "request_dt")
    private ZonedDateTime requestDt;

    @Column(name = "expected_dt")
    private ZonedDateTime expectedDt;

    public void deleteProduct(){
        this.deleteYn = true;
    }

    public void updateName(String name){
        this.name = name;
    }

    public void updateSpecifics(String specifics){
        this.specifics = specifics;
    }

}