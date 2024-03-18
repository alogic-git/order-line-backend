package com.orderline.material.repository;

import com.orderline.material.model.entity.MaterialHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialHistoryRepository extends JpaRepository<MaterialHistory, Long> {

    List<MaterialHistory> findByMaterialId(Long materialId);

}
