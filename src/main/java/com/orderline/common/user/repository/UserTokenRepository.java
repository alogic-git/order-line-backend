package com.orderline.common.user.repository;

import com.orderline.common.user.model.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken,Long> {

    UserToken findFirstByRefreshTokenOrderByIdDesc(String refreshToken);

    List<UserToken> findByUser_Id(Long userId);

}
