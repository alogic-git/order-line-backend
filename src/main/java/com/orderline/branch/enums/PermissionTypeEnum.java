package com.orderline.branch.enums;

import com.orderline.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PermissionTypeEnum implements EnumType {

    NONE("권한없음"),
    VIEW("조회"),
    EDIT("편집");

    private final String text;

    @Override
    public String getId() {
        return this.name();
    }

}
