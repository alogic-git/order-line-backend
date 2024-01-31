package com.ptglue.schedule.repository;

import com.ptglue.schedule.model.entity.DailyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyRecordRepository extends JpaRepository<DailyRecord, Long> {

}
