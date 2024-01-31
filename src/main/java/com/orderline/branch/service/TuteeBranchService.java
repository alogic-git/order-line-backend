package com.orderline.branch.service;

import com.orderline.basic.exception.NotFoundException;
import com.orderline.branch.model.dto.TuteeBranchDto;
import com.orderline.branch.model.entity.BranchUserRole;
import com.orderline.branch.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TuteeBranchService {
    @Resource(name = "branchUserRoleRepository")
    BranchUserRoleRepository branchUserRoleRepository;

    public TuteeBranchDto.ResponseTuteeBranchDto get(Long userId, Long branchId){

        BranchUserRole branchUserRole = branchUserRoleRepository.findByUserIdAndBranchId(userId, branchId)
                .orElseThrow(() -> new NotFoundException("지점이 존재하지 않습니다."));

        return TuteeBranchDto.ResponseTuteeBranchDto.toDto(branchUserRole);
    }

    public Page<TuteeBranchDto.ResponseTuteeBranchDto> getList(Long userId, Pageable pageable) {
        Page<BranchUserRole> branchUserRole = branchUserRoleRepository.findAllByUserId(userId, pageable);

        return branchUserRole.map(TuteeBranchDto.ResponseTuteeBranchDto::toDto);
    }
}
