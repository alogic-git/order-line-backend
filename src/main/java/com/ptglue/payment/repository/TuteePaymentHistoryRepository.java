package com.ptglue.payment.repository;

import com.ptglue.payment.model.entity.TuteePaymentHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TuteePaymentHistoryRepository extends JpaRepository<TuteePaymentHistory, Long> {
    List<TuteePaymentHistory> findByTicketId(Long ticketId);

    Page<TuteePaymentHistory> findByTicketIdIn(List<Long> ticketIdList, Pageable pageable);
}
