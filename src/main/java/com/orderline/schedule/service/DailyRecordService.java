package com.orderline.schedule.service;

import com.orderline.basic.exception.NotFoundException;
import com.orderline.branch.model.entity.Branch;
import com.orderline.branch.repository.BranchRepository;
import com.orderline.schedule.model.dto.DailyRecordDto;
import com.orderline.schedule.model.entity.DailyRecord;
import com.orderline.schedule.model.entity.Reservation;
import com.orderline.schedule.repository.DailyRecordRepository;
import com.orderline.schedule.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class DailyRecordService {
    @Resource(name = "dailyRecordRepository")
    private DailyRecordRepository dailyRecordRepository;

    @Resource(name = "reservationRepository")
    private ReservationRepository reservationRepository;

    @Resource(name = "branchRepository")
    private BranchRepository branchRepository;

    public DailyRecordDto.ResponseDailyRecordDto get(Long dailyRecordId){

        Optional<DailyRecord> dailyRecordOptional = dailyRecordRepository.findById(dailyRecordId);
        if(!dailyRecordOptional.isPresent()){
            throw  new NotFoundException("일일 기록이 존재하지 않습니다.");
        }

        DailyRecord dailyRecord = dailyRecordOptional.get();

        return DailyRecordDto.ResponseDailyRecordDto.toDto(dailyRecord);
    }

    @Transactional
    public DailyRecordDto.ResponseDailyRecordDto create(Long branchId, DailyRecordDto.RequestDailyRecordDto requestdailyRecordDto){

        Optional<Branch> branchOptional = branchRepository.findById(branchId);
        if (!branchOptional.isPresent()){
            throw new NotFoundException("지점이 존재하지 않습니다.");
        }
        Branch branch = branchOptional.get();

        Optional<Reservation> reservationOptional = reservationRepository.findById(requestdailyRecordDto.getReservationId());
        if (!reservationOptional.isPresent()){
            throw new NotFoundException("예약이 존재하지 않습니다.");
        }
        Reservation reservation = reservationOptional.get();

        DailyRecord dailyRecord = requestdailyRecordDto.toEntity(branch, reservation);
        dailyRecordRepository.save(dailyRecord);

        return DailyRecordDto.ResponseDailyRecordDto.toDto(dailyRecord);
    }

    @Transactional
    public DailyRecordDto.ResponseDailyRecordDto archive(Long dailyRecordId){

        Optional<DailyRecord> dailyRecordOptional = dailyRecordRepository.findById(dailyRecordId);
        if (!dailyRecordOptional.isPresent()){
            throw new NotFoundException("일일 기록이 존재하지 않습니다.");
        }

        DailyRecord dailyRecord = dailyRecordOptional.get();

        dailyRecord.archiveDailyRecord();
        dailyRecordRepository.save(dailyRecord);

        return DailyRecordDto.ResponseDailyRecordDto.toDto(dailyRecord);
    }

    @Transactional
    public DailyRecordDto.ResponseDailyRecordDto update(Long dailyRecordId, DailyRecordDto.RequestDailyRecordDto requestDailyRecordDto){

        Optional<DailyRecord> dailyRecordOptional = dailyRecordRepository.findById(dailyRecordId);
        if (!dailyRecordOptional.isPresent()){
            throw new NotFoundException("일일 기록이 존재하지 않습니다.");
        }
        DailyRecord dailyRecord = dailyRecordOptional.get();

        Optional<Reservation> reservationOptional = reservationRepository.findById(requestDailyRecordDto.getReservationId());
        if (!reservationOptional.isPresent()){
            throw new NotFoundException("예약이 존재하지 않습니다.");
        }
        Reservation reservation = reservationOptional.get();

        dailyRecord.updateDailyRecord(requestDailyRecordDto, reservation);
        dailyRecordRepository.save(dailyRecord);

        return DailyRecordDto.ResponseDailyRecordDto.toDto(dailyRecord);
    }
}
