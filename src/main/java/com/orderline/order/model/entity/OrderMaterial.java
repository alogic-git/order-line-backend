package com.orderline.order.model.entity;

import com.orderline.basic.model.entity.BaseTimeEntity;
import com.orderline.material.model.entity.Material;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Table(name="order_material")
public class OrderMaterial extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id")
    private Material material;

    public void deleteOrderMaterial(){
        this.deleteYn = true;
    }
}
