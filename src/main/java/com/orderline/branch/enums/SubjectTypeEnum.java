package com.orderline.branch.enums;

import com.orderline.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubjectTypeEnum implements EnumType {

    SPORTS("스포츠"),
    MUSIC("음악"),
    PRIVATE_LESSON ("과외"),
    ETC("기타");

    private final String text;

    @Override
    public String getId() {
        return this.name();
    }

}
