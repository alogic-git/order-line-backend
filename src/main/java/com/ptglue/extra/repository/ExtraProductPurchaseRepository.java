package com.ptglue.extra.repository;

import com.ptglue.extra.model.entity.ExtraProductPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtraProductPurchaseRepository extends JpaRepository<ExtraProductPurchase, Long> {
}
