package com.orderline.setting.model.dto;

import com.orderline.branch.enums.FunctionTypeEnum;
import com.orderline.setting.model.entity.ArchiveList;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArchiveDto {

    @Getter
    @Builder
    public static class ResponseArchiveDto{

        @ApiModelProperty(value = "분야")
        private FunctionTypeEnum category;

        @ApiModelProperty(value = "분야 아이디")
        private Long categoryId;

        @ApiModelProperty(value = "이름")
        private String name;

        @ApiModelProperty(value = "상세정보")
        private List<String> detail;

        public static ResponseArchiveDto toDto(ArchiveList archiveList){
            return ResponseArchiveDto.builder()
                    .category(archiveList.getCategory())
                    .categoryId(archiveList.getCategoryId())
                    .name(archiveList.getName())
                    .detail(Arrays.asList(archiveList.getInfo1(), archiveList.getInfo2(), archiveList.getInfo3(), archiveList.getInfo4()))
                    .build();
        }

    }

    @Getter
    @Builder
    public static class ResponseArchiveListDto {

        @ApiModelProperty(value = "보관함 리스트")
        private List<ResponseArchiveDto> results;

        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지 당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static ResponseArchiveListDto build(Page<ResponseArchiveDto> responseDtoPage, Integer currentPage, Integer maxResults){
            return ResponseArchiveListDto.builder()
                    .results(responseDtoPage.getContent())
                    .maxResults(maxResults)
                    .currentPage(currentPage)
                    .totalPages(responseDtoPage.getTotalPages())
                    .totalElements(responseDtoPage.getTotalElements())
                    .build();
        }

    }

}
