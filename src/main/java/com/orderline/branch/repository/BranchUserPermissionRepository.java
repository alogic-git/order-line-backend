package com.orderline.branch.repository;

import com.orderline.branch.model.entity.BranchUserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchUserPermissionRepository extends JpaRepository<BranchUserPermission, Long> {

    List<BranchUserPermission> findByBranchUserRoleId(Long branchUserRoleId);

    List<BranchUserPermission> findByBranchUserRole_UserIdAndBranchUserRole_BranchId(Long userId, Long branchId);
}

