package com.orderline.product.enums;

import com.orderline.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductSettingTypeEnum implements EnumType {

    //설정 추가되면 enum 추가하기
    AVAILABLE_DAILY_RESERVATION_COUNT("일일 예약 가능 횟수", "999999"),
    AVAILABLE_DAILY_CANCELLATION_COUNT("일일 취소 가능 횟수", "999999"),
    AVAILABLE_DAILY_RESERVATION_DUPLICATE_COUNT(" 일일 중복 예약 가능 횟수", "999999"),
    AVAILABLE_WEEKLY_RESERVATION_COUNT("주간 예약 가능 횟수", "999999"),
    AVAILABLE_WEEKLY_CANCELLATION_COUNT("주간 취소 가능 횟수", "999999"),
    ;

    private final String text;

    private final String defaultValue;

    @Override
    public String getId() {
        return this.name();
    }

}
