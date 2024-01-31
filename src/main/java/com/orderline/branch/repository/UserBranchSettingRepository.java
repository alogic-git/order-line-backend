package com.orderline.branch.repository;

import com.orderline.branch.model.entity.UserBranchSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserBranchSettingRepository extends JpaRepository<UserBranchSetting, Long> {
    Optional<UserBranchSetting> findByUserIdAndBranchId(Long userId, Long branchId);
}
