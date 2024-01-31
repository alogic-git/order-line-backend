package com.ptglue.schedule.repository;

import com.ptglue.schedule.model.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByBranchIdAndMainTutorIdAndStartDtAndEndDt(Long branchId, Long mainTutorId ,ZonedDateTime startDt, ZonedDateTime endDt);

    Page<Schedule> findByBranchId(@Param("branchId") Long branchId,  Pageable pageable);

    List<Schedule> findByBranchIdAndStartDtGreaterThanEqualAndStartDtLessThanEqual(Long branchId, ZonedDateTime startDt, ZonedDateTime endDt);

    Page<Schedule> findByKlassId(Long klassId, Pageable pageable);

    List<Schedule> findByBranchIdAndStartDtBetweenOrEndDtBetweenOrStartDtLessThanEqualAndEndDtGreaterThanEqual(Long branchId,ZonedDateTime startDt1, ZonedDateTime endDt1, ZonedDateTime startDt2, ZonedDateTime endDt2, ZonedDateTime startDt3, ZonedDateTime endDt3);

    List<Schedule> findByBranchIdAndMainTutorIdAndStartDtGreaterThanEqualAndStartDtLessThanEqual(Long branchId, Long mainTutorId ,ZonedDateTime startDt, ZonedDateTime endDt);

    List<Schedule> findByBranchIdAndAndStartDtGreaterThanEqualAndStartDtLessThanEqual(Long branchId, ZonedDateTime startDt, ZonedDateTime endDt);
}