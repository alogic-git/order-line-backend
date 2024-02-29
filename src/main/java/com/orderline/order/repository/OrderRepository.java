package com.orderline.order.repository;

import com.orderline.order.model.entity.Order;
import com.orderline.site.model.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findBySiteIn(List<Site> siteList);
}
