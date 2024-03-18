package com.orderline.material.repository;

import com.orderline.material.model.entity.Material;
import com.orderline.order.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findByOrder(Order order);
}
