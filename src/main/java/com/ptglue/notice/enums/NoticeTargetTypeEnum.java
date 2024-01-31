package com.ptglue.notice.enums;

import com.ptglue.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NoticeTargetTypeEnum implements EnumType {

    TUTEE("회원"),
    TUTOR("강사"),
    MANAGER("관리자"),
    STAFF("스탭");

    private final String text;

    @Override
    public String getId() {
        return this.name();
    }
}
