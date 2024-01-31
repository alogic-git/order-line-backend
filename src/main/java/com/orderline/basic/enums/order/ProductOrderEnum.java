package com.orderline.basic.enums.order;

import com.orderline.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum ProductOrderEnum implements EnumType {
    REG_DT_ASC("등록일 오름차순", "reg_dt", "ASC"),
    REG_DT_DESC("등록일 내림차순", "reg_dt", "DESC"),
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
