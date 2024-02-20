package com.orderline.order.enums;

import com.orderline.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatusEnum implements EnumType {

    GENERAL("진행중"),
    SNS("진행완료");

    private final String text;

    @Override
    public String getId() {
        return this.name();
    }

}
