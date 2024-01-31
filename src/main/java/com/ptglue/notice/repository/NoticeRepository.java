package com.ptglue.notice.repository;

import com.ptglue.notice.model.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}