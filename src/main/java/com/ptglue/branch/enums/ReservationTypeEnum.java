package com.ptglue.branch.enums;

import com.ptglue.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationTypeEnum implements EnumType {

    FREE_CHOICE("자유 예약형"),
    LIMITED_CHOICE("사전 개설형");

    private final String text;

    @Override
    public String getId() {
        return this.name();
    }
}
