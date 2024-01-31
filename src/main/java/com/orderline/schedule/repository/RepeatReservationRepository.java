package com.orderline.schedule.repository;

import com.orderline.schedule.enums.RepeatScheduleStatusTypeEnum;
import com.orderline.schedule.model.entity.RepeatReservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepeatReservationRepository extends JpaRepository<RepeatReservation, Long> {

    Page<RepeatReservation> findByBranchIdAndTuteeId(Long branchId, Long tuteeId, Pageable pageable);

    Page<RepeatReservation> findByBranchIdAndTuteeIdAndStatusType(Long branchId, Long tuteeId, RepeatScheduleStatusTypeEnum statusType, Pageable pageable);
}