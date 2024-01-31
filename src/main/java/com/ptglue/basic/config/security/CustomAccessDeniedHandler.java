package com.ptglue.basic.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.ptglue.basic.model.dto.ApiResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException{
        log.error("[{}] {}", HttpStatus.FORBIDDEN, e.getMessage());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // 401 UNAUTHORIZED 와 다른점은 401은 인증이 안되었을때, 403은 인증은 되었지만 권한이 없을때
        response.setStatus(HttpStatus.FORBIDDEN.value());

        ApiResponseDto<String> res = ApiResponseDto.createException(e.getMessage());
        try (OutputStream os = response.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(os, res);
            os.flush();
        }
    }
}