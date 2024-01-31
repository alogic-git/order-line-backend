package com.orderline.klass.enums;

import com.orderline.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KlassLevelTypeEnum implements EnumType {

    HIGH("상급"),
    MIDDLE("중급"),
    LOW("초급"),
    ;

    private final String text;

    @Override
    public String getId() {
        return this.name();
    }

}
