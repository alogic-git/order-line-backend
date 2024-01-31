package com.orderline.basic.enums.order;

import com.orderline.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum KlassOrderEnum implements EnumType {

    REG_DT_ASC("등록일 오름차순", "reg_dt", "ASC"),
    REG_DT_DESC("등록일 내림차순", "reg_dt", "DESC"),
    KLASS_NAME_ASC("클래스명 오름차순", "klass_name", "ASC"),
    KLASS_NAME_DESC("클래스명 내림차순", "klass_name", "DESC"),
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
