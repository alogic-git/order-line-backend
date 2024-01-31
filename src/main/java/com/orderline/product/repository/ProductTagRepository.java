package com.orderline.product.repository;

import com.orderline.product.model.entity.ProductTag;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductTagRepository extends JpaRepository<ProductTag, Long> {

    List<ProductTag> findByProductId(Long productId);

    @EntityGraph(attributePaths = {"branchTag"})
    List<ProductTag> findByBranchIdOrderByIdAsc(Long branchId);

    @EntityGraph(attributePaths = {"branchTag"})
    List<ProductTag> findByBranchIdAndProductIdIn(Long branchId, List<Long> productIdList);

}
