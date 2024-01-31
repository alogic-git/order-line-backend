package com.orderline.common.user.enums;


import com.orderline.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SnsTypeEnum implements EnumType {

    NAVER("NAVER"),
    KAKAO("KAKAO"),
    APPLE("APPLE");

    private final String text;

    @Override
    public String getId() {
        return this.name();
    }
}
