package com.orderline.branch.model.dto;


import com.orderline.branch.model.entity.Branch;
import com.orderline.branch.model.entity.BranchTag;
import com.orderline.product.model.entity.ProductTag;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BranchTagDto {

    @Getter
    public static class RequestBranchTagDto {

        @Size(max = 10, message = "태그명은 10자 이하로 입력해주세요.")
        @ApiModelProperty(value = "태그명", example = "이벤트")
        private String tag;

        @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "태그 색상은 16진수로 입력해주세요.")
        @ApiModelProperty(value = "태그 색상", example = "#FFD3E6")
        private String tagColor;

        public BranchTag toEntity(final Branch branch) {
            return BranchTag.builder()
                    .branch(branch)
                    .tag(tag)
                    .tagColor(tagColor)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ResponseBranchTagDto {

        @ApiModelProperty(value = "지점 태그 ID")
        private Long branchTagId;

        @ApiModelProperty(value = "태그명")
        private String tag;

        @ApiModelProperty(value = "태그 색상")
        private String tagColor;

        public static ResponseBranchTagDto toDto(final ProductTag productTag) {
            BranchTag branchTag = productTag.getBranchTag();
            return ResponseBranchTagDto.builder()
                    .branchTagId(branchTag.getId())
                    .tag(branchTag.getTag())
                    .tagColor(branchTag.getTagColor())
                    .build();
        }

        public static ResponseBranchTagDto toDto(final BranchTag branchTag) {
            return ResponseBranchTagDto.builder()
                    .branchTagId(branchTag.getId())
                    .tag(branchTag.getTag())
                    .tagColor(branchTag.getTagColor())
                    .build();
        }
    }
}
