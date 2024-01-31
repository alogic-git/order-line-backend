package com.orderline.basic.enums;

import com.orderline.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QaDetailTypeEnum implements EnumType {
    SCHEDULE("SCHEDULE"),
    KLASS("KLASS");

    private final String text;

    @Override
    public String getId() {
        return this.name();
    }
}
