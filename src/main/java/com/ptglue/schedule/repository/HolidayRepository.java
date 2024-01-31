package com.ptglue.schedule.repository;

import com.ptglue.schedule.model.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    List<Holiday> findByHolidayDateGreaterThanEqualAndHolidayDateLessThanEqual(ZonedDateTime startDt, ZonedDateTime endDt);
}
