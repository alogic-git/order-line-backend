package com.orderline.order.controller;

import com.orderline.basic.model.dto.ApiResponseDto;
import com.orderline.material.model.dto.MaterialDto;
import com.orderline.order.model.dto.OrderDto;
import com.orderline.order.service.OrderService;
import io.swagger.annotations.Api;
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

import static com.orderline.basic.utils.Constants.*;

@Api(tags={"10.Order"})
@RestController
@RequestMapping(path = {"admin/order", "user/order"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

    @Resource(name = "orderService")
    OrderService orderService;

    @ApiOperation(value = "새로운 발주 생성", notes = "새로운 발주를 생성합니다.")
    @PostMapping
    public ResponseEntity<OrderDto.ResponseOrderDto> createOrder(HttpServletRequest httpServletRequest, @RequestBody OrderDto.RequestCreateOrderDto orderDto) {
        Long userId = (Long) httpServletRequest.getAttribute("userId");
        Long siteId = (Long) httpServletRequest.getAttribute("siteId");

        OrderDto.ResponseOrderDto responseCreateOrderDto = orderService.createOrder(userId, siteId, orderDto);
        String uri = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
        String createUri = uri + "/" + responseCreateOrderDto.getId();

        return ApiResponseDto.createdResponseEntity(createUri, responseCreateOrderDto);
    }

    @ApiOperation(value = "발주 목록 조회", notes = "발주 목록을 조회합니다.")
    @GetMapping("/orders")
    public OrderDto.ResponseOrderListDto getOrderList(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "페이지 번호", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer pageNum,
            @ApiParam(value = "페이지당 항목 수", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize) {

        Long userId = (Long) httpServletRequest.getAttribute("userId");
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<OrderDto.ResponseOrderDto> orderDtoPage = orderService.getOrderList(userId, pageable);

        return OrderDto.ResponseOrderListDto.build(orderDtoPage, pageNum, pageSize);
    }

    @ApiOperation(value = "발주 상세 조회", notes = "발주 상세를 조회합니다.")
    @GetMapping("/{orderId}")
    public OrderDto.ResponseOrderDto getOrderDetail(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "발주 ID", required = true, defaultValue = DEFAULT_ID) @PathVariable Long orderId) {

        Long userId = (Long) httpServletRequest.getAttribute("userId");

        return orderService.getOrderDetail(userId, orderId);
    }

    @ApiOperation(value = "발주 수정", notes = "발주를 수정합니다.")
    @PatchMapping("/{orderId}")
    public OrderDto.ResponseOrderDto updateOrder(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "발주 ID", required = true, defaultValue = DEFAULT_ID) @PathVariable Long orderId,
            @RequestBody OrderDto.RequestUpdateOrderDto requestOrderDto) {

        Long userId = (Long) httpServletRequest.getAttribute("userId");
        return orderService.updateOrder(userId, orderId, requestOrderDto);
    }

    @ApiOperation(value = "발주 자재 내역 조회", notes = "발주 자재 내역을 조회합니다.")
    @GetMapping("/{orderId}/materials")
    public MaterialDto.ResponseMaterialListDto getOrderMaterials(
            HttpServletRequest httpServletRequest,
            @ApiParam(value = "발주 ID", required = true, defaultValue = DEFAULT_ID) @PathVariable Long orderId,
            @ApiParam(value = "페이지 번호", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer pageNum,
            @ApiParam(value = "페이지당 항목 수", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize) {

        Long userId = (Long) httpServletRequest.getAttribute("userId");
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<MaterialDto.ResponseMaterialDto> materialDtoPage = orderService.getOrderMaterials(userId, orderId, pageable);

        return MaterialDto.ResponseMaterialListDto.build(materialDtoPage, pageNum, pageSize);
    }
}
