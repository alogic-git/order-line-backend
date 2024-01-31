package com.orderline.branch.enums;

import com.orderline.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConnectionTypeEnum implements EnumType {

    CONNECTED("연결 중", "", "", "",""),
    WAIT("연결 대기", "", "", "",""),
    DISCONNECTED("연결 해제", "", "", "","");//연결해제 색깔 확인 필요

    private final String text;
    private final String attr1;
    private final String attr2;
    private final String attr3;
    private final String attr4;

    @Override
    public String getId() {
        return this.name();
    }

}
