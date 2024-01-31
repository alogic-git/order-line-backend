package com.ptglue.product.service;

import com.ptglue.basic.exception.DuplicateException;
import com.ptglue.branch.model.dto.BranchTuteeDto;
import com.ptglue.branch.model.entity.BranchUserOngoingListGroupedProduct;
import com.ptglue.branch.repository.BranchUserOngoingListGroupedProductRepository;
import com.ptglue.product.repository.ProductKlassRepository;
import com.ptglue.product.repository.ProductRepository;
import com.ptglue.product.repository.ProductTagRepository;
import com.ptglue.basic.exception.NotFoundException;
import com.ptglue.branch.model.dto.BranchDto;
import com.ptglue.branch.model.dto.BranchTagDto;
import com.ptglue.branch.model.entity.Branch;
import com.ptglue.branch.model.entity.BranchTag;
import com.ptglue.branch.repository.BranchRepository;
import com.ptglue.branch.repository.BranchTagRepository;
import com.ptglue.klass.model.dto.KlassDto;
import com.ptglue.klass.model.entity.Klass;
import com.ptglue.klass.repository.KlassRepository;
import com.ptglue.product.model.dto.ProductDto;
import com.ptglue.product.model.entity.Product;
import com.ptglue.product.model.entity.ProductKlass;
import com.ptglue.product.model.entity.ProductTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Resource(name = "productRepository")
    ProductRepository productRepository;

    @Resource(name = "branchRepository")
    BranchRepository branchRepository;

    @Resource(name = "branchTagRepository")
    BranchTagRepository branchTagRepository;

    @Resource(name = "productTagRepository")
    ProductTagRepository productTagRepository;

    @Resource(name = "productKlassRepository")
    ProductKlassRepository productKlassRepository;

    @Resource(name = "klassRepository")
    KlassRepository klassRepository;

    @Resource(name = "branchUserOngoingListGroupedProductRepository")
    BranchUserOngoingListGroupedProductRepository branchUserOngoingListGroupedProductRepository;

    public ProductDto.ResponseProductDto get(Long productId){

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 수강권이 존재하지 않습니다."));

        List<ProductTag> productTagList = productTagRepository.findByProductId(product.getId());
        return ProductDto.ResponseProductDto.toDto(product, productTagList);
    }

    public Page<KlassDto.ResponseKlassDto> getProductKlass(Long productId, Long branchId, Pageable pageable){

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 수강권이 존재하지 않습니다."));

        Page<ProductKlass> productKlassList;
        if(branchId == 0L) {
           productKlassList = productKlassRepository.findByProductIdAndKlass_ArchiveYn(product.getId(), false, pageable);
        }else {
           productKlassList = productKlassRepository.findByProductIdAndKlass_ArchiveYnAndKlass_BranchIdAndKlass_Branch_ArchiveYn(product.getId(), false, branchId, false, pageable);
        }
        return productKlassList.map(productKlass -> KlassDto.ResponseKlassDto.toDto(productKlass.getKlass()));
    }

    public List<BranchDto.ResponseBranchDto> getProductBranch(Long productId){

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 수강권이 존재하지 않습니다."));

        List<ProductKlass> productKlassList = productKlassRepository.findByProductIdAndKlass_ArchiveYnAndKlass_Branch_ArchiveYn(product.getId(), false, false);
        return productKlassList.stream().map(productKlass -> productKlass.getKlass().getBranch()).distinct().map(BranchDto.ResponseBranchDto::toDto).collect(Collectors.toList());
    }

    public List<BranchTagDto.ResponseBranchTagDto> getTagList(Long branchId){

        List<ProductTag> productTagList = productTagRepository.findByBranchIdOrderByIdAsc(branchId);
        List<BranchTag> branchTagList = productTagList.stream().map(ProductTag::getBranchTag).distinct().collect(Collectors.toList());
        return branchTagList.stream().map(BranchTagDto.ResponseBranchTagDto::toDto).collect(Collectors.toList());
    }

    public Page<ProductDto.ResponseProductDto> getList(Long branchId, List<Long> branchTagIdList, String searchWord, Pageable pageable){
        Page<Product> products;
        //한번에 하는 방법 확인하기
        if(branchTagIdList == null || branchTagIdList.isEmpty()){
            products = productRepository.findProductNativeQuery(branchId, searchWord, pageable);
        } else {
            products = productRepository.findProductNativeQueryWithTag(branchId, branchTagIdList, searchWord, branchTagIdList.size(), pageable);
        }
        List<ProductTag> productTagList = productTagRepository.findByBranchIdAndProductIdIn(branchId, products.stream().map(Product::getId).collect(Collectors.toList()));
        List<ProductKlass> productKlassList = productKlassRepository.findByProductIdInAndKlass_ArchiveYn(products.stream().map(Product::getId).collect(Collectors.toList()), false);
        return products.map(
                product ->
                        ProductDto.ResponseProductDto.toDto(product,
                                productTagList.stream().filter(productTag -> productTag.getProduct().getId().equals(product.getId())).map(BranchTagDto.ResponseBranchTagDto::toDto).collect(Collectors.toList()),
                                productKlassList.stream().filter(productKlass -> productKlass.getProduct().getId().equals(product.getId())).map(KlassDto.ResponseKlassDto::toDto).collect(Collectors.toList()))
        );
    }

    @Transactional
    public ProductDto.ResponseProductDto archive(Long productId){

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 수강권이 존재하지 않습니다."));

        product.archiveProduct();
        productRepository.save(product);
        return ProductDto.ResponseProductDto.toDto(product);
    }

    @Transactional
    public ProductDto.ResponseProductDto createProduct(Long branchId, ProductDto.RequestProductDto requestProductDto){

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new NotFoundException("해당 지점이 존재하지 않습니다."));
        //수강권 생성
        Product product = requestProductDto.toEntity(branch);
        productRepository.save(product);
        return ProductDto.ResponseProductDto.toDto(product);
    }

    @Transactional
    public List<Long> createBranchTag(Long branchId, List<BranchTagDto.RequestBranchTagDto> requestTagDtoList){

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new NotFoundException("해당 지점이 존재하지 않습니다."));

        List<String> requestTagNameList = new ArrayList<>();
        requestTagDtoList.forEach(requestBranchTagDto -> requestTagNameList.add(requestBranchTagDto.getTag()));
        //지점에 등록된 태그 조회
        List<BranchTag> branchTagList = branchTagRepository.findByBranchIdAndTagIn(branchId, requestTagNameList);
        //없으면 생성
        List<BranchTag> newBranchTagList = new ArrayList<>();
        for(BranchTagDto.RequestBranchTagDto requestBranchTagDto : requestTagDtoList){
            if(branchTagList.stream().noneMatch(branchTag -> branchTag.getTag().equals(requestBranchTagDto.getTag()))){
                BranchTag branchTag = branchTagRepository.save(requestBranchTagDto.toEntity(branch));
                newBranchTagList.add(branchTag);
            } else {
                newBranchTagList.add(branchTagList.stream().filter(branchTag -> branchTag.getTag().equals(requestBranchTagDto.getTag())).findFirst().get());
            }
        }
        return newBranchTagList.stream().map(BranchTag::getId).collect(Collectors.toList());
    }

    @Transactional
    public List<BranchTagDto.ResponseBranchTagDto> createProductTag(Long productId, List<Long> requestBranchTagIdList){

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 수강권이 존재하지 않습니다."));
        // 지점 태그 조회
        List<BranchTag> branchTagList = branchTagRepository.findByIdIn(requestBranchTagIdList);
        //수강권 태그 생성
        List<ProductTag> productTagList = new ArrayList<>();
        for (BranchTag branchTag : branchTagList) {
            productTagList.add(ProductTag.builder()
                    .branch(product.getBranch())
                    .product(product)
                    .branchTag(branchTag)
                    .build());
        }
        productTagRepository.saveAll(productTagList);
        return productTagList.stream().map(productTag -> BranchTagDto.ResponseBranchTagDto.toDto(productTag.getBranchTag())).collect(Collectors.toList());
    }

    @Transactional
    public List<KlassDto.ResponseKlassDto> createProductKlass(Long productId, List<Long> requestKlassIdList){

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 수강권이 존재하지 않습니다."));

        List<Klass> klassList = klassRepository.findByIdIn(requestKlassIdList);
        List<ProductKlass> productKlassList = new ArrayList<>();
        for(Klass klass : klassList){
            if (!productKlassRepository.findByProductIdAndKlassId(productId, klass.getId()).isEmpty()){
                throw new DuplicateException("이미 등록된 클래스입니다.");
            }
            productKlassList.add(ProductKlass.builder()
                    .branch(product.getBranch())
                    .product(product)
                    .klass(klass)
                    .build());
        }
        productKlassRepository.saveAll(productKlassList);
        return productKlassList.stream().map(KlassDto.ResponseKlassDto::toDto).collect(Collectors.toList());
    }

    @Transactional
    public ProductDto.ResponseProductDto updateProduct(Long productId, ProductDto.RequestProductDto requestProductDto){

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 수강권이 존재하지 않습니다."));

        product.updateProduct(requestProductDto);
        productRepository.save(product);
        return ProductDto.ResponseProductDto.toDto(product);
    }

    @Transactional
    public ProductDto.ResponseProductDto updateProductAndTag(Long productId, ProductDto.RequestProductDto requestProductDto){

        ProductDto.ResponseProductDto productInfo = this.updateProduct(productId, requestProductDto);
        this.deleteProductTag(productId);
        List<Long> branchTagIdList = this.createBranchTag(productInfo.getBranchId(), requestProductDto.getTagList());
        List<BranchTagDto.ResponseBranchTagDto> productTagList = this.createProductTag(productInfo.getProductId(), branchTagIdList);
        productInfo.updateTagList(productTagList);
        return productInfo;
    }

    @Transactional
    public List<KlassDto.ResponseKlassDto> updateProductKlass(Long productId, List<Long> requestKlassIdList){

        this.deleteProductKlass(productId);
        return this.createProductKlass(productId, requestKlassIdList);
    }

    @Transactional
    public void deleteProductKlass(Long productId){

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 수강권이 존재하지 않습니다."));

        List<ProductKlass> productKlassList = productKlassRepository.findByProductId(product.getId());
        productKlassList.forEach(ProductKlass::deleteProductKlass);
        productKlassRepository.saveAll(productKlassList);
    }

    @Transactional
    public void deleteProductTag(Long productId){

        productRepository.findById(productId).orElseThrow(() -> new NotFoundException("해당 수강권이 존재하지 않습니다."));

        //수강권 태그 조회
        List<ProductTag> oldProductTagList = productTagRepository.findByProductId(productId);
        //선택한 수강권 태그 전체 삭제
        oldProductTagList.forEach(ProductTag::deleteProductTag);
        productTagRepository.saveAll(oldProductTagList);
    }

    public Page<BranchTuteeDto.ResponseBranchTuteeDto> getProductTutee(Long productId, Pageable pageable){

        Page<BranchUserOngoingListGroupedProduct> branchUserOngoingListGroupedProductList = branchUserOngoingListGroupedProductRepository.findByProductId(productId, pageable);
        return branchUserOngoingListGroupedProductList.map(BranchTuteeDto.ResponseBranchTuteeDto::toDto);
    }
}
