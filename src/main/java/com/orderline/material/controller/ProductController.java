package com.orderline.material.controller;

import com.orderline.basic.model.dto.ApiResponseDto;
import com.orderline.material.model.dto.ProductDto;
import com.orderline.material.service.MaterialService;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.orderline.basic.utils.Constants.*;

@Api(tags={"30.Product"})
@RestController
@RequestMapping(path = {""}, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

    @Resource(name = "materialService")
    MaterialService materialService;

    @ApiOperation(value = "자재 전체 목록 조회", notes = "자재 목록을 조회합니다.")
    @GetMapping({"admin/product", "user/product"})
    public ProductDto.ResponseProductListDto getProductList(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "페이지 번호", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer pageNum,
            @ApiParam(value = "페이지당 항목 수", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize) {

        Long userId = (Long) httpServletRequest.getAttribute("userId");
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<ProductDto.ResponseProductDto> productDtoPage = materialService.getAllProductList(userId, pageable);

        return ProductDto.ResponseProductListDto.build(productDtoPage, pageNum, pageSize);
    }

    @ApiOperation(value = "새로운 자재 등록", notes = "새로운 자재를 자재 목록에 등록합니다.")
    @PostMapping("admin/product")
    public ResponseEntity<ProductDto.ResponseProductDto> createProduct(HttpServletRequest httpServletRequest, @RequestBody ProductDto.RequestCreateProductDto productDto) {
        Long userId = (Long) httpServletRequest.getAttribute("userId");

        ProductDto.ResponseProductDto responseCreateProductDto = materialService.createProduct(userId, productDto);
        String uri = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
        String createUri = uri + "/" + responseCreateProductDto.getId();

        return ApiResponseDto.createdResponseEntity(createUri, responseCreateProductDto);
    }

    @ApiOperation(value = "자재 등록 내역 수정")
    @PatchMapping("admin/product/{productId}")
    public ProductDto.ResponseProductDto updateProduct(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "자재 id", required = true, defaultValue = DEFAULT_ID) @PathVariable Long productId,
            @RequestBody ProductDto.RequestCreateProductDto requestProductDto) {
        Long userId = (Long) httpServletRequest.getAttribute("userId");

        return materialService.updateProduct(userId, productId, requestProductDto);
    }

    @ApiOperation(value = "자재 등록 내역 삭제")
    @DeleteMapping("admin/product/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteProduct(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "자재 id", required = true, defaultValue = DEFAULT_ID) @PathVariable Long productId) {
        Long userId = (Long) httpServletRequest.getAttribute("userId");

        materialService.deleteProduct(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "자재 상세 조회")
    @GetMapping({"admin/product/{productId}", "user/product/{productId}"})
    public ProductDto.ResponseProductDto getProductDetail(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "자재 id", required = true, defaultValue = DEFAULT_ID) @PathVariable Long productId) {
        Long userId = (Long) httpServletRequest.getAttribute("userId");

        return materialService.getProductDetail(userId, productId);
    }

}
