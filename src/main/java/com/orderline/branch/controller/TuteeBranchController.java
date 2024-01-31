package com.orderline.branch.controller;

import com.orderline.branch.model.dto.TuteeBranchDto;
import com.orderline.branch.model.dto.UserBranchSettingDto;
import com.orderline.branch.service.TuteeBranchService;
import com.orderline.branch.service.UserBranchSettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Api(tags = {"19.Branch Tutee"})
@RestController
@RequestMapping(path = "tutee/branch", produces = MediaType.APPLICATION_JSON_VALUE)
public class TuteeBranchController {
    @Resource(name = "tuteeBranchService")
    TuteeBranchService tuteeBranchService;

    @Resource(name = "userBranchSettingService")
    UserBranchSettingService userBranchSettingService;

    @ApiOperation(value = "지점 상세 조회", notes = "선택한 지점을 상세 조회 합니다.")
    @GetMapping("{branchId}")
    public TuteeBranchDto.ResponseTuteeBranchDto get(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "조회할 지점", required = true, defaultValue = "1") @PathVariable Long branchId
    ){
        Long userId = (Long) httpServletRequest.getAttribute("userId");

        return tuteeBranchService.get(userId, branchId);
    }

    @ApiOperation(value = "지점 목록 조회", notes = "선택한 지점 목록을 조회 합니다.")
    @GetMapping("")
    public TuteeBranchDto.ResponseTuteeBranchListDto getList(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "페이지 번호", required = true, defaultValue = "0") Integer page,
            @ApiParam(value = "페이지당 항목 수", required = true, defaultValue = "10") Integer maxResults
    ){
        Long userId = (Long) httpServletRequest.getAttribute("userId");

        Sort sort = Sort.by(Sort.Direction.DESC, "reg_dt"); //지점 이름순으로 변경. 방법 찾기
        Pageable pageable = PageRequest.of(page, maxResults, sort);
        Page<TuteeBranchDto.ResponseTuteeBranchDto> reponseTuteeBranchDto = tuteeBranchService.getList(userId ,pageable);

        return TuteeBranchDto.ResponseTuteeBranchListDto.build(reponseTuteeBranchDto, page, maxResults);
    }

    @ApiOperation(value = "알람 수정", notes = "회원 알람 값을 수정")
    @PatchMapping("alarm/setting")
    @ResponseStatus(HttpStatus.OK)
    public UserBranchSettingDto.ResponseAlarmSettingDto updateTuteeAlarmSetting(
            HttpServletRequest httpServletRequest,
            UserBranchSettingDto.RequestTuteeAlarmSettingDto requestTuteeAlarmSettingDto
    ) {
        Long userId = (Long) httpServletRequest.getAttribute("userId");
        Long branchId = (Long) httpServletRequest.getAttribute("branchId");

        return userBranchSettingService.updateTuteeBranchSetting(branchId, userId, requestTuteeAlarmSettingDto);
    }
}
