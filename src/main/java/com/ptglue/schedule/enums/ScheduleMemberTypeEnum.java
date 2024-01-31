package com.ptglue.schedule.enums;

import com.ptglue.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScheduleMemberTypeEnum implements EnumType {

    ALL("전체 일정"),
    FREE("자유 예약"),
    RESERVED("나의 일정"),
    INDIVIDUAL("개인 수업"),
    GROUP("그룹 수업");

    private final String text;

    @Override
    public String getId() { return this.name(); }
}

