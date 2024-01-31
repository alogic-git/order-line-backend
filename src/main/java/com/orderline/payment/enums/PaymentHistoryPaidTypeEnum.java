package com.orderline.payment.enums;


import com.orderline.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum PaymentHistoryPaidTypeEnum implements EnumType {


//    1원이라도 납부 시작했을때 ~ 잔여금액 0원 초과일떄
    PAYMENT("납부중"),
//    납부 금액이 0원일때
    UNPAID("미납"),
//    잔여 금액이 0원일때
    PAID("완납"),
    REFUND("환불");

    private final String text;

    @Override
    public String getId() { return this.name(); };
}
