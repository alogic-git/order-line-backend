package com.orderline.material.service;

import com.orderline.basic.exception.NotFoundException;
import com.orderline.material.model.dto.MaterialDto;
import com.orderline.material.model.dto.ProductDto;
import com.orderline.material.model.entity.Material;
import com.orderline.material.model.entity.MaterialHistory;
import com.orderline.material.model.entity.Product;
import com.orderline.material.repository.MaterialHistoryRepository;
import com.orderline.material.repository.MaterialRepository;
import com.orderline.material.repository.ProductRepository;
import com.orderline.order.model.entity.Order;
import com.orderline.order.model.entity.OrderMaterial;
import com.orderline.order.repository.OrderMaterialRepository;
import com.orderline.order.repository.OrderRepository;
import com.orderline.user.model.entity.User;
import com.orderline.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialService {
    @Resource(name = "userRepository")
    UserRepository userRepository;

    @Resource(name = "materialRepository")
    MaterialRepository materialRepository;

    @Resource(name = "orderMaterialRepository")
    OrderMaterialRepository orderMaterialRepository;

    @Resource(name = "orderRepository")
    OrderRepository orderRepository;

    @Resource(name = "productRepository")
    ProductRepository productRepository;

    @Resource(name = "materialHistoryRepository")
    MaterialHistoryRepository materialHistoryRepository;

    @Transactional
    public ProductDto.ResponseProductDto createProduct(Long userId, ProductDto.RequestCreateProductDto requestCreateProductDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        if(Boolean.FALSE.equals(user.getAdminYn())) {
            throw new NotFoundException("관리자만 자재를 등록할 수 있습니다.");
        }

        Product product = requestCreateProductDto.toEntity();

        productRepository.save(product);

        return ProductDto.ResponseProductDto.toDto(product);
    }

    @Transactional
    public MaterialDto.ResponseMaterialDto createMaterial(Long userId, MaterialDto.RequestCreateMaterialDto requestCreateMaterialDto) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        Order order = orderRepository.findById(requestCreateMaterialDto.getOrderId())
                .orElseThrow(() -> new NotFoundException("발주를 찾을 수 없습니다."));

        Product product = productRepository.findById(requestCreateMaterialDto.getProductId())
                .orElseThrow(() -> new NotFoundException("자재를 찾을 수 없습니다."));

        Material material = requestCreateMaterialDto.toEntity(product);

        OrderMaterial orderMaterial = OrderMaterial.builder()
                .order(order)
                .material(material)
                .build();

        materialRepository.save(material);
        orderMaterialRepository.save(orderMaterial);

        return MaterialDto.ResponseMaterialDto.toDto(material, product);
    }

    public Page<ProductDto.ResponseProductDto> getAllProductList(Long userId, Pageable pageable) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        List<Product> productList = productRepository.findAll();

        List<ProductDto.ResponseProductDto> productDtoList = productList.stream()
                .map(ProductDto.ResponseProductDto::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(productDtoList, pageable, productDtoList.size());
    }

    public void createMaterialHistory(Material material) {
        MaterialHistory materialHistory = MaterialHistory.builder()
                .materialId(material.getId())
                .name(material.getName())
                .totalPrice(material.getTotalPrice())
                .quantity(material.getQuantity())
                .specifics(material.getSpecifics())
                .status(material.getStatus())
                .expectedDt(material.getExpectedDt())
                .requestDt(material.getRequestDt())
                .build();

        materialHistoryRepository.save(materialHistory);
    }

    @Transactional
    public void deleteMaterial(Long materialId) {
        Material material = materialRepository.findById(materialId).orElseThrow(() -> new NotFoundException("자재를 찾을 수 없습니다."));

        createMaterialHistory(material);

        List<OrderMaterial> orderMaterialList = orderMaterialRepository.findByMaterial(material);
        orderMaterialList.forEach(OrderMaterial::deleteOrderMaterial);
        orderMaterialRepository.saveAll(orderMaterialList);

        material.deleteMaterial();
        materialRepository.save(material);
    }

    public void deleteProduct(Long userId, Long productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("자재를 찾을 수 없습니다."));

        if(Boolean.FALSE.equals(user.getAdminYn())) {
            throw new NotFoundException("관리자만 자재를 삭제할 수 있습니다.");
        }

        product.deleteProduct();
        productRepository.save(product);
    }


//    public Page<ProductDto.ResponseProductDto> getSelectedProductList(Long userId, Pageable pageable) {
//        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
//
//        List<Product> productList = productRepository.findAll();
//
//        List<ProductDto.ResponseProductDto> productDtoList = productList.stream()
//                .map(ProductDto.ResponseProductDto::toDto)
//                .collect(Collectors.toList());
//
//        return new PageImpl<>(productDtoList, pageable, productDtoList.size());
//    }
}
