package com.ptglue.extra.repository;

import com.ptglue.extra.model.entity.ExtraProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtraProductRepository  extends JpaRepository<ExtraProduct, Long> {
}
