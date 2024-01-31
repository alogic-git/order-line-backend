package com.orderline.product.repository;

import com.orderline.product.model.entity.ProductKlass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductKlassRepository extends JpaRepository<ProductKlass, Long> {

    Page<ProductKlass> findByProductIdAndKlass_ArchiveYn(Long productId, Boolean klassArchiveYn, Pageable pageable);

    Page<ProductKlass> findByProductIdAndKlass_ArchiveYnAndKlass_BranchIdAndKlass_Branch_ArchiveYn(Long productId, Boolean klassArchiveYn, Long branchId, Boolean branchArchieYn, Pageable pageable);

    List<ProductKlass> findByProductId(Long productId);

    @EntityGraph(attributePaths = {"klass"})
    List<ProductKlass> findByProductIdInAndKlass_ArchiveYn(List<Long> productIds, Boolean klassArchiveYn);

    List<ProductKlass> findByKlassId(Long klassId);

    List<ProductKlass> findByProductIdAndKlass_ArchiveYnAndKlass_Branch_ArchiveYn(Long productId, Boolean klassArchiveYn, Boolean branchArchiveYn);

    List<ProductKlass> findByBranchId(Long branchId);

    List<ProductKlass> findByProductIdAndKlassId(Long productId, Long klassId);
}
