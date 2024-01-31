package com.orderline.basic.enums.order;

import com.orderline.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum TicketPauseHistoryOrderEnum implements EnumType {

    START_DT_DESC("시간순 정렬", "startDate", "DESC"),
    TICKET_NAME_ASC("티켓명 가나다순", "t.ticket_name", "ASC"), //체크하기
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
