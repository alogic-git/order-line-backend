package com.orderline.setting.repository;

import com.orderline.branch.enums.FunctionTypeEnum;
import com.orderline.setting.model.entity.ArchiveList;
import com.orderline.setting.model.entity.CategoryCompositeId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchiveListRepository extends JpaRepository<ArchiveList, CategoryCompositeId> {

    Page<ArchiveList> findByBranchId(Long branchId, Pageable pageable);

    Page<ArchiveList> findByBranchIdAndCategory(Long branchId, FunctionTypeEnum category, Pageable pageable);

}
