package com.ptglue.branch.repository;

import com.ptglue.branch.model.entity.BranchUserKlassCompositeId;
import com.ptglue.branch.model.entity.BranchUserOngoingListGroupedKlass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchUserOngoingListGroupedKlassRepository extends JpaRepository<BranchUserOngoingListGroupedKlass, BranchUserKlassCompositeId> {

    @EntityGraph(attributePaths = {"user"})
    Page<BranchUserOngoingListGroupedKlass> findByKlassId(Long klassId, Pageable pageable);
}
