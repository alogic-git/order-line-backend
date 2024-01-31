package com.ptglue.extra.enums;

import com.ptglue.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExtraStatusTypeEnum implements EnumType {

    PURCHASE_COMPLETED("구매 완료"),
    PURCHASE_REQUEST("구매 요청");

    private final String text;

    @Override
    public String getId() {
        return this.name();
    }
}
