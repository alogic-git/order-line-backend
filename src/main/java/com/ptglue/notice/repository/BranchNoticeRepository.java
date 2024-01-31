package com.ptglue.notice.repository;

import com.ptglue.notice.model.entity.BranchNotice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BranchNoticeRepository extends JpaRepository<BranchNotice, Long> {
    Page<BranchNotice> findByBranchId(Long branchId, Pageable pageable);
}
