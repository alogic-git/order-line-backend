package com.ptglue.ticket.enums;

import com.ptglue.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TicketStatusTypeEnum implements EnumType {

    ALL("전체"),
    BEFORE("진행전"),
    ONGOING("진행중"),
    END("종료"),
    REFUND("환불"),
    PAUSE("일시 정지");

    private final String text;

    @Override
    public String getId() {
        return this.name();
    }
}
