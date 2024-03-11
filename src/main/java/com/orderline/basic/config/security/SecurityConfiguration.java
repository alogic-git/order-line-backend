package com.orderline.basic.config.security;

import com.orderline.user.enums.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Profile(value = {"local", "dev", "prd"})
@Configuration
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@EnableWebSecurity
public class SecurityConfiguration {

  private final JwtTokenProvider jwtTokenProvider;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
    http
        .cors().configurationSource(corsConfigurationSource()) // cors 관련 설정
        .and()
        .httpBasic().disable() // rest api 이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트 된다.
        .csrf().disable() // rest api 이므로 csrf 보안이 필요없으므로 disable 처리.
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt token 으로 인증하므로 세션은 필요없으므로 생성안함.
        .and()
        .authorizeRequests() // 다음 리퀘스트에 대한 사용권한 체크
        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll() // preflight 요청은 모두 허용
        .antMatchers("/", "/v2/api-docs/**", "/swagger*/**", "/user/**", "/auth/token/reissue", "/common/**", "/basic/**").permitAll()
        //.antMatchers("/user/**").hasRole(UserRoleEnum.USER.getId())
        .antMatchers("/admin/**").hasRole(UserRoleEnum.ADMIN.getId())
        .antMatchers(HttpMethod.OPTIONS, "/**", "/**/*").permitAll()
        .and()
        .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
        .and()
        .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        .and()
        .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); // before filter 추가
    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring().antMatchers("/swagger*/**");
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedOriginPattern("*");
//    configuration.addAllowedOriginPattern("https://localhost");
//    configuration.addAllowedOriginPattern("http://localhost:*");
//    configuration.addAllowedOriginPattern("http://*.orderline.com");
//    configuration.addAllowedOriginPattern("https://*.orderline.com");
//    configuration.addAllowedOriginPattern("https://*.naver.com");
    configuration.addAllowedHeader("*");
    configuration.addAllowedMethod("*");
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public HttpFirewall defaultHttpFirewall() {
    return new DefaultHttpFirewall();
  }
}
