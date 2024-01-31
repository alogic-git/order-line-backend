package com.ptglue.tutor.controller;

import com.ptglue.basic.enums.order.TutorOrderEnum;
import com.ptglue.basic.model.dto.ApiResponseDto;
import com.ptglue.tutor.service.TutorService;
import com.ptglue.common.user.enums.UserRoleEnum;
import com.ptglue.common.user.model.dto.UserDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.ptglue.branch.model.dto.BranchUserDto;
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

@Api(tags = {"11.Tutor"})
@RestController
@RequestMapping(path = "/manager/tutor", produces = MediaType.APPLICATION_JSON_VALUE)
public class TutorController {

    @Resource(name = "tutorService")
    TutorService tutorService;

    @ApiOperation(value = "지점 강사 상세 조회", notes = "token의 지점에서 user id로 지점의 강사를 상세 조회합니다.")
    @GetMapping("{userId}")
    public BranchUserDto.ResponseBranchUserDto get(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "user id", required = true, defaultValue = "1") @PathVariable Long userId){

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        return tutorService.get(branchId, userId);
    }

    @ApiOperation(value = "지점 강사 목록 조회(정렬, 검색 포함)", notes = "정렬, 검색 포함하여 token의 지점의 강사 목록을 조회합니다.")
    @GetMapping("")
    public BranchUserDto.ResponseBranchUserListDto getList(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "검색할 단어", defaultValue = "%") @RequestParam(required = false, defaultValue = "%") String searchWord,
            @ApiParam(value = "정렬", defaultValue = "TUTOR_NAME_ASC") @RequestParam(required = false, defaultValue = "TUTOR_NAME_ASC") TutorOrderEnum orderBy,
            @ApiParam(value = "페이지 번호", defaultValue = "0") Integer page,
            @ApiParam(value = "페이지 크기", defaultValue = "10") Integer maxResults) {

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");

        Sort sort;
        if (orderBy.getDirection().equals("ASC")) {
            sort = Sort.by(Sort.Direction.ASC, orderBy.getColumn());
        } else {
            sort = Sort.by(Sort.Direction.DESC, orderBy.getColumn());
        }
        Pageable pageable = PageRequest.of(page, maxResults, sort);

        if (!searchWord.equals("%")) {
            searchWord = '%' + searchWord + '%';
        }
        Page<BranchUserDto.ResponseBranchUserDto> branchUserList = tutorService.getList(branchId, searchWord, pageable);
        return BranchUserDto.ResponseBranchUserListDto.build(branchUserList, page, maxResults);
    }

    @ApiOperation(value = "강사 보관함 이동")
    @PatchMapping("{userId}/archive")
    @ResponseStatus(HttpStatus.OK)
    public BranchUserDto.ResponseBranchUserDto archive(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "user id", required = true, defaultValue = "1") @PathVariable Long userId){

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        return tutorService.archive(branchId, userId);
    }

    @ApiOperation(value = "강사 별칭 수정")
    @PatchMapping("{userId}")
    public BranchUserDto.ResponseBranchUserDto updateNickname(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "user id", required = true, defaultValue = "1") @PathVariable Long userId,
            @RequestBody BranchUserDto.RequestUpdateNickname requestUpdateNickname){

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        return tutorService.updateNickname(branchId, userId, requestUpdateNickname);
    }

    @ApiOperation(value = "현재 선택 지점에 등록된 고객인지 조회", notes = "휴대폰번호로 지점의 고객을 조회합니다.")
    @GetMapping("search/phone")
    public UserDto.ResponseUser getBranchUser(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "등록할 역할", required = true, defaultValue = "1") @RequestParam UserRoleEnum role,
            @ApiParam(value = "휴대폰 번호", required = true, defaultValue = "01012345678") @RequestParam String phone){

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        return tutorService.getBranchUser(branchId, role, phone);
    }

    @ApiOperation(value = "이미 아이디가 있는 강사 추가")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BranchUserDto.ResponseBranchUserDto> createBranchUser(
            HttpServletRequest httpServletRequest,
            @RequestBody @Valid BranchUserDto.RequestBranchUserDto requestBranchUserDto) {

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        BranchUserDto.ResponseBranchUserDto branchUserInfo = tutorService.createBranchUser(branchId, requestBranchUserDto);
        return ApiResponseDto.createdResponseEntity(branchUserInfo.getUserId(), branchUserInfo);
    }

    @ApiOperation(value = "강사 권한 편집")
    @PatchMapping("{userId}/permission")
    public List<BranchUserDto.ResponseBranchUserPermissionDto> updatePermission(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "user id", required = true, defaultValue = "1") @PathVariable Long userId,
            @RequestBody @Valid List<BranchUserDto.RequestBranchUserPermissionDto> requestBranchUserPermissionDtoList){

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        return tutorService.updatePermission(branchId, userId, requestBranchUserPermissionDtoList);
    }

    @ApiOperation(value = "지점 강사 권한 조회", notes = "지점 강사를 권한 조회합니다.")
    @GetMapping("{userId}/permission")
    public List<BranchUserDto.ResponseBranchUserPermissionDto> getUserPermission(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "userId", required = true, defaultValue = "1") @PathVariable Long userId){

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        return tutorService.getUserPermission(branchId, userId);
    }

    @ApiOperation(value = "선택한 강사의 현재 지점 연결 해제", notes = "선택한 강사의 현재 지점 연결 해제합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("{userId}/disconnect")
    public BranchUserDto.ResponseBranchUserDto disconnectUser(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "user id", required = true, defaultValue = "1") @PathVariable Long userId){

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        return tutorService.disconnectUser(branchId, userId);
    }

    @ApiOperation(value = "지점 재연결", notes = "강사님에게 지점 재연결을 요청합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("{userId}/reconnect")
    public BranchUserDto.ResponseBranchUserDto reconnectUser(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "user id", required = true, defaultValue = "1") @PathVariable Long userId){

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        return tutorService.reconnectUser(branchId, userId);
    }
}
