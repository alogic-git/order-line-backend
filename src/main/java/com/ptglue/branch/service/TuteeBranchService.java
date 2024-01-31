package com.ptglue.branch.service;

import com.ptglue.basic.exception.NotFoundException;
import com.ptglue.branch.model.dto.TuteeBranchDto;
import com.ptglue.branch.model.dto.UserBranchSettingDto;
import com.ptglue.branch.model.entity.BranchUserRole;
import com.ptglue.branch.model.entity.UserBranchSetting;
import com.ptglue.branch.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

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
