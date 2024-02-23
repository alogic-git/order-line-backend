package com.orderline.user.repository;

import com.orderline.user.model.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    Optional<UserToken> findFirstByRefreshTokenOrderByIdDesc(String refreshToken);

    List<UserToken> findByUserId(Long userId);

}
