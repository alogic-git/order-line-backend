package com.orderline.schedule.enums;

import com.orderline.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScheduleStatusTypeEnum implements EnumType {

    BEFORE_RECEIPT("접수전"),
    BEING_RECEIPT("접수중"),
    PENDING_RECEIPT("대기 접수"),
    AFTER_RECEIPT("접수 완료"),
    DURING_CLASS("수업 중"),
    ENDED_CLASS("수업 종료"),
    CANCEL_CLASS("수업 취소");

    private final String text;

    @Override
    public String getId() {
        return this.name();
    }
}

