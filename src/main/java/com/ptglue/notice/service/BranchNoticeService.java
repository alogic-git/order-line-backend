package com.ptglue.notice.service;

import com.ptglue.basic.exception.NotFoundException;
import com.ptglue.notice.model.dto.BranchNoticeDto;
import com.ptglue.notice.model.entity.BranchNotice;
import com.ptglue.notice.repository.BranchNoticeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class BranchNoticeService {
    @Resource(name = "branchNoticeRepository")
    BranchNoticeRepository branchNoticeRepository;

    public Page<BranchNoticeDto.ResponseBranchNoticeDto> getList(Long branchId, Pageable pageable){

        Page<BranchNotice> branchNoticeList = branchNoticeRepository.findByBranchId(branchId, pageable);

        return branchNoticeList.map(BranchNoticeDto.ResponseBranchNoticeDto::toDto);
    }

    public BranchNoticeDto.ResponseBranchNoticeDto get(Long branchNoticeId){
        Optional<BranchNotice> branchNoticeOptional = branchNoticeRepository.findById(branchNoticeId);

        if (!branchNoticeOptional.isPresent()){
            throw new NotFoundException("존재하지 않는 공지입니다.");
        }

        BranchNotice branchNotice =  branchNoticeOptional.get();

        return BranchNoticeDto.ResponseBranchNoticeDto.toDto(branchNotice);
    }

    @Transactional
    public void updateHits(Long branchNoticeId){
        Optional<BranchNotice> branchNoticeOptional = branchNoticeRepository.findById(branchNoticeId);

        if (!branchNoticeOptional.isPresent()){
            throw new NotFoundException("존재하지 않는 공지입니다.");
        }

        BranchNotice branchNotice =  branchNoticeOptional.get();
        branchNotice.incrementsHits();

        branchNoticeRepository.save(branchNotice);
    }

    @Transactional
    public BranchNoticeDto.ResponseBranchNoticeDto updateLike(Long branchNoticeId){
        Optional<BranchNotice> branchNoticeOptional = branchNoticeRepository.findById(branchNoticeId);

        if (!branchNoticeOptional.isPresent()){
            throw new NotFoundException("존재하지 않는 공지입니다.");
        }

        BranchNotice branchNotice =  branchNoticeOptional.get();
        branchNotice.incrementsLike();

        branchNoticeRepository.save(branchNotice);

        return BranchNoticeDto.ResponseBranchNoticeDto.toDto(branchNotice);
    }
}
