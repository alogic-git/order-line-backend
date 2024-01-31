package com.orderline.notice.enums;

import com.orderline.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NoticeTypeEnum implements EnumType {

    EMERGENCY("긴급"),
    GENERAL("일반");

    private final String text;

    @Override
    public String getId() {
        return this.name();
    }
}
