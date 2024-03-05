package com.orderline.site.controller;

import com.orderline.basic.model.dto.ApiResponseDto;
import com.orderline.site.model.dto.SiteDto;
import com.orderline.site.service.SiteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.orderline.basic.utils.Constants.DEFAULT_PAGE_NUM;
import static com.orderline.basic.utils.Constants.DEFAULT_PAGE_SIZE;

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
    @GetMapping("/sites")
    public SiteDto.ResponseSiteListDto getSiteList(
            HttpServletRequest httpServletRequest) {

        Long userId = (Long) httpServletRequest.getAttribute("userId");

        return siteService.getSiteList(userId);

    }
}
