package com.orderline.notice.enums;

import com.orderline.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BranchNoticeTargetTypeEnum implements EnumType {

    ONGOING_TUTEE("진행중 회원"),
    END_TUTEE("종료된 회원"),
    ALL_TUTEE("전체 회원");

    private final String text;

    @Override
    public String getId() {
        return this.name();
    }
}
