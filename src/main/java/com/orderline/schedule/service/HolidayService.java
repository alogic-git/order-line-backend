package com.orderline.schedule.service;

import com.orderline.basic.exception.NotFoundException;
import com.orderline.schedule.model.dto.HolidayDto;
import com.orderline.schedule.model.entity.Holiday;
import com.orderline.schedule.repository.HolidayRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HolidayService {
    @Resource(name = "holidayRepository")
    private HolidayRepository holidayRepository;

    public HolidayDto.ResponseHolidayDto get(Long holidayId){

        Optional<Holiday> holidayOptional = holidayRepository.findById(holidayId);
        if(!holidayOptional.isPresent()){
            throw  new NotFoundException("휴일이 존재하지 않습니다.");
        }

        Holiday holiday = holidayOptional.get();
        return HolidayDto.ResponseHolidayDto.toDto(holiday);
    }

    public List<HolidayDto.ResponseHolidayDto> getList(LocalDate startDate, LocalDate endDate){
        ZonedDateTime startDt = startDate.atTime(0,0,0).atZone(ZoneId.systemDefault());
        ZonedDateTime endDt = endDate.plusDays(1).atTime(0,0,0).atZone(ZoneId.systemDefault());

        List<Holiday> holidays = holidayRepository.findByHolidayDateGreaterThanEqualAndHolidayDateLessThanEqual(startDt, endDt);

        return holidays.stream().map(HolidayDto.ResponseHolidayDto::toDto).collect(Collectors.toList());
    }

    @Transactional
    public HolidayDto.ResponseHolidayDto create(HolidayDto.RequestHolidayDto requestholidayDto){

        Holiday holiday = requestholidayDto.toEntity();
        holidayRepository.save(holiday);

        return HolidayDto.ResponseHolidayDto.toDto(holiday);
    }

    @Transactional
    public HolidayDto.ResponseHolidayDto update(Long holidayId, HolidayDto.RequestHolidayDto requestHolidayDto){

        Optional<Holiday> holidayOptional = holidayRepository.findById(holidayId);
        if (!holidayOptional.isPresent()){
            throw new NotFoundException("휴일이 존재하지 않습니다.");
        }
        Holiday holiday = holidayOptional.get();

        holiday.updateHoliday(requestHolidayDto);
        holidayRepository.save(holiday);

        return HolidayDto.ResponseHolidayDto.toDto(holiday);
    }
}
