package com.orderline.schedule.enums;

import com.orderline.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RepeatScheduleStatusTypeEnum implements EnumType {

    BEFORE("진행 전"),
    ONGOING("진행 중"),
    END("종료"),
    ;

    private final String text;

    @Override
    public String getId() {
        return this.name();
    }
}
