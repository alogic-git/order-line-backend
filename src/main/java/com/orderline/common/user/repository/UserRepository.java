package com.orderline.common.user.repository;

import com.orderline.common.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Integer countByName(String name);

    Integer countByPhoneAndPhoneActiveYn(String phone, Boolean phoneActiveYn);

    User findFirstByPhoneOrderByModDateTimeDesc(String phone);

    User findByUsernameAndPhone(String username, String phone);

    List<User> findAllByPhone(String phone);

    Optional<User> findByPhoneAndPhoneActiveYn(String phone, Boolean phoneActiveYn);

    List<User> findByIdIn(List<Long> userIdList);

    Optional<User> findByIdAndPhoneActiveYn(Long Id, Boolean phoneActiveYn);
}
