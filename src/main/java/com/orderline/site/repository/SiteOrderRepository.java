package com.orderline.site.repository;

import com.orderline.site.model.entity.Site;
import com.orderline.site.model.entity.SiteOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SiteOrderRepository extends JpaRepository<SiteOrder, Long> {
    List<SiteOrder> findBySiteIn(List<Site> siteList);
}
