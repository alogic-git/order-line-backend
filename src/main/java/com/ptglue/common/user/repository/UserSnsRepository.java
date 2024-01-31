package com.ptglue.common.user.repository;

import com.ptglue.common.user.enums.SnsTypeEnum;
import com.ptglue.common.user.model.entity.UserSns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSnsRepository extends JpaRepository<UserSns, Long> {
    UserSns findByUser_Id(Long userId);

    Optional<UserSns> findBySnsTypeAndSnsId(SnsTypeEnum snsType, String snsId);
}


