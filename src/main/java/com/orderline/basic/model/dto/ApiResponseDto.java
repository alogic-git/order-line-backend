package com.orderline.basic.model.dto;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Data
public class ApiResponseDto<T> {
    private String systemTitle;
    private String systemMessage;
    private String systemInformation;
    private T data;

    private ApiResponseDto(String systemTitle, T data) {
        this.systemTitle = systemTitle;
        this.data = data;
    }

    public static ApiResponseDto<String> createException(String systemTitle) {
        return new ApiResponseDto<>(systemTitle, "");
    }

    public static <T> ApiResponseDto<T> createException(String systemTitle, T data) {
        return new ApiResponseDto<>(systemTitle, data);
    }

    // create 성공시 uri 를 parameter 로 받음
    public static <T> ResponseEntity<T> createdResponseEntity(String uri, T body) {
        return ResponseEntity.created(URI.create(uri)).body(body);
    }

    // create 성공시 id 를 parameter 로 받음
    public static <T> ResponseEntity<T> createdResponseEntity(Long id, T body) {
        return ResponseEntity.created(URI.create(ServletUriComponentsBuilder.fromCurrentRequest().toUriString() + "/" + id)).body(body);
    }
}
