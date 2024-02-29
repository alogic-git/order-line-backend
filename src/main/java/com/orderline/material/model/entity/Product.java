package com.orderline.material.model.entity;

import com.orderline.basic.model.entity.BaseTimeEntity;
import com.orderline.basic.utils.TimeFunction;
import com.orderline.material.enums.ProductStatusEnum;
import com.orderline.material.model.dto.MaterialDto;
import com.orderline.material.model.dto.ProductDto;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;

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

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "name")
    private String name;

    @Column(name = "unit_price")
    private int unitPrice;

    @Column(name = "sale_price")
    private int salePrice;

    @Column(name = "available_stock")
    private int availableStock;

    @Column(name = "color")
    private String color;

    @Column(name = "image_uri")
    private String imageUri;

    @Column(name = "specifics")
    private String specifics;

    @Enumerated(EnumType.STRING)
    private ProductStatusEnum status;

    @Column(name = "model_number")
    private String modelNumber;

    public void deleteProduct(){
        this.deleteYn = true;
    }

    public void updateName(String name){
        this.name = name;
    }

    public void updateSpecifics(String specifics){
        this.specifics = specifics;
    }

    public void updateProduct(ProductDto.RequestCreateProductDto requestProductDto){
        this.name = requestProductDto.getName();
        this.companyName = requestProductDto.getCompanyName();
        this.modelNumber = requestProductDto.getModelNumber();
        this.unitPrice = requestProductDto.getUnitPrice();
        this.specifics = requestProductDto.getSpecifics();
        this.salePrice = requestProductDto.getSalePrice();
        this.availableStock = requestProductDto.getAvailableStock();
        this.color = requestProductDto.getColor();
        this.imageUri = requestProductDto.getImageUri();
    }
}