package com.ptglue.branch.enums;

import com.ptglue.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScheduleAutoFinishTypeEnum implements EnumType {

    OFF("설정 안함"),
    ATTENDANCE("출석"),
    ABSENCE("결석"),
    ;

    private final String text;

    @Override
    public String getId() {
        return name();
    }
}
