package com.orderline.basic.model.dto;

import com.orderline.basic.enums.ImageType;
import lombok.*;

public class CommonDto {

    @Data
    @Builder
    public static class ImageInfo {
        private String fileName;
        private ImageType imageType;
    }

    @Data
    @Builder
    public static class VideoInfo{
        private String fileName;
    }

    @Data
    @Builder
    public static class EmailInfo{
        private String to;
        private String subject;
        private String contents;
    }

    @Getter
    @Builder
    public static class EnumStateWithAttr{
        private String stateCode;
        private String stateName;
        private String attr1;
        private String attr2;
        private String attr3;
        private String attr4;
    }


}
