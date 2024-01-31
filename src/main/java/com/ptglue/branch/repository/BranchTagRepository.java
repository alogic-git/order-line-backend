package com.ptglue.branch.repository;

import com.ptglue.branch.model.entity.BranchTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchTagRepository extends JpaRepository<BranchTag, Long> {

    List<BranchTag> findByBranchIdAndTagIn(Long branchId, List<String> tagList);

    List<BranchTag> findByIdIn(List<Long> branchTagIdList);
}
