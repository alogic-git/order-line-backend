package com.ptglue.basic.enums;

import com.ptglue.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QaTypeEnum implements EnumType {
    FUNCTION("FUNCTION"),
    ERROR("ERROR" );

    private final String text;

    @Override
    public String getId() {
        return this.name();
    }

}
