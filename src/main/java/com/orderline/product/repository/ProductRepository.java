package com.orderline.product.repository;

import com.orderline.product.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    //검색했을 때 어느 항목 포함되어야 하는지 확인하기
    @Query(value = "SELECT DISTINCT p.* FROM product as p " +
            "LEFT JOIN product_tag as pt on pt.product_id = p.id " +
            "where p.branch_id = :branchId " +
            "and p.archive_yn = false " +
            "and p.delete_yn = false " +
            "and p.product_name like :searchWord " ,
            countQuery = "SELECT count(DISTINCT p.id) FROM product as p  " +
                    "LEFT JOIN product_tag as pt on pt.product_id = p.id " +
                    "where p.branch_id = :branchId " +
                    "and p.archive_yn = false " +
                    "and p.delete_yn = false " +
                    "and p.product_name like :searchWord " ,
            nativeQuery = true)
    Page<Product> findProductNativeQuery(
            @Param("branchId") Long branchId,
            @Param("searchWord") String searchWord,
            Pageable pageable);

    @Query(value = "SELECT DISTINCT p.* FROM product as p " +
            "INNER JOIN product_tag as pt on pt.product_id = p.id and pt.delete_yn = false " +
            "and p.branch_id = :branchId " +
            "and p.archive_yn = false " +
            "and p.delete_yn = false " +
            "and p.product_name like :searchWord " +
            "and pt.branch_tag_id in :tagList " +
            "GROUP BY p.id " +
            "HAVING COUNT(DISTINCT pt.branch_tag_id) = :tagListSize"
            ,
            countQuery = "SELECT count(DISTINCT p.id) FROM product as p  " +
                    "INNER JOIN product_tag as pt on pt.product_id = p.id and pt.delete_yn = false " +
                    "and p.branch_id = :branchId " +
                    "and p.archive_yn = false " +
                    "and p.delete_yn = false " +
                    "and p.product_name like :searchWord " +
                    "and pt.branch_tag_id in :tagList " +
                    "GROUP BY p.id " +
                    "HAVING COUNT(DISTINCT pt.branch_tag_id) = :tagListSize",
            nativeQuery = true)
    Page<Product> findProductNativeQueryWithTag(
            @Param("branchId") Long branchId,
            @Param("tagList") List<Long> tagList,
            @Param("searchWord") String searchWord,
            @Param("tagListSize") int tagListSize,
            Pageable pageable);

    Page<Product> findByBranchIdAndArchiveYn(Long branchId, Boolean archiveYn, Pageable pageable);

    List<Product> findByIdIn(List<Long> productIdList);
}
