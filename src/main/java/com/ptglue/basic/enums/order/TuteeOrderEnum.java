package com.ptglue.basic.enums.order;

import com.ptglue.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum TuteeOrderEnum implements EnumType {

    TUTEE_NAME_ASC("회원명 가나다순", "u.name", "ASC"),
    TICKET_TOTAL_RESERVATION_CNT_DESC("등록 횟수 많은 순", "totalReservationCount", "DESC"),
    TICKET_TOTAL_RESERVATION_CNT_ASC("등록 횟수 적은 순", "totalReservationCount", "ASC"),
    TICKET_REMAIN_RESERVATION_CNT_DESC("잔여 횟수 많은 순", "remainReservationCount", "DESC"),
    TICKET_REMAIN_RESERVATION_CNT_ASC("잔여 횟수 적은 순", "remainReservationCount", "ASC"),
    TICKET_START_DT_DESC("시작 일자 최근 순", "startDate", "DESC"),
    TICKET_START_DT_ASC("시작 일자 과거 순", "startDate", "ASC"),
    TICKET_END_DT_DESC("남은 일자 많은 순/종료 일자 최근 순", "endDate", "DESC"),
    TICKET_END_DT_ASC("남은 일자 적은 순/종료 일자 과거 순", "endDate", "ASC"),
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
