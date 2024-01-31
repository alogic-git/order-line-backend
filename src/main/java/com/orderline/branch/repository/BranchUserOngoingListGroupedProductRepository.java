package com.orderline.branch.repository;

import com.orderline.branch.model.entity.BranchUserOngoingListGroupedProduct;
import com.orderline.branch.model.entity.BranchUserProductCompositeId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchUserOngoingListGroupedProductRepository extends JpaRepository<BranchUserOngoingListGroupedProduct, BranchUserProductCompositeId> {

    @EntityGraph(attributePaths = {"user"})
    Page<BranchUserOngoingListGroupedProduct> findByProductId(Long productId, Pageable pageable);

}

