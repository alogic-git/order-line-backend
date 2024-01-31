package com.orderline.basic.enums;

import com.orderline.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiResponseCode implements EnumType {
    OK("(200) 요청이 성공하였습니다."),
    BAD_REQUEST("(400) 잘못된 요청입니다."),
    BAD_PARAMETER("(400) 요청 파라미터가 잘못되었습니다."),
    UNAUTHORIZED("(401) 인증에 실패하였습니다."),
    ACCESS_DENIED("(403) 접근 권한이 없습니다."),
    NOT_FOUND("(404) 리소스를 찾지 못했습니다."),
    TOAST_ERROR("(420) 토스트 에러입니다."),
    SERVER_ERROR("(500) 서버 에러입니다.");

    private final String systemTitle ;

    public String getId() {
        return name();
    }

    @Override
    public String getText() {
        return systemTitle;
    }
}

