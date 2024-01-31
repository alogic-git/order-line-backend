package com.ptglue.branch.model.dto;

import io.swagger.annotations.ApiModelProperty;
import com.ptglue.branch.enums.ReservationTypeEnum;
import com.ptglue.branch.enums.SubjectTypeEnum;
import com.ptglue.branch.model.entity.BranchUserRole;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TuteeBranchDto {
    @Builder
    @Getter
    public static class ResponseTuteeBranchDto {

        @ApiModelProperty(value = "지점명", example = "베르테미 PT + 필라테스 보라매 병원점")
        private String branchName;

        @ApiModelProperty(value = "지점 대표 사진", example = "img")
        private String imageUri;

        @ApiModelProperty(value = "분야", example = "스포츠")
        private SubjectTypeEnum subjectType;

        @ApiModelProperty(value = "상세 분야", example = "아웃도어")
        private String subjectDetail;

        @ApiModelProperty(value = "지점의 예약 타입", example = "자유 예약형")
        private ReservationTypeEnum reservationType;

        @ApiModelProperty(value = "지점의 공개 메모", example = "오늘 폐업합니다.")
        private String publicMemo;

        @ApiModelProperty(value = "지점 마지막 조회 여부", example = "false")
        private Boolean lastViewYn;

        @ApiModelProperty(value = "유효한 수강권 갯수", example = "10")
        private Integer activeTicketNum;

        public static ResponseTuteeBranchDto toDto(final BranchUserRole branchUserRole){
           return ResponseTuteeBranchDto.builder()
                   .branchName(branchUserRole.getBranch().getBranchName())
                   .imageUri(branchUserRole.getBranch().getImageUri())
                   .subjectType(branchUserRole.getBranch().getSubjectType())
                   .subjectDetail(branchUserRole.getBranch().getSubjectDetail())
                   .reservationType(branchUserRole.getBranch().getReservationType())
                   .publicMemo(branchUserRole.getBranch().getPublicMemo())
                   .lastViewYn(branchUserRole.getLastViewYn())
                   .activeTicketNum(branchUserRole.getActiveTicketNum())
                   .build();
        }
    }

    @Builder
    @Getter
    public static class ResponseTuteeBranchListDto {
        @ApiModelProperty(value = "지점 리스트")
        private List<TuteeBranchDto.ResponseTuteeBranchDto> results;

        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static TuteeBranchDto.ResponseTuteeBranchListDto build(Page<TuteeBranchDto.ResponseTuteeBranchDto> responseDtoPage, Integer currentPage, Integer maxResults){
            return TuteeBranchDto.ResponseTuteeBranchListDto.builder()
                    .results(responseDtoPage.getContent())
                    .maxResults(maxResults)
                    .currentPage(currentPage)
                    .totalPages(responseDtoPage.getTotalPages())
                    .totalElements(responseDtoPage.getTotalElements())
                    .build();
        }
    }
}
