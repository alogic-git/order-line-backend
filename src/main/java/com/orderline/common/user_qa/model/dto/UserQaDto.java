package com.orderline.common.user_qa.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.orderline.basic.enums.QaDetailTypeEnum;
import com.orderline.basic.enums.QaTypeEnum;
import com.orderline.common.user_qa.model.entity.UserQa;
import io.swagger.annotations.ApiModelProperty;
import com.orderline.admin.user_qa_comment.model.entity.UserQaComment;
import com.orderline.common.user.model.entity.User;
import lombok.*;
import org.springframework.data.domain.Page;

import javax.validation.constraints.*;
import java.util.List;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserQaDto {

    @Getter
    @Builder
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    public static class ResponseUserQaDto {

        @ApiModelProperty(value = "userQaId", example = "1")
        private Long userQaId;

        @ApiModelProperty(value = "질문 타입", example = "FUNCTION")
        private QaTypeEnum qaType;

        @ApiModelProperty(value = "질문 세부 타입", example = "SCHEDULE")
        private QaDetailTypeEnum qaDetailType;

        @ApiModelProperty(value = "제목")
        private String title;

        @ApiModelProperty(value = "내용")
        private String contents;

        @ApiModelProperty(value = "email")
        private String email;

        public static ResponseUserQaDto toDto(final UserQa userQa) {
            return ResponseUserQaDto.builder()
                    .userQaId(userQa.getId())
                    .qaType(userQa.getQaType())
                    .qaDetailType(userQa.getQaDetailType())
                    .title(userQa.getTitle())
                    .contents(userQa.getContents())
                    .build();
        }

    }

    @Getter
    @Builder
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    public static class ResponseUserQaAdminDto {

        @ApiModelProperty(value = "userQaId", example = "1")
        private Long userQaId;

        @ApiModelProperty(value = "유저")
        private User user;

        @ApiModelProperty(value = "질문 타입", example = "FUNCTION")
        private QaTypeEnum qaType;

        @ApiModelProperty(value = "질문 세부 타입", example = "SCHEDULE")
        private QaDetailTypeEnum qaDetailType;

        @ApiModelProperty(value = "제목")
        private String title;

        @ApiModelProperty(value = "내용")
        private String contents;

        @ApiModelProperty(value = "email")
        private String email;

        public static ResponseUserQaAdminDto toDto(final UserQa userQa) {
            return ResponseUserQaAdminDto.builder()
                    .userQaId(userQa.getId())
                    .user(userQa.getUser())
                    .qaType(userQa.getQaType())
                    .qaDetailType(userQa.getQaDetailType())
                    .title(userQa.getTitle())
                    .contents(userQa.getContents())
                    .build();
        }

    }


    @Getter
    @Builder
    public static class RequestUserQaDto {

        @ApiModelProperty(value = "질문 타입", example = "FUNCTION / ERROR")
        @NotNull(message = "질문 타입을 입력해주세요.")
        private QaTypeEnum qaType;

        @ApiModelProperty(value = "질문 세부 타입", example = "SCHEDULE / KLASS")
        private QaDetailTypeEnum qaDetailType;

        @ApiModelProperty(value = "제목")
        @NotNull(message = "제목을 입력해주세요.")
        private String title;

        @ApiModelProperty(value = "내용")
        @NotNull(message = "내용을 입력해주세요.")
        private String contents;

        @ApiModelProperty(value = "email")
        @NotNull(message = "email 을 입력해주세요.")
        private String email;

        public UserQa toEntity(User user, UserQaDto.RequestUserQaDto createUserQaRequest){
            return UserQa.builder()
                    .user(user)
                    .qaType(createUserQaRequest.getQaType())
                    .qaDetailType(createUserQaRequest.getQaDetailType())
                    .title(createUserQaRequest.getTitle())
                    .contents(createUserQaRequest.getContents())
                    .email(createUserQaRequest.getEmail())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ResponseUserQaListDto {

        @ApiModelProperty(value = "고객 문의 리스트")
        private List<ResponseUserQaDto> results;
        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static ResponseUserQaListDto build(Page<UserQaDto.ResponseUserQaDto> responseDtoPage, Integer currentPage, Integer maxResults){
            return UserQaDto.ResponseUserQaListDto.builder()
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
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    public static class ResponseUserQaCommentDto
    {
        @ApiModelProperty(value = "답변 id", example = "1")
        @NotBlank(message = "답변 ID")
        private Long userQaCommentId;

        @ApiModelProperty(value = "질문 정보")
        @NotBlank(message = "질문 정보")
        private UserQa userQa;

        @ApiModelProperty(value = "답변 제목")
        @NotNull(message = "답변 내용을 입력해주세요.")
        private String title;

        @ApiModelProperty(value = "답변 내용")
        @NotNull(message = "답변 내용을 입력해주세요.")
        private String contents;

        public static ResponseUserQaCommentDto toDto(final UserQaComment userQaComment) {
            return ResponseUserQaCommentDto.builder()
                    .userQaCommentId(userQaComment.getId())
                    .userQa(userQaComment.getUserQa())
                    .title(userQaComment.getTitle())
                    .contents(userQaComment.getContents())
                    .build();
        }
    }
    @Getter
    @Builder
    public static class ResponseUserQaCommentListDto {

        @ApiModelProperty(value = "고객 문의 답변 리스트")
        private List<ResponseUserQaCommentDto> results;
        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static ResponseUserQaCommentListDto build(Page<UserQaDto.ResponseUserQaCommentDto> responseDtoPage, Integer currentPage, Integer maxResults){
            return UserQaDto.ResponseUserQaCommentListDto.builder()
                    .results(responseDtoPage.getContent())
                    .maxResults(maxResults)
                    .currentPage(currentPage)
                    .totalPages(responseDtoPage.getTotalPages())
                    .totalElements(responseDtoPage.getTotalElements())
                    .build();
        }
    }

}
