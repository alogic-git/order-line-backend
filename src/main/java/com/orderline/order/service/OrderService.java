package com.orderline.order.service;

import com.orderline.basic.exception.NotFoundException;
import com.orderline.material.model.dto.MaterialDto;
import com.orderline.material.model.entity.Material;
import com.orderline.order.model.dto.OrderDto;
import com.orderline.order.model.entity.Order;
import com.orderline.order.model.entity.OrderHistory;
import com.orderline.order.model.entity.OrderMaterial;
import com.orderline.order.repository.OrderHistoryRepository;
import com.orderline.order.repository.OrderMaterialRepository;
import com.orderline.order.repository.OrderRepository;
import com.orderline.site.model.entity.Site;
import com.orderline.site.repository.SiteRepository;
import com.orderline.user.model.entity.User;
import com.orderline.user.model.entity.UserSite;
import com.orderline.user.repository.UserRepository;
import com.orderline.user.repository.UserSiteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Resource(name = "userRepository")
    UserRepository userRepository;

    @Resource(name = "siteRepository")
    SiteRepository siteRepository;
    @Resource(name = "userSiteRepository")
    UserSiteRepository userSiteRepository;

    @Resource(name = "orderRepository")
    OrderRepository orderRepository;

    @Resource(name = "orderMaterialRepository")
    OrderMaterialRepository orderMaterialRepository;

    @Resource(name = "orderHistoryRepository")
    OrderHistoryRepository orderHistoryRepository;

    @Transactional
    public OrderDto.ResponseOrderDto createOrder(Long userId, Long siteId, OrderDto.RequestCreateOrderDto requestCreateOrderDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> new NotFoundException("현장을 찾을 수 없습니다."));

        userSiteRepository.findByUserAndSite(user, site).orElseThrow(() -> new NotFoundException("사용자 현장을 찾을 수 없습니다."));

        Order order = requestCreateOrderDto.toEntity(site);

        orderRepository.save(order);

        return OrderDto.ResponseOrderDto.toDto(order);
    }

    public Page<OrderDto.ResponseOrderDto> getOrderList(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        List<Site> siteList = userSiteRepository.findByUserId(user.getId())
                .stream()
                .map(UserSite::getSite)
                .collect(Collectors.toList());

        List<Order> orderList = orderRepository.findBySiteIn(siteList);

        List<OrderDto.ResponseOrderDto> orderDtoList = orderList.stream()
                .map(OrderDto.ResponseOrderDto::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(orderDtoList, pageable, orderDtoList.size());
    }

    public Page<MaterialDto.ResponseMaterialDto> getOrderMaterials(Long userId, Long orderId, Pageable pageable) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("해당 발주를 찾을 수 없습니다."));

        List<Material> materialList = orderMaterialRepository.findByOrder(order)
                .stream()
                .map(OrderMaterial::getMaterial)
                .collect(Collectors.toList());

        List<MaterialDto.ResponseMaterialDto> materialDtoList = materialList.stream()
                .map(material -> MaterialDto.ResponseMaterialDto.toDto(material, material.getProduct()))
                .collect(Collectors.toList());

        return new PageImpl<>(materialDtoList, pageable, materialDtoList.size());
    }

    @Transactional
    public OrderDto.ResponseOrderDto updateOrder(Long userId, Long orderId, OrderDto.RequestUpdateOrderDto requestOrderDto) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("발주를 찾을 수 없습니다."));

        createOrderHistory(order);

        Site site = siteRepository.findById(requestOrderDto.getSiteId())
                .orElseThrow(() -> new NotFoundException("현장을 찾을 수 없습니다."));

        order.updateOrder(requestOrderDto, site);
        ZonedDateTime expectedDt = calculateExpectedDt(order);
        order.updateExpectedDt(expectedDt);

        return OrderDto.ResponseOrderDto.toDto(order, expectedDt);
    }

    public OrderDto.ResponseOrderDto getOrderDetail(Long userId, Long orderId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("해당 발주를 찾을 수 없습니다."));
        return OrderDto.ResponseOrderDto.toDto(order);
    }

    public ZonedDateTime calculateExpectedDt(Order order) {
        List<OrderMaterial> orderMaterialList = orderMaterialRepository.findByOrder(order);

        ZonedDateTime expectedDt = order.getRequestDt();

        for (OrderMaterial orderMaterial : orderMaterialList) {
            ZonedDateTime tmpTime = orderMaterial.getMaterial().getExpectedDt();
            if (tmpTime.isAfter(expectedDt)) {
                expectedDt = tmpTime;
            }
        }
        return expectedDt;
    }

    public void createOrderHistory(Order order) {
        OrderHistory orderHistory = OrderHistory.builder()
                .order(order)
                .address(order.getAddress())
                .specifics(order.getSpecifics())
                .managerName(order.getManagerName())
                .emergencyYn(order.getEmergencyYn())
                .status(order.getStatus())
                .requestDt(order.getRequestDt())
                .orderDt(order.getOrderDt())
                .expectedDt(order.getExpectedDt())
                .build();

        orderHistoryRepository.save(orderHistory);
    }
}
