package com.orderline.common.user_qa.repository;

import com.orderline.common.user_qa.model.entity.UserQa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserQaRepository extends JpaRepository<UserQa, Long> {

    Optional<UserQa> findByIdAndUserId(Long id, Long userId);
    Page<UserQa> findByUserId(Long userId, Pageable pageable);

}
