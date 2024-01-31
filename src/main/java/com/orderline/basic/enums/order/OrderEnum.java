package com.orderline.basic.enums.order;

import com.orderline.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

//사용안함. 분리함
@Getter
@RequiredArgsConstructor
public enum OrderEnum implements EnumType {

    //type 별로 따로 정렬 enum 생성할지 확인.
    TUTOR_NAME_ASC("강사명 오름차순", "u.name", "ASC"),
    TUTOR_NAME_DESC("강사명 내림차순", "u.name", "DESC"),
    TUTOR_NICKNAME_ASC("강사별칭 오름차순", "nickname", "ASC"),
    TUTOR_NICKNAME_DESC("강사별칭 내림차순", "nickname", "DESC"),
    REG_DT_ASC("등록일 오름차순", "reg_dt", "ASC"),
    REG_DT_DESC("등록일 내림차순", "reg_dt", "DESC"),
    KLASS_NAME_ASC("클래스명 오름차순", "klass_name", "ASC"),
    KLASS_NAME_DESC("클래스명 내림차순", "klass_name", "DESC"),
    PRODUCT_NAME_ASC("수강권 오름차순", "product_name", "ASC"),
    PRODUCT_NAME_DESC("수강권 내림차순", "product_name", "DESC"),
    ;

    private final String text;
    private final String column;
    private final String direction;

    @Override
    public String getId() {
        return this.name();
    }

    public Sort getSort() {
        if (this.getDirection().equals("ASC")) {
            return Sort.by(Sort.Direction.ASC, this.getColumn());
        } else {
            return Sort.by(Sort.Direction.DESC, this.getColumn());
        }
    }

}
