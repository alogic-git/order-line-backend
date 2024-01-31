package com.ptglue.branch.enums;

import com.ptglue.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StateTypeEnum implements EnumType {

    ONGOING("진행 중"),
    END("종료"),
    ALL("전체"),
    ;

    private final String text;

    @Override
    public String getId() {
        return this.name();
    }
}
