package com.orderline.basic.config;

import com.orderline.basic.interceptor.PermissionInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Profile(value = {"local", "dev", "prd"})
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	PermissionInterceptor permissionInterceptor;

	public WebMvcConfig(PermissionInterceptor permissionInterceptor) {
		this.permissionInterceptor = permissionInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		log.info("addInterceptor");
		registry.addInterceptor(permissionInterceptor)
				.addPathPatterns("/manager/**")
				.addPathPatterns("/tutor/**")
				.addPathPatterns("/tutee/**")
				.excludePathPatterns("/**/branch/**"); // branch 의 경우 role 만 체크
	}
}
