package com.orderline.klass.enums;

import com.orderline.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KlassStartTimeTypeEnum implements EnumType {

    A0("매시각 정시", 60, 60),
    A30("매시각 30분", 60, 30),
    E10("10분 마다", 10, 10),
    E15("15분 마다", 15, 15),
    E20("20분 마다", 20, 20),
    E30("30분 마다", 30, 30),
    ;

    private final String text;
    private final int additionalMinutes;
    private final int atMinutes;

    @Override
    public String getId() {
        return this.name();
    }

}
