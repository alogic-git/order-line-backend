package com.orderline.schedule.repository;

import com.orderline.schedule.enums.ReservationStatusTypeEnum;
import com.orderline.schedule.model.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Page<Reservation> findByTuteeId(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"klass", "schedule"})
    @Query(value =
            "SELECT r FROM Reservation r " +
                    "WHERE r.branch.id = :branchId " +
                    "AND r.tutee.phone = :phone " +
                    "AND r.statusType = :statusType " +
                    "AND r.schedule.startDt > :todayStartDt " +
                    "AND r.schedule.startDt < :todayEndDt ")
    List<Reservation> findTodayReservation(
            @Param("branchId") Long branchId,
            @Param("phone") String phone,
            @Param("statusType") ReservationStatusTypeEnum statusType,
            @Param("todayStartDt") ZonedDateTime todayStartDt,
            @Param("todayEndDt") ZonedDateTime todayEndDt);

    Page<Reservation> findByTicketIdAndTuteeId(Long ticketId, Long tuteeId, Pageable pageable);

    Integer countByTicketIdAndTuteeIdAndStatusType(Long ticketId, Long tuteeId, ReservationStatusTypeEnum statusType);

    @EntityGraph(attributePaths = {"schedule", "ticket"})
    Page<Reservation> findByBranchIdAndTuteeId(Long branchId, Long userId, Pageable pageable);
  
    @EntityGraph(attributePaths = {"schedule", "ticket"})
    List<Reservation> findByScheduleIdAndIdIn(Long scheduleId, List<Long> reservationIds);

    Page<Reservation> findByScheduleId(Long scheduleId, Pageable pageable);

    List<Reservation> findByScheduleId(Long scheduleId);

    Optional<Reservation> findByScheduleIdAndTuteeId(Long scheduleId, Long tuteeId);

    List<Reservation> findByRepeatReservationIdAndStatusTypeIn(Long repeatReservationId, List<ReservationStatusTypeEnum> statusTypeList);

    Optional<Reservation> findOneByRepeatReservationId(Long repeatReservationId);

    List<Reservation> findByRepeatReservationId(Long repeatReservationId);
}


