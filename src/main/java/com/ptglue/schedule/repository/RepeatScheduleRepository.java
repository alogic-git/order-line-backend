package com.ptglue.schedule.repository;

import com.ptglue.schedule.model.entity.RepeatSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepeatScheduleRepository extends JpaRepository<RepeatSchedule, Long> {

    Page<RepeatSchedule> findByKlassId(Long klassId, Pageable pageable);

}