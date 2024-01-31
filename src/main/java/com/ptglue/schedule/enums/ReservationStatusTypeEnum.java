package com.ptglue.schedule.enums;

import com.ptglue.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationStatusTypeEnum implements EnumType {

    WAIT("예약 대기"),
    CONFIRMATION("예약 확정"),
    CANCELLATION("예약 취소"),
    ATTENDANCE("출석"),
    ABSENCE("결석");


    private final String text;

    @Override
    public String getId() {
        return this.name();
    }
}

