package com.orderline.order.service;

import com.orderline.basic.exception.NotFoundException;
import com.orderline.order.model.dto.OrderDto;
import com.orderline.order.model.entity.Order;
import com.orderline.order.repository.OrderRepository;
import com.orderline.site.model.entity.Site;
import com.orderline.site.model.entity.SiteOrder;
import com.orderline.site.repository.SiteOrderRepository;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Resource(name = "userRepository")
    UserRepository userRepository;

    @Resource(name = "siteRepository")
    SiteRepository siteRepository;

    @Resource(name = "siteOrderRepository")
    SiteOrderRepository siteOrderRepository;

    @Resource(name = "userSiteRepository")
    UserSiteRepository userSiteRepository;

    @Resource(name = "orderRepository")
    OrderRepository orderRepository;

    @Transactional
    public OrderDto.ResponseOrderDto createOrder(Long userId, OrderDto.RequestCreateOrderDto requestCreateOrderDto) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        Site site = siteRepository.findById(requestCreateOrderDto.getSiteId())
                .orElseThrow(() -> new NotFoundException("현장을 찾을 수 없습니다."));

        Order order = requestCreateOrderDto.toEntity();

        SiteOrder siteOrder = SiteOrder.builder()
                .site(site)
                .order(order)
                .build();

        orderRepository.save(order);
        siteOrderRepository.save(siteOrder);

        return OrderDto.ResponseOrderDto.toDto(order, site);
    }


    public Page<OrderDto.ResponseOrderDto> getOrderList(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        List<Site> siteList = userSiteRepository.findByUserId(user.getId())
                .stream()
                .map(UserSite::getSite)
                .collect(Collectors.toList());

        List<SiteOrder> siteOrderList = siteOrderRepository.findBySiteIn(siteList);

        List<OrderDto.ResponseOrderDto> orderDtoList = siteOrderList.stream()
                .map(siteOrder -> OrderDto.ResponseOrderDto.toDto(siteOrder.getOrder(), siteOrder.getSite()))
                .collect(Collectors.toList());

        return new PageImpl<>(orderDtoList, pageable, orderDtoList.size());
    }
}
