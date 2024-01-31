package com.ptglue.common.user.enums;

import com.ptglue.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoginTypeEnum implements EnumType {

    GENERAL("GENERAL"),
    SNS("SNS");

    private final String text;

    @Override
    public String getId() {
        return this.name();
    }

}
