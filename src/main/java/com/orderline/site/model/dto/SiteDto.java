package com.orderline.site.model.dto;

import com.orderline.site.model.entity.ConstructionCompany;
import com.orderline.site.model.entity.Site;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SiteDto {

    @Getter
    @Builder
    public static class ResponseSiteDto {
        @ApiModelProperty(value = "현장 ID", example = "1")
        private Long id;

        @ApiModelProperty(value = "회사 ID", example = "1")
        private Long companyId;

        @ApiModelProperty(value = "현장명", example = "가락")
        private String name;

        @ApiModelProperty(value = "발주 주소", example = "서울시 강남구 역삼동 123-4")
        private String address;

        @ApiModelProperty(value = "담당자명", example = "홍길동")
        private String managerName;

        public static ResponseSiteDto toDto(Site site) {
            return ResponseSiteDto.builder()
                    .id(site.getId())
                    .companyId(site.getConstructionCompany().getId())
                    .name(site.getName())
                    .address(site.getAddress())
                    .managerName(site.getManagerName())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ResponseSiteListDto {
        @ApiModelProperty(value = "현장 리스트")
        private List<SiteDto.ResponseSiteDto> results;

        public static ResponseSiteListDto toDto(List<Site> siteList) {
            List<SiteDto.ResponseSiteDto> siteListDto = siteList.stream()
                    .map(SiteDto.ResponseSiteDto::toDto)
                    .collect(Collectors.toList());
            return ResponseSiteListDto.builder()
                    .results(siteListDto)
                    .build();
        }
    }



    @Getter
    @Builder
    public static class RequestCreateSiteDto {
        @ApiModelProperty(value = "회사 ID", example = "1")
        private Long companyId;

        @ApiModelProperty(value = "현장명", example = "가락")
        private String name;

        @ApiModelProperty(value = "발주 주소", example = "서울시 강남구 역삼동 123-4")
        private String address;

        @ApiModelProperty(value = "담당자명", example = "홍길동")
        private String managerName;

        public Site toEntity(ConstructionCompany constructionCompany) {
            return Site.builder()
                    .constructionCompany(constructionCompany)
                    .name(name)
                    .address(address)
                    .managerName(managerName)
                    .build();

        }
    }
}
