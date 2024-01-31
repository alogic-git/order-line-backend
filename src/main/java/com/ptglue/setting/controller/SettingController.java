package com.ptglue.setting.controller;

import com.ptglue.branch.enums.FunctionTypeEnum;
import com.ptglue.branch.model.dto.BranchDto;
import com.ptglue.setting.model.dto.ArchiveDto;
import com.ptglue.setting.service.ArchiveService;
import com.ptglue.branch.service.BranchService;
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

@Api(tags = {"90.Setting"})
@RestController
@RequestMapping(path = {"manager/setting"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class SettingController {


    @Resource(name = "archiveService")
    ArchiveService archiveService;

    @Resource(name = "branchService")
    BranchService branchService;

    @ApiOperation(value = "지점 설정 조회")
    @GetMapping("")
    public BranchDto.ResponseBranchSettingDto getBranchSetting(
            HttpServletRequest httpServletRequest) {

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        return branchService.getBranchSetting(branchId);
    }

    @ApiOperation(value = "지점 설정 수정")
    @PatchMapping("")
    public BranchDto.ResponseBranchSettingDto updateBranchSetting(
            HttpServletRequest httpServletRequest,
            @Valid @RequestBody BranchDto.RequestBranchSettingDto requestBranchSettingDto) {

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        return branchService.updateBranchSetting(branchId, requestBranchSettingDto);
    }

    @ApiOperation(value = "보관함 조회")
    @GetMapping("archive")
    public ArchiveDto.ResponseArchiveListDto getArchive(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "분야") @RequestParam(required = false) FunctionTypeEnum category,
            @ApiParam(value = "페이지", defaultValue = "0") @RequestParam Integer page,
            @ApiParam(value = "페이지 당 항목 수", defaultValue = "10") @RequestParam Integer maxResults) {

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        Pageable pageable = PageRequest.of(page, maxResults);
        Page<ArchiveDto.ResponseArchiveDto> archiveList = null;
        if (category != null) {
            archiveList = archiveService.getArchiveListWithCategory(branchId, category, pageable);
        } else {
            archiveList = archiveService.getArchiveList(branchId, pageable);
        }
        return ArchiveDto.ResponseArchiveListDto.build(archiveList, page, maxResults);
    }

    @ApiOperation(value = "보관함에서 복구")
    @PatchMapping("archive/{category}/{categoryId}")
    public ResponseEntity<Void> recovery(
            @ApiParam(value = "분야", required = true, defaultValue = "TUTEE") @PathVariable FunctionTypeEnum category,
            @ApiParam(value = "분야 아이디", required = true, defaultValue = "1") @PathVariable Long categoryId) {

        archiveService.recovery(category, categoryId);
        return  ResponseEntity.ok().build();
    }

    @ApiOperation(value = "보관함에서 영구삭제")
    @DeleteMapping("archive/{category}/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(
            @ApiParam(value = "분야", required = true, defaultValue = "TUTEE") @PathVariable FunctionTypeEnum category,
            @ApiParam(value = "분야 아이디", required = true, defaultValue = "1") @PathVariable Long categoryId) {

        archiveService.delete(category, categoryId);
        return ResponseEntity.noContent().build();
    }
}