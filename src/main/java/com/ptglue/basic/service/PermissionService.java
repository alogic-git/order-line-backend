package com.ptglue.basic.service;

import com.ptglue.basic.model.dto.UserContext;
import com.ptglue.branch.repository.BranchUserPermissionRepository;
import com.ptglue.branch.repository.BranchUserRoleRepository;
import com.ptglue.branch.model.dto.BranchUserDto;
import com.ptglue.branch.model.entity.BranchUserPermission;
import com.ptglue.branch.model.entity.BranchUserRole;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    private final BranchUserRoleRepository branchUserRoleRepository;
    private final BranchUserPermissionRepository branchUserPermissionRepository;

    public PermissionService(BranchUserRoleRepository branchUserRoleRepository, BranchUserPermissionRepository branchUserPermissionRepository) {
        this.branchUserRoleRepository = branchUserRoleRepository;
        this.branchUserPermissionRepository = branchUserPermissionRepository;
    }

    // userContext 정보(userId, branchId, role) 을 이용하여 사용자의 권한을 조회
    public List<BranchUserDto.ResponseBranchUserPermissionDto> getUserPermissions(UserContext userContext) {

        // 해당 지점의 role 조회
        Optional<BranchUserRole> branchUserRole = branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(userContext.getBranchId(), userContext.getUserId(), userContext.getRole());
        if(!branchUserRole.isPresent()) {
            throw new AuthorizationServiceException("해당 지점의 권한이 없습니다.");
        }
        // 사용자의 역할(Role)을 조회
        List<BranchUserPermission> branchUserPermissions = branchUserPermissionRepository.findByBranchUserRoleId(branchUserRole.get().getId());

        return branchUserPermissions.stream().map(BranchUserDto.ResponseBranchUserPermissionDto::toDto).collect(Collectors.toList());
    }
}
