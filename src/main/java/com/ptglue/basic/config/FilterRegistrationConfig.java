package com.ptglue.basic.config;

import com.ptglue.basic.filter.LoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterRegistrationConfig {

    private final LoggingFilter loggingFilter;

    public FilterRegistrationConfig(LoggingFilter loggingFilter) {
        this.loggingFilter = loggingFilter;
    }

    @Bean
    public FilterRegistrationBean<LoggingFilter> loggingFilterRegistration() {
        FilterRegistrationBean<LoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(loggingFilter);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
