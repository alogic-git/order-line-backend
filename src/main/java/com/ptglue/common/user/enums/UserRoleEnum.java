package com.ptglue.common.user.enums;


import com.ptglue.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRoleEnum implements EnumType {

    ADMIN("ROLE_ADMIN"),
    TUTOR("ROLE_TUTOR"),
    TUTEE("ROLE_TUTEE"),
    MANAGER("ROLE_MANAGER"),
    CENTER("ROLE_CENTER"),
    GENERAL("ROLE_GENERAL")
    ;

    private final String text;

    @Override
    public String getId() {
        return this.name();
    }

}
