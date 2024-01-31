package com.ptglue.schedule.service;

import com.ptglue.basic.exception.NotFoundException;
import com.ptglue.branch.model.entity.Branch;
import com.ptglue.branch.repository.BranchRepository;
import com.ptglue.klass.repository.KlassRepository;
import com.ptglue.schedule.model.dto.RepeatScheduleDto;
import com.ptglue.schedule.model.entity.RepeatSchedule;
import com.ptglue.klass.model.entity.Klass;
;
import com.ptglue.schedule.repository.RepeatScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class RepeatScheduleService {
    @Resource(name = "repeatScheduleRepository")
    private RepeatScheduleRepository repeatScheduleRepository;

    @Resource(name = "klassRepository")
    private KlassRepository klassRepository;

    @Resource(name = "branchRepository")
    private BranchRepository branchRepository;

    public RepeatScheduleDto.ResponseRepeatScheduleDto get(Long repeatScheduleId){

        Optional<RepeatSchedule> repeatScheduleOptional = repeatScheduleRepository.findById(repeatScheduleId);
        if(!repeatScheduleOptional.isPresent()){
            throw  new NotFoundException("반복 일정이 존재하지 않습니다.");
        }

        RepeatSchedule repeatSchedule = repeatScheduleOptional.get();
        return RepeatScheduleDto.ResponseRepeatScheduleDto.toDto(repeatSchedule);
    }

    @Transactional
    public RepeatScheduleDto.ResponseRepeatScheduleDto create(Long branchId, RepeatScheduleDto.RequestRepeatScheduleDto requestrepeatScheduleDto){
        Optional<Branch> branchOptional = branchRepository.findById(branchId);
        if (!branchOptional.isPresent()){
            throw new NotFoundException("지점이 존재하지 않습니다.");
        }
        Branch branch = branchOptional.get();

        Optional<Klass> klassOptional = klassRepository.findById(requestrepeatScheduleDto.getKlassId());
        if (!klassOptional.isPresent()){
            throw new NotFoundException("클래스가 존재하지 않습니다.");
        }
        Klass klass = klassOptional.get();

        RepeatSchedule repeatSchedule = requestrepeatScheduleDto.toEntity(branch, klass);
        repeatScheduleRepository.save(repeatSchedule);

        return RepeatScheduleDto.ResponseRepeatScheduleDto.toDto(repeatSchedule);
    }

    @Transactional
    public RepeatScheduleDto.ResponseRepeatScheduleDto archive(Long repeatScheduleId){

        Optional<RepeatSchedule> repeatScheduleOptional = repeatScheduleRepository.findById(repeatScheduleId);
        if (repeatScheduleOptional.isPresent()){
            throw new NotFoundException("반복일정이 없습니다.");
        }

        RepeatSchedule repeatSchedule = repeatScheduleOptional.get();

        repeatSchedule.archiveRepeatSchedule();
        repeatScheduleRepository.save(repeatSchedule);

        return RepeatScheduleDto.ResponseRepeatScheduleDto.toDto(repeatSchedule);
    }
}
