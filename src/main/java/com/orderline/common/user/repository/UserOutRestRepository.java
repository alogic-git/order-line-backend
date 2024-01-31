package com.orderline.common.user.repository;

import com.orderline.common.user.model.entity.UserOutRest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserOutRestRepository extends JpaRepository<UserOutRest, Long> {

    Optional<UserOutRest> findByUserId(Long userId);

    UserOutRest findFirstByPhoneOrderByModDateTimeDesc(String phone);

    UserOutRest findByUsernameAndPhone(String username, String phone);

    List<UserOutRest> findAllByPhone(String phone);
}
