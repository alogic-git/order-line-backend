package com.orderline.klass.model.dto;

import com.orderline.klass.model.entity.Klass;
import com.orderline.schedule.enums.ScheduleMemberTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KlassTuteeDto {
    @Getter
    @Builder
    public static class ResponseTuteeKlassByTicketIdDto{
        @ApiModelProperty(value = "클래스 ID")
        private Long klassId;

        @ApiModelProperty(value = "수강권 ID")
        private Long ticketId;

        @ApiModelProperty(value = "메인 강사 닉네임")
        private String mainTutorNickName;

        @ApiModelProperty(value = "보조 강사 닉네임")
        private String subTutorNickName;

        @ApiModelProperty(value = "클래스명")
        private String klassName;

        @ApiModelProperty(value = "강의 종류")
        private ScheduleMemberTypeEnum memberType;

        public static ResponseTuteeKlassByTicketIdDto toDto(Klass klass, Long ticketId){
            return  ResponseTuteeKlassByTicketIdDto.builder()
                    .klassId(klass.getId())
                    .ticketId(ticketId)
                    .klassName(klass.getKlassName())
                    .mainTutorNickName(klass.getMainTutorNickname())
                    .subTutorNickName(klass.getSubTutorNickname())
                    .memberType(klass.getMemberType())
                    .build();
        }
    }

    @Builder
    @Getter
    public static class ResponseTuteeKlassByTicketIdListDto {
        @ApiModelProperty(value = "클래스 리스트")
        private List<KlassTuteeDto.ResponseTuteeKlassByTicketIdDto> results;

        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static ResponseTuteeKlassByTicketIdListDto build(Page<KlassTuteeDto.ResponseTuteeKlassByTicketIdDto> responseDtoPage, Integer currentPage, Integer maxResults){
            return ResponseTuteeKlassByTicketIdListDto.builder()
                    .results(responseDtoPage.getContent())
                    .maxResults(maxResults)
                    .currentPage(currentPage)
                    .totalPages(responseDtoPage.getTotalPages())
                    .totalElements(responseDtoPage.getTotalElements())
                    .build();
        }
    }
}
