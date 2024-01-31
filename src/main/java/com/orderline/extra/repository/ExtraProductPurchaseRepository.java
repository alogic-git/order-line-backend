package com.orderline.extra.repository;

import com.orderline.extra.model.entity.ExtraProductPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtraProductPurchaseRepository extends JpaRepository<ExtraProductPurchase, Long> {
}
