package com.ptglue.branch.controller;

import com.ptglue.branch.model.dto.UserBranchSettingDto;
import com.ptglue.branch.service.BranchService;
import com.ptglue.branch.service.UserBranchSettingService;
import com.ptglue.common.user.enums.UserRoleEnum;
import com.ptglue.common.user.model.dto.UserDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.ptglue.branch.model.dto.BranchUserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Api(tags = {"10.Branch"})
@RestController
@RequestMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommonBranchController {

    @Resource(name = "branchService")
    BranchService branchService;

    @Resource(name = "userBranchSettingService")
    UserBranchSettingService userBranchSettingService;

    @ApiOperation(value = "지점 이동", notes = "선택한 지점으로 이동합니다. jwt 토큰의 지점 정보를 변경합니다.")
    @PatchMapping("common/branch/{branchId}/select/{branchUserRoleId}")
    public UserDto.UserInfoDto select(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "지점 id", required = true, defaultValue = "1") @PathVariable Long branchId,
            @ApiParam(value = "지점 사용자 id", required = true, defaultValue = "1") @PathVariable Long branchUserRoleId){

        Long userId = (Long) httpServletRequest.getAttribute("userId");
        return branchService.select(userId, branchUserRoleId);
    }


    @ApiOperation(value = " 선택한 지점 연결 해제", notes = "선택한 지점과 나와의 연결 해제합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("common/branch/{branchId}/disconnect")
    public BranchUserDto.ResponseBranchUserDto disconnectBranch(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "지점 id", required = true, defaultValue = "1") @PathVariable Long branchId,
            @ApiParam(value = "사용자 역할", required = true, defaultValue = "TUTEE") @RequestParam(required = false, defaultValue = "false") UserRoleEnum roleType){

        Long userId = (Long) httpServletRequest.getAttribute("userId");
        return branchService.disconnectBranch(branchId, userId, roleType);
    }

    @ApiOperation(value = "알람 설정한 값들을 조회", notes = "token의 값을 확인하여 관리자의 알람 관련 설정값을 조회")
    @GetMapping({"common/branch/alarm/setting"})
    public UserBranchSettingDto.ResponseAlarmSettingDto getAlarmSetting(
            HttpServletRequest httpServletRequest
    ) {
        Long userId = (Long) httpServletRequest.getAttribute("userId");
        Long branchId = (Long) httpServletRequest.getAttribute("branchId");

        return userBranchSettingService.getBranchSetting(userId, branchId);
    }


}
