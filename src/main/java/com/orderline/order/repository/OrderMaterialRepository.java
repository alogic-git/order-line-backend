package com.orderline.order.repository;

import com.orderline.material.model.entity.Material;
import com.orderline.order.model.entity.OrderMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMaterialRepository extends JpaRepository<OrderMaterial, Long> {
    public List<OrderMaterial> findByMaterial(Material material);
}
