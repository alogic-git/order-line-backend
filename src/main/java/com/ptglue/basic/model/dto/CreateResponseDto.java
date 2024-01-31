package com.ptglue.basic.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateResponseDto {
    private Long id;

    @Getter
    @Builder
    public static class S3UploadResponseDto {
        private String imageUri;
    }
}
