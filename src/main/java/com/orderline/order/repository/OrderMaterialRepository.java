package com.orderline.order.repository;

import com.orderline.order.model.entity.OrderMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMaterialRepository extends JpaRepository<OrderMaterial, Long> {
}
