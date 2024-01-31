package com.orderline.ticket.repository;

import com.orderline.ticket.enums.TicketStatusTypeEnum;
import com.orderline.ticket.model.entity.TicketPauseHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketPauseHistoryRepository extends JpaRepository<TicketPauseHistory,Long> {
    Page<TicketPauseHistory> findByTicketId(Long ticketId, Pageable pageable);

    Page<TicketPauseHistory> findByBranchIdAndTicket_User_idAndTicket_StatusType(Long branchId, Long userId, TicketStatusTypeEnum statusType, Pageable pageable);
  
    Page<TicketPauseHistory> findByBranchIdAndTicket_User_id(Long branchId, Long userId, Pageable pageable);
}
