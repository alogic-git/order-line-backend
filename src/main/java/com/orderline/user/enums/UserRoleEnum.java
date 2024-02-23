package com.orderline.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.orderline.basic.utils.EnumType;

@Getter
@RequiredArgsConstructor
public enum UserRoleEnum implements EnumType {

    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER")
    ;

    private final String text;

    @Override
    public String getId() {
        return this.name();
    }

}
