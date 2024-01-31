package com.orderline.payment.enums;

import com.orderline.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentHistoryPaidMethodTypeEnum implements EnumType {

    NONE("결제 수단 미입력"),
    CASH("현금"),
    CARD("카드"),
    TRNASFER("계좌 이체"),
    ZEROPAY("제로 페이"),
    NAVERPAY("네이버 페이"),
    KAKAOPAY("카카오 페이"),
    APPLEPAY("애플 페이"),
    CASH_CARD("현금 + 카드"),
    CARD_TRANSFER("카드 + 계좌 이체"),
    CASH_TRANSFER("현금 + 계좌 이체"),
    ZEROPAY_CASH("제로 페이 + 현금"),
    ZEROPAY_CARD("제로 페이 + 카드"),
    ZEROPAY_TRANSFER("제로 페이 + 계좌 이체");

    private final String text;

    @Override
    public String getId() { return this.name(); }
}
