package com.orderline.branch.repository;

import com.orderline.branch.model.entity.BranchUserEndList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchUserEndListRepository extends JpaRepository<BranchUserEndList, Long> {

    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT DISTINCT bul FROM BranchUserEndList bul " +
            "INNER JOIN bul.user u " +
            "WHERE bul.branch.id = :branchId " +
            "AND (u.username LIKE :searchWord OR u.name LIKE :searchWord OR u.phone LIKE :searchWord)")
    Page<BranchUserEndList> findEndTuteeBySearchWord(@Param("branchId") Long branchId,
                                             @Param("searchWord") String searchWord,
                                             Pageable pageable);

}

