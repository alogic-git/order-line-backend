package com.orderline.basic.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

//토큰으로부터 유저 정보를 받아 mod_user_id, reg_user_id를 auditing하기 위한 클래스
public class AuditorAwareImpl implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        String authenticatedId = authentication.getName();
        long userId;
        try{
            userId = Long.parseLong(authenticatedId);
        }
        catch (NumberFormatException e){
            return Optional.empty();
        }
        return Optional.of(userId);
    }
}
