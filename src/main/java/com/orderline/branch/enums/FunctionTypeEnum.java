package com.orderline.branch.enums;

import com.orderline.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FunctionTypeEnum implements EnumType {

    SCHEDULE("일정"),
    RESERVATION("예약"),
    TUTEE("회원"),
    KLASS("수업"),
    PRODUCT("수강권"),
    TICKET("회원 수강권"), //enum 추가 할지 확인 하기 (회원에서 ticket 연장 시 수정 권한 필요 ?)
    TUTOR("강사"),
    MANAGER("관리자"),
    EXTRAPRODUCT("부가상품"),
    BRANCHNOTICE("지점알림"),
    NOTICE("공지사항"),
    STATISTICS("통계"),
    SETTING("설정"),
    ;

    private final String text;

    @Override
    public String getId() {
        return this.name();
    }

}
