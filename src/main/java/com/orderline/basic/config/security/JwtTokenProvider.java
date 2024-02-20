package com.orderline.basic.config.security;

import io.jsonwebtoken.*;
import com.orderline.basic.config.Env;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

@DependsOn("Env")
@Component
@RequiredArgsConstructor
public class JwtTokenProvider { // JWT 토큰을 생성 및 검증 모듈

    private String secretKey = Env.jwtSecret();
//    private String secretKey = "aaaaaaaaaa";

    private static final long ACCESS_TOKEN_VALID_MILLISECOND = 1000L * 60 * 60 * 24 * 90; // accessToken 3개월 유효
    private static final long REFRESH_TOKEN_VALID_MILLISECOND = 1000L * 60 * 60 * 24 * 180; // refreshToken 6개월 유효


    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
    	secretKey = Base64Utils.encodeToString(secretKey.getBytes());
    }

    // Jwt 토큰 생성
//    public String createAccessToken(String userPk, UserDto.RoleDto userRoleDto) {
//        Claims claims = Jwts.claims().setSubject(userPk);
//        claims.put("role", userRoleDto.getRoleType());
//        if(userRoleDto.getBranchId() > 0L) {
//            claims.put("branchId", userRoleDto.getBranchId());
//        }
//        Date now = new Date();
//        return Jwts.builder()
//                .setClaims(claims) // 데이터
//                .setIssuedAt(now) // 토큰 발행일자
//                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_MILLISECOND)) // set Expire Time
//                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, secret값 세팅
//                .compact();
//    }
    public String createRefreshToken() {
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() +REFRESH_TOKEN_VALID_MILLISECOND))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Jwt 토큰으로 인증 정보를 조회
    public Authentication getAuthentication(String userPk) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userPk);
        if (userDetails == null) {
        	return null;
        }
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Jwt 토큰에서 회원 구별 정보 추출
    public String getUserPk(String token) {
        String userPk;
        try {
            userPk = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        }
        catch(ExpiredJwtException e) {
            userPk = e.getClaims().getSubject();
            return userPk;
        }
        return userPk;
    }



    public Long getExpiresIn(String token) {
        Date now = new Date();
        Date expiresDate;
        try {
            expiresDate = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration();
        }
        catch(ExpiredJwtException e) {
            expiresDate = e.getClaims().getExpiration();
            return (expiresDate.getTime() - now.getTime()) / 1000;
        }
        return (expiresDate.getTime() - now.getTime()) / 1000;
    }
    // Request의 Header에서 token 파싱 : "X-AUTH-TOKEN: jwt토큰"
    public String resolveToken(HttpServletRequest req) {
        return req.getHeader("X-AUTH-TOKEN");
    }

    // Jwt 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}