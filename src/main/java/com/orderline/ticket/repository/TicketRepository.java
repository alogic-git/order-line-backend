package com.orderline.ticket.repository;

import com.orderline.ticket.enums.TicketStatusTypeEnum;
import com.orderline.ticket.model.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Page<Ticket> findByBranchIdAndUserIdAndArchiveYn(Long branchId, Long userId, Boolean archiveYn, Pageable pageable);

    Page<Ticket> findByUserIdAndStatusType(Long userId, TicketStatusTypeEnum statusType, Pageable pageable);

    Page<Ticket> findByUserId(Long userId, Pageable pageable);

    List<Ticket> findByUserId(Long userId);

    Integer countByUserId(Long userId);

    List<Ticket> findByBranchIdAndStatusTypeIn(Long branchId, List<TicketStatusTypeEnum> statusTypes);
}
