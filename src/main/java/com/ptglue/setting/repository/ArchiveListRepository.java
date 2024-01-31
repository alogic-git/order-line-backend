package com.ptglue.setting.repository;

import com.ptglue.branch.enums.FunctionTypeEnum;
import com.ptglue.setting.model.entity.ArchiveList;
import com.ptglue.setting.model.entity.CategoryCompositeId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchiveListRepository extends JpaRepository<ArchiveList, CategoryCompositeId> {

    Page<ArchiveList> findByBranchId(Long branchId, Pageable pageable);

    Page<ArchiveList> findByBranchIdAndCategory(Long branchId, FunctionTypeEnum category, Pageable pageable);

}
