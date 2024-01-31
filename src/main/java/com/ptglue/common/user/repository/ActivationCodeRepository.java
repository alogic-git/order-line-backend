package com.ptglue.common.user.repository;

import com.ptglue.common.user.model.entity.ActivationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivationCodeRepository extends JpaRepository<ActivationCode, Long> {

    ActivationCode findFirstByPhoneOrderByModDateTimeDesc(String phone);

    ActivationCode findFirstByPhoneOrderByRegDateTimeDesc(String phone);

    Optional<ActivationCode> findByPhoneAndActivationYn(String phone, Boolean activationYn);
}
