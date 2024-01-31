package com.ptglue.klass.controller;

import com.ptglue.basic.enums.order.KlassOrderEnum;
import com.ptglue.branch.model.dto.BranchTuteeDto;
import com.ptglue.klass.model.entity.Klass;
import com.ptglue.product.model.dto.ProductDto;
import com.ptglue.schedule.model.dto.RepeatScheduleDto;
import com.ptglue.schedule.model.dto.ScheduleDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.ptglue.basic.model.dto.ApiResponseDto;
import com.ptglue.basic.model.dto.CommonDto;
import com.ptglue.branch.model.dto.BranchUserDto;
import com.ptglue.klass.model.dto.KlassDto;
import com.ptglue.klass.service.KlassService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Api(tags = {"30.Klass"})
@RestController
@RequestMapping(path = {"manager/klass", "tutor/klass"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class KlassController {

    @Resource(name = "klassService")
    KlassService klassService;

    @ApiOperation(value = "클래스 상세 조회", notes = "선택한 클래스를 상세 조회합니다.")
    @GetMapping("{klassId}")
    public KlassDto.ResponseKlassDto get(
            @ApiParam(value = "klass id", required = true, defaultValue = "1") @PathVariable Long klassId) {

        return klassService.get(klassId);
    }

    @ApiOperation(value = "클래스를 수강할 수 있는 수강권 조회", notes = "선택한 클래스를 수강할 수 있는 수강권을 조회합니다.")
    @GetMapping("{klassId}/product")
    public List<ProductDto.ResponseProductDto> getKlassProduct(
            @ApiParam(value = "klass id", required = true, defaultValue = "1") @PathVariable Long klassId) {

        return klassService.getKlassProduct(klassId);
    }

    @ApiOperation(value = "클래스 리스트 조회", notes = "token의 branch Id에 해당하는 클래스 리스트를 조회합니다.")
    @GetMapping("")
    public KlassDto.ResponseKlassListDto get(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "검색할 단어", defaultValue = "%") @RequestParam(required = false, defaultValue = "%") String searchWord,
            @ApiParam(value = "정렬", defaultValue ="KLASS_NAME_ASC" ) @RequestParam(required = false, defaultValue = "KLASS_NAME_ASC") KlassOrderEnum orderBy,
            @ApiParam(value = "페이지 번호", required = true, defaultValue = "0") Integer page,
            @ApiParam(value = "페이지당 항목 수", required = true, defaultValue = "10") Integer maxResults){

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
        Page<KlassDto.ResponseKlassDto> klassPage = klassService.getList(branchId, searchWord, pageable);
        return KlassDto.ResponseKlassListDto.build(klassPage, page, maxResults);
    }

    @ApiOperation(value = "자유 예약형 클래스 추가")
    @PostMapping("free")
    public ResponseEntity<KlassDto.ResponseKlassDto> createFreeKlass(
            HttpServletRequest httpServletRequest,
            @RequestBody @Valid KlassDto.RequestFreeKlassDto requestKlassDto) {

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        KlassDto.ResponseKlassDto klassInfo = klassService.createFreeKlass(branchId, requestKlassDto);
        String uri = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
        String createUri = uri.replace("free", String.valueOf(klassInfo.getKlassId()));
        return ApiResponseDto.createdResponseEntity(createUri, klassInfo);
    }

    @ApiOperation(value = "사전 개설형 클래스 추가")
    @PostMapping("limited")
    public ResponseEntity<KlassDto.ResponseKlassDto> createLimitedKlass(
            HttpServletRequest httpServletRequest,
            @RequestBody @Valid KlassDto.RequestLimitedKlassDto requestKlassDto) {

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        KlassDto.ResponseKlassDto klassInfo = klassService.createLimitedKlass(branchId, requestKlassDto);
        String uri = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
        String createUri = uri.replace("limited", String.valueOf(klassInfo.getKlassId()));
        return ApiResponseDto.createdResponseEntity(createUri, klassInfo);
    }

    @ApiOperation(value = "클래스 색상 조회")
    @GetMapping("color")
    public List<CommonDto.ColorCodeDto> getColor() {
        return klassService.getKlassColor();
    }

    @ApiOperation(value = "강사 리스트 조회", notes = "클래스의 담당 강사 선택을 위한 강사 리스트를 조회합니다.")
    @GetMapping("tutor")
    public BranchUserDto.ResponseBranchTutorListDto getTutorList(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "페이지 번호", required = true, defaultValue = "0") Integer page,
            @ApiParam(value = "페이지당 항목 수", required = true, defaultValue = "10") Integer maxResults){

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        Pageable pageable = PageRequest.of(page, maxResults);
        Page<BranchUserDto.ResponseBranchTutorDto> branchTutorDtoPage = klassService.getTutorList(branchId, pageable);
        return BranchUserDto.ResponseBranchTutorListDto.build(branchTutorDtoPage, page, maxResults);
    }

    @ApiOperation(value = "수강권 리스트 조회", notes = "클래스를 포함하는 수강권 선택을 위한 수강권 리스트를 조회합니다.")
    @GetMapping("product")
    public ProductDto.ResponseProductListDto getProductList(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "페이지 번호", required = true, defaultValue = "0") Integer page,
            @ApiParam(value = "페이지당 항목 수", required = true, defaultValue = "10") Integer maxResults){

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        Pageable pageable = PageRequest.of(page, maxResults);
        Page<ProductDto.ResponseProductDto> productDtoPage = klassService.getProductList(branchId, pageable);
        return ProductDto.ResponseProductListDto.build(productDtoPage, page, maxResults);
    }

    @ApiOperation(value = "클래스에 수강권 추가")
    @PostMapping("{klassId}/product")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<List<ProductDto.ResponseProductDto>> createKlassProduct(
            @ApiParam(value = "클래스 id", required = true, defaultValue = "1") @PathVariable Long klassId,
            @RequestBody @Valid List<Long> requestProductIdList){

        List<ProductDto.ResponseProductDto> productInfoList = klassService.createKlassProduct(klassId, requestProductIdList);
        return ApiResponseDto.createdResponseEntity(ServletUriComponentsBuilder.fromCurrentRequest().toUriString(), productInfoList);
    }

    @ApiOperation(value = "클래스 보관함 이동")
    @PatchMapping("{klassId}/archive")
    public KlassDto.ResponseKlassDto archive(
            @ApiParam(value = "클래스 id", required = true, defaultValue = "1") @PathVariable Long klassId){

        return klassService.archive(klassId);
    }

    @ApiOperation(value = "자유 예약형 클래스 수정")
    @PatchMapping("free/{klassId}")
    public KlassDto.ResponseKlassDto updateFreeKlass(
            @ApiParam(value = "클래스 id", required = true, defaultValue = "1") @PathVariable Long klassId,
            @RequestBody @Valid KlassDto.RequestFreeKlassDto requestKlassDto){

        return klassService.updateFreeKlass(klassId, requestKlassDto);
    }

    @ApiOperation(value = "자유 예약형 클래스 설정 수정")
    @PatchMapping("free/{klassId}/setting")
    public KlassDto.ResponseKlassDto updateFreeKlassSetting(
            @ApiParam(value = "클래스 id", required = true, defaultValue = "1") @PathVariable Long klassId,
            @RequestBody @Valid KlassDto.RequestFreeKlassSettingDto requestKlassSettingDto){

        return klassService.updateFreeKlassSetting(klassId, requestKlassSettingDto);
    }

    @ApiOperation(value = "사전 개설형 클래스 수정")
    @PatchMapping("limited/{klassId}")
    public KlassDto.ResponseKlassDto updateLimitedKlass(
            @ApiParam(value = "클래스 id", required = true, defaultValue = "1") @PathVariable Long klassId,
            @RequestBody @Valid KlassDto.RequestLimitedKlassDto requestKlassDto){

        return klassService.updateLimitedKlass(klassId, requestKlassDto);
    }

    @ApiOperation(value = "사전 개설형 클래스 설정 수정")
    @PatchMapping("limited/{klassId}/setting")
    public KlassDto.ResponseKlassDto updateLimitedKlassSetting(
            @ApiParam(value = "클래스 id", required = true, defaultValue = "1") @PathVariable Long klassId,
            @RequestBody @Valid KlassDto.RequestLimitedKlassSettingDto requestKlassSettingDto){

        return klassService.updateLimitedKlassSetting(klassId, requestKlassSettingDto);
    }

    @ApiOperation(value = "클래스 진행중 회원 조회")
    @GetMapping("{klassId}/tutee")
    public BranchTuteeDto.ResponseBranchTuteeListDto getKlassTutee(
            @ApiParam(value = "클래스 id", required = true, defaultValue = "1") @PathVariable Long klassId,
            @ApiParam(value = "페이지 번호", required = true, defaultValue = "0") Integer page,
            @ApiParam(value = "페이지 당 항목 수", required = true, defaultValue = "10") Integer maxResults){

        Pageable pageable = PageRequest.of(page, maxResults);
        Page<BranchTuteeDto.ResponseBranchTuteeDto> branchTuteeDtoPage = klassService.getKlassTutee(klassId, pageable);
        return BranchTuteeDto.ResponseBranchTuteeListDto.build(branchTuteeDtoPage, page, maxResults);
    }

    @ApiOperation(value = "클래스 반복 일정 조회")
    @GetMapping("{klassId}/repeat-schedule")
    public RepeatScheduleDto.ResponseRepeatScheduleListDto getKlassRepeatSchedule(
            @ApiParam(value = "클래스 id", required = true, defaultValue = "1") @PathVariable Long klassId,
            @ApiParam(value = "페이지 번호", required = true, defaultValue = "0") Integer page,
            @ApiParam(value = "페이지당 항목 수", required = true, defaultValue = "10") Integer maxResults){

        Pageable pageable = PageRequest.of(page, maxResults);
        Page<RepeatScheduleDto.ResponseRepeatScheduleDto> repeatScheduleDtoPage = klassService.getKlassRepeatSchedule(klassId, pageable);
        return RepeatScheduleDto.ResponseRepeatScheduleListDto.build(repeatScheduleDtoPage, page, maxResults);
    }

    @ApiOperation(value = "클래스 일정 조회")
    @GetMapping("{klassId}/schedule")
    public ScheduleDto.ResponseSchedulePagingListDto getKlassSchedule(
            @ApiParam(value = "클래스 id", required = true, defaultValue = "1") @PathVariable Long klassId,
            @ApiParam(value = "페이지 번호", required = true, defaultValue = "0") Integer page,
            @ApiParam(value = "페이지당 항목 수", required = true, defaultValue = "10") Integer maxResults){

        Pageable pageable = PageRequest.of(page, maxResults);
        Page<ScheduleDto.ResponseScheduleDto> scheduleDtoPage = klassService.getKlassSchedule(klassId, pageable);
        return ScheduleDto.ResponseSchedulePagingListDto.build(scheduleDtoPage, page, maxResults);
    }

    @ApiOperation(value = "클래스 삭제")
    @DeleteMapping("{klassId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(
            @ApiParam(value = "클래스 id", required = true, defaultValue = "1") @PathVariable Long klassId){

        klassService.delete(klassId);
        return ResponseEntity.noContent().build();
    }



}
