package com.orderline.user.repository;

import com.orderline.user.model.entity.User;
import com.orderline.user.model.entity.UserSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserSiteRepository extends JpaRepository<UserSite, Long> {
    List<UserSite> findByUserId(Long userId);

    List<UserSite> findByUser(User user);

}
