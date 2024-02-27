package com.orderline.material.enums;

import com.orderline.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatusEnum implements EnumType {

    AVAILABLE("주문 가능"),
    UNAVAILABLE("주문 불가");

    private final String text;

    @Override
    public String getId() {
        return this.name();
    }

}

