package com.ptglue.notice.repository;

import com.ptglue.notice.model.entity.BranchNoticeReadHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchNoticeReadHistoryRepository extends JpaRepository<BranchNoticeReadHistory, Long> {
    Boolean existsByBranchNoticeIdAndUserId(Long branchNoticeId, Long userId);
}
