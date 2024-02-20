package com.orderline.basic.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    // Jwt Provier 주입
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // Request로 들어오는 Jwt Token의 유효성을 검증(jwtTokenProvider.validateToken)하는 filter를 filterChain에 등록합니다.
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
        	String userPk = jwtTokenProvider.getUserPk(token);

            Authentication auth = jwtTokenProvider.getAuthentication(userPk);
            SecurityContextHolder.getContext().setAuthentication(auth);
            request.setAttribute("userId", Long.valueOf(userPk));
            //request.setAttribute("role", jwtTokenProvider.getUserRole(token));
//            if(jwtTokenProvider.getBranchId(token) != null) {
//                request.setAttribute("branchId", jwtTokenProvider.getBranchId(token).get());
//            } else {
//                request.setAttribute("branchId", jwtTokenProvider.getBranchId(token));
//            }
        }

        filterChain.doFilter(request, response);
    }
}