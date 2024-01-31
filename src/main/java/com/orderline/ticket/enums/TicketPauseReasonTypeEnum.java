package com.orderline.ticket.enums;

import com.orderline.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TicketPauseReasonTypeEnum implements EnumType {


    TUTEE_REQUEST("회원 요청"),
    BRANCH_REQUEST("지점 사정");

    private final String text;

    @Override
    public String getId() {
        return this.name();
    }
}
