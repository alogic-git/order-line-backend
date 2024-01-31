package com.orderline.admin.user_qa_comment.controller;

import com.orderline.basic.model.dto.CreateResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.orderline.admin.user_qa_comment.model.dto.UserQaCommentDto;
import com.orderline.admin.user_qa_comment.service.UserQaCommentService;
import com.orderline.basic.exception.InternalServerErrorException;
import com.orderline.basic.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.orderline.basic.model.dto.ApiResponseDto;

import javax.annotation.Resource;
import javax.validation.Valid;


@Api(tags={"51. Admin"})
@RestController
@RequestMapping(path = "/admin/qa", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserQaCommentController {

    public static final String NOT_FOUND_MESSAGE = "존재하지 않는 게시물입니다.";

    @Resource(name = "userQaCommentService")
    UserQaCommentService userQaCommentService;

    @ApiOperation(value = "고객 문의 조회", notes = "입력된 ID에 해당하는 고객문의를 읽습니다.")
    @GetMapping("/{userQaId}")
    @ResponseStatus(HttpStatus.OK)
    public UserQaCommentDto.ResponseUserQaDto getUserQa(
            @ApiParam(value = "userQaId", required = true) @PathVariable Long userQaId) {

        UserQaCommentDto.ResponseUserQaDto result = userQaCommentService.getUserQa(userQaId);
        if (result == null) {
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }

        return result;
    }

    @ApiOperation(value = "고객문의 목록 조회", notes = "고객문의 리스트를 가져옵니다.")
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public UserQaCommentDto.ResponseUserQaListDto getUserQaList(
            @RequestParam(value="page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value="max-results", required = false, defaultValue = "10")  Integer maxResults) {

        Pageable pageable = PageRequest.of(page, maxResults, Sort.Direction.DESC, "modDt");
        Page<UserQaCommentDto.ResponseUserQaDto> systemQuestions = userQaCommentService.getUserQaList(pageable);

        return UserQaCommentDto.ResponseUserQaListDto.build(systemQuestions, page, maxResults);
    }

    @ApiOperation(value = "고객 문의 답변 목록 조회", notes = "입력된 ID에 해당하는 고객문의 답변 목록을 가져옵니다.")
    @GetMapping("/{userQaId}/comment/{userQaCommentId}")
    @ResponseStatus(HttpStatus.OK)
    public UserQaCommentDto.ResponseUserQaCommentDto getUserQaCommentList(
            @ApiParam(value = "userQaId", required = true) @PathVariable Long userQaId,
            @ApiParam(value = "userQaCommentId", required = true) @PathVariable Long userQaCommentId){

        UserQaCommentDto.ResponseUserQaCommentDto result = userQaCommentService.getUserQaComment(userQaCommentId);
        if (result == null) {
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }

        return result;
    }


    @ApiOperation(value = "고객 문의 답변 목록 조회", notes = "입력된 ID에 해당하는 고객문의 답변 목록을 가져옵니다.")
    @GetMapping("/{userQaId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public UserQaCommentDto.ResponseUserQaCommentListDto getUserQaCommentList(
            @ApiParam(value = "userQaId", required = true) @PathVariable Long userQaId,
            @RequestParam(value="page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value="max-results", required = false, defaultValue = "10")  Integer maxResults) {

        Pageable pageable = PageRequest.of(page, maxResults, Sort.Direction.DESC, "modDt");
        Page<UserQaCommentDto.ResponseUserQaCommentDto> userQaComments = userQaCommentService.getUserQaCommentList(userQaId, pageable);


        return UserQaCommentDto.ResponseUserQaCommentListDto.build(userQaComments, page, maxResults);
    }



    @ApiOperation(value = "고객문의 답변 등록", notes = "고객문의 답변을 등록합니다.")
    @PostMapping("/{userQaId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CreateResponseDto> createUserQaComment(
            @ApiParam(value = "userQaId", required = true) @PathVariable Long userQaId,
            @RequestBody @Valid UserQaCommentDto.RequestUserQaCommentDto createPostRequest) {

        Long result = userQaCommentService.createUserQaComment(userQaId, createPostRequest);
        if (result <= 0L) {
            throw new InternalServerErrorException(NOT_FOUND_MESSAGE);
        }

        return ApiResponseDto.createdResponseEntity(result, CreateResponseDto.builder().id(result).build());
    }


    @ApiOperation(value = "고객문의 답변 삭제", notes = "고객문의 답변을 삭제합니다.")
    @DeleteMapping("/{userQaId}/comment/{userQaCommentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteUserQaComment(
            @ApiParam(value = "userQaId", required = true) @PathVariable Long userQaId,
            @ApiParam(value = "userQaCommentId", required = true) @PathVariable Long userQaCommentId) {

        Long result = userQaCommentService.deleteUserQaComment(userQaCommentId);
        if (result <= 0L) {
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }

        return ResponseEntity.noContent().build();
    }


}
