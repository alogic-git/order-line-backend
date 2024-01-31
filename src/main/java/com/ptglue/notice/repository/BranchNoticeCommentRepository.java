package com.ptglue.notice.repository;

import com.ptglue.notice.model.entity.BranchNoticeComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchNoticeCommentRepository extends JpaRepository<BranchNoticeComment, Long> {
    Page<BranchNoticeComment> findAllByBranchNoticeId(Long branchNoticeId, Pageable pageable);
}
