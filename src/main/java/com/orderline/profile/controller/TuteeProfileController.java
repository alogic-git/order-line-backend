package com.orderline.profile.controller;

import com.orderline.profile.model.dto.TuteeProfileDto;
import com.orderline.profile.service.ProfileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@Api(tags={"03.Profile"})
@RestController
@RequestMapping(path = "/user/tutee", produces = MediaType.APPLICATION_JSON_VALUE)
//TODO : tutorProfileController 추가로 만들기
public class TuteeProfileController {
    @Resource(name = "profileService")
    ProfileService profileService;

    @ApiOperation(value = "내 정보 수정", notes = "마이페이지에서 내 정보를 수정 합니다.")
    @PatchMapping("profile")
    @ResponseStatus(HttpStatus.OK)
    public TuteeProfileDto.ResponseTuteeProfileDto updateProfile(
            HttpServletRequest httpServletRequest,
            @RequestBody @Valid TuteeProfileDto.RequestTuteeProfileDto requestProfileDto
    ){
        Long userId = (Long) httpServletRequest.getAttribute("userId");

        if (requestProfileDto.getPhoneActiveYn() && profileService.isPhoneChanged(userId, requestProfileDto.getPhone())){
            profileService.disableCurrentActivationCode(userId);
            profileService.updateCurrentUserPhone(requestProfileDto.getPhone());
        }

        return profileService.updateProfile(userId, requestProfileDto);
    }

    @ApiOperation(value = "마이페이지 수강권, 지점 개수 정보 조회 ")
    @GetMapping("profile/branch-ticket/count")
    public TuteeProfileDto.ResponseUserProfileStatisticsDto getStatistics(
            HttpServletRequest httpServletRequest
    ){
        Long userId = (Long) httpServletRequest.getAttribute("userId");

        return profileService.getStatistics(userId);
    }
}
