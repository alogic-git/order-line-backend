package com.orderline.site.controller;

import com.orderline.basic.model.dto.ApiResponseDto;
import com.orderline.site.model.dto.SiteDto;
import com.orderline.site.service.SiteService;
import com.orderline.user.model.dto.UserDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Api(tags={"30.Site"})
@RestController
@RequestMapping(path = {"admin/site", "user/site"}, produces = MediaType.APPLICATION_JSON_VALUE)

public class SiteController {

    @Resource(name = "siteService")
    SiteService siteService;
    @ApiOperation(value = "새로운 현장 생성", notes = "새로운 현장을 생성합니다.")
    @PostMapping
    public ResponseEntity<SiteDto.ResponseSiteDto> createOrder(HttpServletRequest httpServletRequest, @RequestBody SiteDto.RequestCreateSiteDto siteDto) {
        Long userId = (Long) httpServletRequest.getAttribute("userId");

        SiteDto.ResponseSiteDto responseCreateSiteDto = siteService.createSite(userId, siteDto);
        String uri = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
        String createUri = uri + "/" + responseCreateSiteDto.getId();

        return ApiResponseDto.createdResponseEntity(createUri, responseCreateSiteDto);
    }

    @ApiOperation(value = "현장 목록 조회", notes = "현장 목록을 조회합니다.")
    @GetMapping
    public SiteDto.ResponseSiteListDto getSiteList(
            HttpServletRequest httpServletRequest) {

        Long userId = (Long) httpServletRequest.getAttribute("userId");

        return siteService.getSiteList(userId);

    }

    @ApiOperation(value = "현장 선택", notes = "현장을 선택합니다.")
    @PatchMapping("{siteId}")
    public UserDto.UserInfoDto selectSite(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "현장 id", required = true) @PathVariable Long siteId) {

        Long userId = (Long) httpServletRequest.getAttribute("userId");
        return siteService.selectSite(userId, siteId);
    }

}
