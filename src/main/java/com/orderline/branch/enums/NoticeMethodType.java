package com.orderline.branch.enums;

import com.orderline.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum NoticeMethodType implements EnumType {

    SMS("SMS"),
    PUSH("PUSH"),
    EMAIL("EMAIL"),
    KAKAO("KAKAO"),
    ;

    private final String text;

    @Override
    public String getId() {
        return name();
    }
}
