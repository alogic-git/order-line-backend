package com.ptglue.product.controller;

import com.ptglue.basic.enums.order.KlassOrderEnum;
import com.ptglue.basic.enums.order.ProductOrderEnum;
import com.ptglue.branch.model.dto.BranchDto;
import com.ptglue.branch.model.dto.BranchTagDto;
import com.ptglue.branch.model.dto.BranchTuteeDto;
import com.ptglue.product.model.dto.ProductDto;
import com.ptglue.product.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.ptglue.basic.model.dto.ApiResponseDto;
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

@Api(tags = {"20.Product"})
@RestController
@RequestMapping(path = {"manager/product", "tutor/product"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

    @Resource(name = "productService")
    ProductService productService;
    
    @ApiOperation(value = "수강권 상세 조회", notes = "선택한 수강권을 상세 조회합니다.")
    @GetMapping("{productId}")
    public ProductDto.ResponseProductDto get(
            @ApiParam(value = "수강권 id", required = true, defaultValue = "1") @PathVariable Long productId){

        return productService.get(productId);
    }

    @ApiOperation(value = "수강권의 포함 클래스 조회", notes = "선택한 수강권의 포함 클래스를 조회합니다. \n" +
            "지점 id를 입력하지 않으면 수강권에 포함된 모든 클래스를 조회합니다.")
    @GetMapping("{productId}/klass")
    public KlassDto.ResponseKlassListDto getProductKlass(
            @ApiParam(value = "수강권 id", required = true, defaultValue = "1") @PathVariable Long productId,
            @ApiParam(value = "지점 id") @RequestParam(required = false, defaultValue = "0") Long branchId,
            @ApiParam(value = "페이지 번호", required = true, defaultValue = "0") Integer page,
            @ApiParam(value = "페이지당 항목 수", required = true, defaultValue = "10") Integer maxResults){

        Pageable pageable = PageRequest.of(page, maxResults);
        Page<KlassDto.ResponseKlassDto> klassList = productService.getProductKlass(productId, branchId, pageable);
        return KlassDto.ResponseKlassListDto.build(klassList, page, maxResults);
    }

    @ApiOperation(value = "수강권에 포함된 클래스가 속한 지점 리스트 조회")
    @GetMapping("{productId}/branch")
    public List<BranchDto.ResponseBranchDto> getProductBranch(
            @ApiParam(value = "수강권 id", required = true, defaultValue = "1") @PathVariable Long productId){

        return productService.getProductBranch(productId);
    }

    @ApiOperation(value = "수강권 태그 조회", notes = "수강권 태그를 조회합니다.")
    @GetMapping("tag")
    public List<BranchTagDto.ResponseBranchTagDto> getProductTag(
            HttpServletRequest httpServletRequest){

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        return productService.getTagList(branchId);
    }

    @ApiOperation(value = "수강권 리스트 조회", notes = "token의 branch Id에 해당하는 수강권 리스트를 클래스 포함하여 조회합니다.")
    @GetMapping("")
    public ProductDto.ResponseProductListDto getList(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "태그") @RequestParam(required = false) List<Long> branchTagIdList,
            @ApiParam(value = "검색할 단어", defaultValue = "%") @RequestParam(required = false, defaultValue = "%") String searchWord,
            @ApiParam(value = "정렬", defaultValue = "PRODUCT_NAME_ASC") @RequestParam(required = false, defaultValue = "PRODUCT_NAME_ASC") ProductOrderEnum orderBy,
            @ApiParam(value = "페이지 번호", required = true, defaultValue = "0") @RequestParam Integer page,
            @ApiParam(value = "페이지당 항목 수", required = true, defaultValue = "10") @RequestParam Integer maxResults){

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
        Page<ProductDto.ResponseProductDto> products = productService.getList(branchId, branchTagIdList, searchWord, pageable);
        return ProductDto.ResponseProductListDto.build(products, page, maxResults);
    }

    @ApiOperation(value = "수강권 보관함 이동")
    @PatchMapping("{productId}/archive")
    public ProductDto.ResponseProductDto archive(
            @ApiParam(value = "수강권 id", required = true, defaultValue = "1") @PathVariable Long productId){

        return productService.archive(productId);
    }

    @ApiOperation(value = "수강권 추가", notes = "token의 branch Id에 해당하는 수강권을 추가합니다.")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductDto.ResponseProductDto> create(
            HttpServletRequest httpServletRequest,
            @RequestBody @Valid ProductDto.RequestProductDto requestProductDto){

        Long branchId = (Long) httpServletRequest.getAttribute("branchId");
        ProductDto.ResponseProductDto productInfo = productService.createProduct(branchId, requestProductDto);
        List<Long> branchTagIdList = productService.createBranchTag(branchId, requestProductDto.getTagList());
        List<BranchTagDto.ResponseBranchTagDto> productTagList = productService.createProductTag(productInfo.getProductId(), branchTagIdList);
        productInfo.updateTagList(productTagList);
        return ApiResponseDto.createdResponseEntity(productInfo.getProductId(), productInfo);
    }

    @ApiOperation(value = "수강권 클래스 추가")
    @PostMapping("{productId}/klass")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<List<KlassDto.ResponseKlassDto>> createProductKlass(
            @ApiParam(value = "수강권 id", required = true, defaultValue = "1") @PathVariable Long productId,
            @RequestBody @Valid List<Long> requsetklassIdList){

        List<KlassDto.ResponseKlassDto> klassInfoList = productService.createProductKlass(productId, requsetklassIdList);
        return ApiResponseDto.createdResponseEntity(ServletUriComponentsBuilder.fromCurrentRequest().toUriString(), klassInfoList);
    }

    @ApiOperation(value = "수강권 수정", notes = "수강권 정보와 수강권 태그를 수정합니다.")
    @PatchMapping("{productId}")
    public ProductDto.ResponseProductDto update(
            @ApiParam(value = "수강권 id", required = true, defaultValue = "1") @PathVariable Long productId,
            @RequestBody @Valid ProductDto.RequestProductDto requestProductDto){

        return productService.updateProductAndTag(productId, requestProductDto);
    }

    @ApiOperation(value = "수강권 클래스 수정")
    @PatchMapping("{productId}/klass")
    public List<KlassDto.ResponseKlassDto> updateProductKlass(
            @ApiParam(value = "수강권 id", required = true, defaultValue = "1") @PathVariable Long productId,
            @RequestBody @Valid List<Long> requestKlassIdList){

        return productService.updateProductKlass(productId, requestKlassIdList);
    }

    @ApiOperation(value = "수강권 보유 회원 조회")
    @GetMapping("{productId}/tutee")
    public BranchTuteeDto.ResponseBranchTuteeListDto getProductTutee(
            @ApiParam(value = "수강권 id", required = true, defaultValue = "1") @PathVariable Long productId,
            @ApiParam(value = "페이지 번호", required = true, defaultValue = "0") Integer page,
            @ApiParam(value = "페이지 당 항목 수", required = true, defaultValue = "10") Integer maxResults){

        Pageable pageable = PageRequest.of(page, maxResults);
        Page<BranchTuteeDto.ResponseBranchTuteeDto> branchTuteeDtoPage = productService.getProductTutee(productId, pageable);
        return BranchTuteeDto.ResponseBranchTuteeListDto.build(branchTuteeDtoPage, page, maxResults);
    }
}
