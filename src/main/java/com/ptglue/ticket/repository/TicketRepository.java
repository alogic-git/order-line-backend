package com.ptglue.ticket.repository;

import com.ptglue.ticket.enums.TicketStatusTypeEnum;
import com.ptglue.ticket.model.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Page<Ticket> findByBranchIdAndUserIdAndArchiveYn(Long branchId, Long userId, Boolean archiveYn, Pageable pageable);

    Page<Ticket> findByUserIdAndStatusType(Long userId, TicketStatusTypeEnum statusType, Pageable pageable);

    Page<Ticket> findByUserId(Long userId, Pageable pageable);

    List<Ticket> findByUserId(Long userId);

    Integer countByUserId(Long userId);

    List<Ticket> findByBranchIdAndStatusTypeIn(Long branchId, List<TicketStatusTypeEnum> statusTypes);
}
