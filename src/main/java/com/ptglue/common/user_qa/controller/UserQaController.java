package com.ptglue.common.user_qa.controller;

import com.ptglue.basic.model.dto.CreateResponseDto;
import com.ptglue.common.user_qa.model.dto.UserQaDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.ptglue.basic.exception.InternalServerErrorException;
import com.ptglue.basic.exception.NotFoundException;
import com.ptglue.basic.model.dto.ApiResponseDto;
import com.ptglue.basic.service.CommonService;
import com.ptglue.common.user_qa.service.UserQaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Objects;

@Api(tags={"02.System Question"})
@RestController
@RequestMapping(path = "/common/qa", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserQaController {

    public static final String NOT_FOUND_MESSAGE = "존재하지 않는 게시물입니다.";
    public static final String NOT_FOUND_USER_MESSAGE = "존재하지 않는 유저입니다.";
    @Resource(name = "userQaService")
    UserQaService userQaService;

    @Resource(name = "commonService")
    CommonService commonService;

    @ApiOperation(value = "고객 문의 조회", notes = "입력된 ID에 해당하는 고객문의를 읽습니다.")
    @GetMapping("/{userQaId}")
    @ResponseStatus(HttpStatus.OK)
    public UserQaDto.ResponseUserQaDto get(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "userQaId", required = true) @PathVariable Long userQaId) {

        Long userId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        UserQaDto.ResponseUserQaDto result = userQaService.get(userQaId, userId);
        if (result == null) {

            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }

        return result;
    }

    @ApiOperation(value = "내 고객문의 목록 조회", notes = "고객문의 리스트를 가져옵니다.")
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public UserQaDto.ResponseUserQaListDto getList(
            HttpServletRequest httpServletRequest,
            @RequestParam(value="page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value="max-results", required = false, defaultValue = "10")  Integer maxResults) {

        Long userId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        Pageable pageable = PageRequest.of(page, maxResults, Sort.Direction.DESC, "modDateTime");
        Page<UserQaDto.ResponseUserQaDto> userQas = userQaService.getList(userId, pageable);

        return UserQaDto.ResponseUserQaListDto.build(userQas, page, maxResults);
    }

    @ApiOperation(value = "고객문의 등록", notes = "고객문의를 등록합니다.")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CreateResponseDto> createQuestion(
            HttpServletRequest httpServletRequest,
            @RequestBody @Valid UserQaDto.RequestUserQaDto createPostRequest) {

        Long userId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        Long result = userQaService.create(userId, createPostRequest);
        if (result <= 0L) {
            throw new InternalServerErrorException(NOT_FOUND_USER_MESSAGE);
        }
        commonService.sendEmail("support@pters.co.kr", createPostRequest.getTitle(), createPostRequest.getContents());

        return ApiResponseDto.createdResponseEntity(result, CreateResponseDto.builder().id(result).build());
    }

    @ApiOperation(value = "고객문의 삭제", notes = "게시물을 삭제합니다.")
    @DeleteMapping("/{userQaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(
            @ApiParam(value = "userQaId", required = true) @PathVariable Long userQaId) {

        Long result = userQaService.delete(userQaId);
        if (result <= 0L) {
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }

        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "고객 문의 답변 조회", notes = "입력된 ID에 해당하는 고객문의 답변 목록을 가져옵니다.")
    @GetMapping("/{userQaId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public UserQaDto.ResponseUserQaCommentListDto getCommentList(
            @ApiParam(value = "userQaId", required = true) @PathVariable Long userQaId,
            @RequestParam(value="page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value="max-results", required = false, defaultValue = "10")  Integer maxResults) {

        Pageable pageable = PageRequest.of(page, maxResults, Sort.Direction.DESC, "modDateTime");
        Page<UserQaDto.ResponseUserQaCommentDto> userQaComments = userQaService.getCommentList(userQaId, pageable);

        return UserQaDto.ResponseUserQaCommentListDto.build(userQaComments, page, maxResults);
    }

    @ApiOperation(value = "고객 문의 답변 조회", notes = "입력된 ID에 해당하는 고객문의 답변 목록을 가져옵니다.")
    @GetMapping("/{userQaId}/comment/{userQaCommentId}")
    @ResponseStatus(HttpStatus.OK)
    public UserQaDto.ResponseUserQaCommentDto getComment(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "userQaId", required = true) @PathVariable Long userQaId,
            @ApiParam(value = "userQaCommentId", required = true) @PathVariable Long userQaCommentId) {

        Long userId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        UserQaDto.ResponseUserQaCommentDto result = userQaService.getComment(userQaCommentId);
        if (result == null || Objects.equals(result.getUserQa().getId(), userQaId) || Objects.equals(result.getUserQa().getUser().getId(), userId)) {
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }

        return result;
    }

}
