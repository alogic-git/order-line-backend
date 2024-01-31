package com.orderline.branch.controller;

import com.orderline.branch.model.dto.UserBranchSettingDto;
import com.orderline.branch.service.BranchService;
import com.orderline.branch.service.UserBranchSettingService;
import com.orderline.common.user.enums.UserRoleEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.orderline.basic.model.dto.ApiResponseDto;
import com.orderline.branch.model.dto.BranchDto;
import com.orderline.branch.model.dto.BranchUserDto;
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
import java.util.List;

@Api(tags = {"10.Branch"})
@RestController
@RequestMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
public class BranchController {

    @Resource(name = "branchService")
    BranchService branchService;

    @Resource(name = "userBranchSettingService")
    UserBranchSettingService userBranchSettingService;

    @ApiOperation(value = "지점 상세 조회", notes = "선택한 지점의 상세 조회합니다.")
    @GetMapping({"manager/branch/{branchId}", "tutor/branch/{branchId}"})
    public BranchDto.ResponseBranchDto get(
            @ApiParam(value = "지점 id", required = true, defaultValue = "1") @PathVariable Long branchId){

        return branchService.get(branchId);
    }

    @ApiOperation(value = "지점 리스트 조회", notes = "token의 userId와 role로 해당하는 지점 리스트를 조회합니다.")
    @GetMapping({"manager/branch", "tutor/branch"})
    public BranchDto.ResponseBranchListDto getList(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "페이지 번호", required = true, defaultValue = "0") Integer page,
            @ApiParam(value = "페이지당 항목 수", required = true, defaultValue = "10") Integer maxResults){

        Long userId = (Long) httpServletRequest.getAttribute("userId");
        Sort sort = Sort.by(Sort.Direction.DESC, "reg_dt"); // TODO: 이름순 정렬로 변경
        Pageable pageable = PageRequest.of(page, maxResults, sort);
        Page<BranchDto.ResponseBranchDto> myWorkBranchPage = branchService.getList(userId, pageable);
        return BranchDto.ResponseBranchListDto.build(myWorkBranchPage, page, maxResults);
    }

    @ApiOperation(value = "지점 삭제")
    @DeleteMapping("manager/branch/{branchId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public  ResponseEntity<Void> delete(
            @ApiParam(value = "지점 id", required = true, defaultValue = "1") @PathVariable Long branchId){

        branchService.delete(branchId);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "지점 수정")
    @PatchMapping("manager/branch/{branchId}")
    public BranchDto.ResponseBranchDto update(
            @ApiParam(value = "지점 id", required = true, defaultValue = "1") @PathVariable Long branchId,
            @RequestBody @Valid BranchDto.RequestBranchDto requestBranchDto){

        return branchService.update(branchId, requestBranchDto);
    }

    @ApiOperation(value = "지점 추가")
    @PostMapping("manager/branch")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BranchDto.ResponseBranchDto> create(
            HttpServletRequest httpServletRequest,
            @RequestBody @Valid BranchDto.RequestBranchDto requestBranchDto){

        Long userId = (Long) httpServletRequest.getAttribute("userId");
        BranchDto.ResponseBranchDto branchInfo = branchService.create(userId, requestBranchDto);
        return ApiResponseDto.createdResponseEntity(branchInfo.getBranchId(), branchInfo);
    }

    @ApiOperation(value = "지점 보관함 이동")
    @PatchMapping("manager/branch/{branchId}/archive")
    @ResponseStatus(HttpStatus.OK)
    public BranchDto.ResponseBranchDto archive(
            @ApiParam(value = "지점 id", required = true, defaultValue = "1") @PathVariable Long branchId){

        return branchService.archive(branchId);
    }

    @ApiOperation(value = "선택한 지점의 내 권한 조회")
    @GetMapping({"manager/branch/{branchId}/permission", "tutor/branch/{branchId}/permission"})
    public List<BranchUserDto.ResponseBranchUserPermissionDto> getMyPermission(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "지점 id", required = true, defaultValue = "1") @PathVariable Long branchId,
            @ApiParam(value = "사용자 역할", required = true, defaultValue = "TUTEE") @RequestParam(required = false, defaultValue = "false") UserRoleEnum roleType){

        Long userId = (Long) httpServletRequest.getAttribute("userId");
        return branchService.getMyPermission(branchId, userId, roleType);
    }

    @ApiOperation(value = "알람 수정", notes = "관리자의 알람값을 수정")
    @PatchMapping({"tutor/branch/alarm/setting", "manager/branch/alarm/setting"})
    @ResponseStatus(HttpStatus.OK)
    public UserBranchSettingDto.ResponseAlarmSettingDto updateStaffAlarmSetting(
            HttpServletRequest httpServletRequest,
            UserBranchSettingDto.RequestStaffAlarmSettingDto requestStaffAlarmSettingDto
    ) {
        Long userId = (Long) httpServletRequest.getAttribute("userId");
        Long branchId = (Long) httpServletRequest.getAttribute("branchId");

        return userBranchSettingService.updateStaffBranchSetting(userId, branchId, requestStaffAlarmSettingDto);
    }
}
