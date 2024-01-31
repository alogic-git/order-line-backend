package com.orderline.notice.enums;

import com.orderline.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationTypeEnum implements EnumType {

    SMS("문자 메시지"),
    PUSH("푸시 메시지"),
    NONE("없음");

    private final String text;

    @Override
    public String getId() {
        return this.name();
    }
}
