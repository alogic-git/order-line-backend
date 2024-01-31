package com.orderline.klass.enums;

import com.orderline.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KlassColorTypeEnum implements EnumType {

    //Klass Color 관리 방법 ENUM?
    POMEGRANATE("석류", "#FF6B6B", "#FFFFFF"),
    PEACH("복숭아", "#FF9EB5", "#FFFFFF"),
    CORAL("산호","#FFB87C", "#000000"),
    BUTTER_CREAM("버터크림","#39CC36", "#000000"),
    OLIVE("올리브","#39CC36", "#FFFFFF"),
    LIME("라임","#85ED00", "#FFFFFF"),
    BLUEBERRY("블루베리","#57A0D3", "#FFFFFF"),
    EMERALD("에메랄드", "#30AABC", "#000000"),
    PLUM("자두", "#9B88DF", "#FFFFFF"),
    PACIFIC("퍼시픽", "#4844FF", "#000000"),
    SUNSET("선셋", "#FF9E0C", "#000000")
    ;

    private final String text;
    private final String mainColor;
    private final String textColor;

    @Override
    public String getId() {
        return this.name();
    }

}
