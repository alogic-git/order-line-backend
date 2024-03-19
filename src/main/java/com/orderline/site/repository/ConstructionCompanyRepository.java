package com.orderline.site.repository;

import com.orderline.site.model.entity.ConstructionCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConstructionCompanyRepository extends JpaRepository<ConstructionCompany, Long> {

}
