package com.orderline.notice.controller;

import com.orderline.basic.model.dto.ApiResponseDto;
import com.orderline.notice.model.dto.BranchNoticeCommentDto;
import com.orderline.notice.model.dto.BranchNoticeDto;
import com.orderline.notice.service.BranchNoticeCommentService;
import com.orderline.notice.service.BranchNoticeReadHistoryService;
import com.orderline.notice.service.BranchNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Api(tags = {"71.Branch Notice"})
@RestController
@RequestMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
public class BranchNoticeController {

    @Resource(name = "branchNoticeService")
    BranchNoticeService branchNoticeService;

    @Resource(name = "branchNoticeReadHistoryService")
    BranchNoticeReadHistoryService branchNoticeReadHistoryService;

    @Resource(name = "branchNoticeCommentService")
    BranchNoticeCommentService branchNoticeCommentService;

    @ApiOperation(value = "공지 조회", notes = "공지 목록 조회")
    @GetMapping("common/branch/notice")
    public BranchNoticeDto.ResponseBranchNoticeListDto getList(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "페이지 번호", required = true, defaultValue = "0") Integer page,
            @ApiParam(value = "페이지당 항목 수", required = true, defaultValue = "10") Integer maxResults
    ) {
        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        Pageable pageable = PageRequest.of(page, maxResults);

        Page<BranchNoticeDto.ResponseBranchNoticeDto> branchNoticeDtoPage = branchNoticeService.getList(branchId, pageable);

        return BranchNoticeDto.ResponseBranchNoticeListDto.build(branchNoticeDtoPage, page, maxResults);
    }

    @ApiOperation(value = "공지 상세 조회", notes = "선택한 공지를 상세 조회")
    @GetMapping("common/branch/notice/{branchNoticeId}")
    public BranchNoticeDto.ResponseBranchNoticeDto get(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "branchNoticeId", required = true, defaultValue = "1") @PathVariable Long branchNoticeId
    ) {
        Long userId = (Long) httpServletRequest.getAttribute("userId");
        Long branchId = (Long) httpServletRequest.getAttribute("branchId");

        if (!branchNoticeReadHistoryService.isExist(branchNoticeId, userId)) {
            branchNoticeReadHistoryService.create(branchNoticeId, branchId, userId);
            branchNoticeService.updateHits(branchNoticeId);
        }

        return branchNoticeService.get(branchNoticeId);
    }

//    @ApiModelProperty(value = "좋아요 클릭", notes = "공지에 좋아요 수 수정")
//    @PatchMapping("common/branch/notice/{branchNoticeId}/like")
//    public BranchNoticeDto.ResponseBranchNoticeDto updateLike(
//            @ApiParam(value = "branchNoticeId", required = true, defaultValue = "1") @PathVariable Long branchNoticeId
//    ){
//        return branchNotice = branchNoticeService.updateLike(branchNoticeId);
//    }

    @ApiOperation(value = "공지 댓글 목록 조회", notes = "선택한 공지 댓글 목록 조회")
    @GetMapping("common/branch/notice/{branchNoticeId}/comment")
    public BranchNoticeCommentDto.ResponseBranchNoticeCommentListDto getList(
            @ApiParam(value = "branchNoticeId", required = true, defaultValue = "1") @PathVariable Long branchNoticeId,
            @ApiParam(value = "페이지 번호", required = true, defaultValue = "0") Integer page,
            @ApiParam(value = "페이지당 항목 수", required = true, defaultValue = "10") Integer maxResults
    ) {
        Pageable pageable = PageRequest.of(page, maxResults);

        Page<BranchNoticeCommentDto.ResponseBranchNoticeCommentDto> branchNoticeCommentPage = branchNoticeCommentService.getList(branchNoticeId, pageable);

        return BranchNoticeCommentDto.ResponseBranchNoticeCommentListDto.build(branchNoticeCommentPage, page, maxResults);
    }

    @ApiOperation(value = "댓글 입력", notes = "공지에 댓글을 입력")
    @PostMapping("common/branch/notice/{branchNoticeId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BranchNoticeCommentDto.ResponseBranchNoticeCommentDto> create(
            HttpServletRequest httpServletRequest,
            @RequestBody @Valid BranchNoticeCommentDto.RequestBranchNoticeCommentDto requestBranchNoticeComment,
            @ApiParam(value = "branchNoticeId", required = true, defaultValue = "1") @PathVariable Long branchNoticeId
    ){
        Long userId = (Long) httpServletRequest.getAttribute("userId");
        Long branchId = (Long) httpServletRequest.getAttribute("branchId");

        BranchNoticeCommentDto.ResponseBranchNoticeCommentDto branchNoticeInfo = branchNoticeCommentService.create(branchNoticeId, branchId, userId, requestBranchNoticeComment);

        return ApiResponseDto.createdResponseEntity(branchNoticeInfo.getId(), branchNoticeInfo);
    }

    @ApiOperation(value = "댓글 수정", notes = "공지에 사용자가 입력한 댓글 수정")
    @PatchMapping("common/branch/notice/{branchNoticeId}/comment/{branchNoticeCommentId}")
    @ResponseStatus(HttpStatus.OK)
    public BranchNoticeCommentDto.ResponseBranchNoticeCommentDto update(
            @RequestBody @Valid BranchNoticeCommentDto.RequestBranchNoticeCommentDto requestBranchNoticeComment,
            @ApiParam(value = "branchNoticeId", required = true, defaultValue = "1") @PathVariable Long branchNoticeId,
            @ApiParam(value = "branchNoticeCommentId", required = true, defaultValue = "1") @PathVariable Long branchNoticeCommentId
    ){
        return branchNoticeCommentService.update(branchNoticeCommentId, requestBranchNoticeComment);
    }

    @ApiOperation(value = "댓글 삭제", notes = "공지 사용자가 입력한 댓글 삭제")
    @DeleteMapping("common/branch/notice/{branchNoticeId}/comment/{branchNoticeCommentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(
            @ApiParam(value = "branchNoticeId", required = true, defaultValue = "1") @PathVariable Long branchNoticeId,
            @ApiParam(value = "branchNoticeCommentId", required = true, defaultValue = "1") @PathVariable Long branchNoticeCommentId
    ){
        branchNoticeCommentService.delete(branchNoticeCommentId);
        return ResponseEntity.noContent().build();
    }
}
