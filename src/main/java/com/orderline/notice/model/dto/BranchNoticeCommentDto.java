package com.orderline.notice.model.dto;

import com.orderline.branch.model.entity.Branch;
import com.orderline.common.user.model.entity.User;
import com.orderline.notice.model.entity.BranchNotice;
import com.orderline.notice.model.entity.BranchNoticeComment;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotBlank;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BranchNoticeCommentDto {
    @Getter
    @Builder
    public static class ResponseBranchNoticeCommentDto{
        @ApiModelProperty(value = "지점내 공지 댓글 ID")
        private Long id;

        @ApiModelProperty(value = "지점 ID")
        private Long branchId;

        @ApiModelProperty(value = "지점내 공지 ID")
        private Long branchNoticeId;

        @ApiModelProperty(value = "작성자 ID")
        private Long userId;

        @ApiModelProperty(value = "댓글 제목")
        private String title;

        @ApiModelProperty(value = "댓글 내용")
        private String contents;

        public static ResponseBranchNoticeCommentDto toDto(BranchNoticeComment branchNoticeComment){
            return ResponseBranchNoticeCommentDto.builder()
                    .id(branchNoticeComment.getId())
                    .branchId(branchNoticeComment.getBranch().getId())
                    .branchNoticeId(branchNoticeComment.getBranchNotice().getId())
                    .userId(branchNoticeComment.getUser().getId())
                    .title(branchNoticeComment.getTitle())
                    .contents(branchNoticeComment.getContents())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ResponseBranchNoticeCommentListDto {
        @ApiModelProperty(value = "댓글 리스트")
        private List<BranchNoticeCommentDto.ResponseBranchNoticeCommentDto> results;

        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static BranchNoticeCommentDto.ResponseBranchNoticeCommentListDto build(Page<BranchNoticeCommentDto.ResponseBranchNoticeCommentDto> responseDtoPage, Integer currentPage, Integer maxResults){
            return BranchNoticeCommentDto.ResponseBranchNoticeCommentListDto.builder()
                    .results(responseDtoPage.getContent())
                    .maxResults(maxResults)
                    .currentPage(currentPage)
                    .totalPages(responseDtoPage.getTotalPages())
                    .totalElements(responseDtoPage.getTotalElements())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class RequestBranchNoticeCommentDto{
        @NotBlank(message = "제목이 유효하지 형태입니다.")
        @ApiModelProperty(value = "댓글 제목")
        private String title;

        @NotBlank(message = "내용이 유효하지 형태입니다.")
        @ApiModelProperty(value = "댓글 내용")
        private String contents;

        public BranchNoticeComment toEntity(BranchNotice branchNotice, Branch branch, User user){
            return BranchNoticeComment.builder()
                    .branchNotice(branchNotice)
                    .branch(branch)
                    .user(user)
                    .title(this.title)
                    .contents(this.contents)
                    .build();
        }
    }
}
