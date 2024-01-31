package com.ptglue.klass.enums;

import com.ptglue.basic.utils.EnumType;
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
