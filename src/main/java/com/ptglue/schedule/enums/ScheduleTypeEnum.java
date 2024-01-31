package com.ptglue.schedule.enums;

import com.ptglue.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScheduleTypeEnum implements EnumType {

    NORMAL("일반"),
    OFF("휴무"),
    COUNSEL("상담"),
    ;

    private final String text;

    @Override
    public String getId() {
        return this.name();
    }
}
