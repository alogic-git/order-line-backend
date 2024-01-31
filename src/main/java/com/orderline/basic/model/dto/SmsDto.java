package com.orderline.basic.model.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SmsDto {

    @ToString
    @Getter
    @Builder
    public static class MessagesDto {
        @JsonProperty("to")
        private String to;

        @JsonProperty("content")
        private String content;

    }

    @ToString
    @Getter
    @Builder
    public static class SmsRequestDto {
        @JsonProperty("type")
        private String type;

        @JsonProperty("contentType")
        private String contentType;

        @JsonProperty("countryCode")
        private String countryCode;

        @JsonProperty("from")
        private String from;

        @JsonProperty("content")
        private String content;

        @JsonProperty("messages")
        private List<MessagesDto> messages;

    }

    @ToString
    @Getter
    @Builder
    public static class SmsResponseDto {
        @JsonProperty("requestId")
        private String requestId;

        @JsonProperty("requestTime")
        private String requestTime;

        @JsonProperty("statusCode")
        private int statusCode;

        @JsonProperty("statusName")
        private String statusName;

    }

}
