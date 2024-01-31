package com.ptglue.notice.enums;

import com.ptglue.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationStatusTypeEnum implements EnumType {

    SUCCESS("성공"),
    FAIL("실패"),
    RESERVATION_SENT("예약 발송"),
    ALL("전부")
    ;

    private final String text;

    @Override
    public String getId() {
        return this.name();
    }
}
