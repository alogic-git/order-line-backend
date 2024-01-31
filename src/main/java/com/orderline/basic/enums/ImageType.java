package com.orderline.basic.enums;

import com.orderline.basic.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageType implements EnumType {
    IMAGE("이미지"),
    GIF("GIF")
    ;

    private final String text;

    @Override
    public String getId() {
        return this.name();
    }


}
