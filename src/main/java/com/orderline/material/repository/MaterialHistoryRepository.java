package com.orderline.material.repository;

import com.orderline.material.model.entity.MaterialHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialHistoryRepository extends JpaRepository<MaterialHistory, Long> {

}
