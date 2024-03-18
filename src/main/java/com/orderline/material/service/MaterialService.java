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
import com.orderline.order.repository.OrderRepository;
import com.orderline.user.repository.UserRepository;
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
public class MaterialService {
    @Resource(name = "userRepository")
    UserRepository userRepository;

    @Resource(name = "materialRepository")
    MaterialRepository materialRepository;

    @Resource(name = "orderRepository")
    OrderRepository orderRepository;

    @Resource(name = "productRepository")
    ProductRepository productRepository;

    @Resource(name = "materialHistoryRepository")
    MaterialHistoryRepository materialHistoryRepository;

    @Transactional
    public ProductDto.ResponseProductDto createProduct(Long userId, ProductDto.RequestCreateProductDto requestCreateProductDto) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

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

        Material material = requestCreateMaterialDto.toEntity(order, product);

        ZonedDateTime expectedDt = calculateExpectedDt(order);
        order.updateExpectedDt(expectedDt);

        int totalPrice = calculateTotalPrice(order);
        order.updateTotalPrice(totalPrice);

        materialRepository.save(material);

        createMaterialHistory(material);

        return MaterialDto.ResponseMaterialDto.toDto(material, product);
    }

    @Transactional
    public MaterialDto.ResponseMaterialDto updateMaterial(Long userId, Long materialId, MaterialDto.RequestUpdateMaterialDto requestMaterialDto) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        Material material = materialRepository.findById(materialId).orElseThrow(() -> new NotFoundException("자재를 찾을 수 없습니다."));
        Product product = productRepository.findById(material.getProduct().getId()).orElseThrow(() -> new NotFoundException("자재를 찾을 수 없습니다."));

        material.updateMaterial(requestMaterialDto, product);

        createMaterialHistory(material);

        return MaterialDto.ResponseMaterialDto.toDto(material, product);
    }

    @Transactional
    public ProductDto.ResponseProductDto updateProduct(Long userId, Long productId, ProductDto.RequestCreateProductDto requestProductDto) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("자재를 찾을 수 없습니다."));

        product.updateProduct(requestProductDto);

        return ProductDto.ResponseProductDto.toDto(product);
    }


    public Page<ProductDto.ResponseProductDto> getAllProductList(Long userId, Pageable pageable) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        List<Product> productList = productRepository.findAll();

        List<ProductDto.ResponseProductDto> productDtoList = productList.stream()
                .map(ProductDto.ResponseProductDto::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(productDtoList, pageable, productDtoList.size());
    }

    public ProductDto.ResponseProductDto getProductDetail(Long userId, Long productId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("자재를 찾을 수 없습니다."));

        return ProductDto.ResponseProductDto.toDto(product);
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
    public void deleteMaterial(Long userId, Long materialId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        Material material = materialRepository.findById(materialId).orElseThrow(() -> new NotFoundException("자재를 찾을 수 없습니다."));

        material.deleteMaterial();
        createMaterialHistory(material);
    }

    public Page<MaterialDto.ResponseMaterialHistoryDto> getMaterialHistoryList(Long userId, Long materialId, Pageable pageable) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        Material material = materialRepository.findById(materialId).orElseThrow(() -> new NotFoundException("자재를 찾을 수 없습니다."));

        List<MaterialHistory> materialHistoryList = materialHistoryRepository.findByMaterialId(material.getId());

        List<MaterialDto.ResponseMaterialHistoryDto> materialHistoryDtoList = materialHistoryList.stream()
                .map(MaterialDto.ResponseMaterialHistoryDto::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(materialHistoryDtoList, pageable, materialHistoryDtoList.size());

    }


    public void deleteProduct(Long userId, Long productId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("자재를 찾을 수 없습니다."));

        product.deleteProduct();
        productRepository.save(product);
    }

    public int calculateTotalPrice(Order order) {
        List<Material> materialList = materialRepository.findByOrder(order);

        int totalPrice = 0;

        for (Material material : materialList) {
            totalPrice += material.getTotalPrice();
        }

        return totalPrice;
    }

    public ZonedDateTime calculateExpectedDt(Order order) {
        List<Material> materialList = materialRepository.findByOrder(order);

        ZonedDateTime expectedDt = order.getRequestDt();

        for (Material material : materialList) {
            ZonedDateTime tmpTime = material.getExpectedDt();
            if (tmpTime.isAfter(expectedDt)) {
                expectedDt = tmpTime;
            }
        }
        return expectedDt;
    }
}
