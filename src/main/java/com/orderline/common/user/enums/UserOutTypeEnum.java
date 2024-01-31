package com.orderline.common.user.enums;

import com.orderline.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserOutTypeEnum implements EnumType {

    ACTIVE("이용중"),
    INACTIVE("휴면");

    private final String text;

    @Override
    public String getId() {
        return this.name();
    }
}
