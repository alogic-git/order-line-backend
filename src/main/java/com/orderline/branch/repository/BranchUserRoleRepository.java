package com.orderline.branch.repository;

import com.orderline.common.user.enums.UserRoleEnum;
import com.orderline.branch.model.entity.BranchUserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchUserRoleRepository extends JpaRepository<BranchUserRole, Long> {

    Optional<BranchUserRole> findByUserIdAndLastViewYnAndArchiveYn(Long userId, Boolean lastViewYn, Boolean archiveYn);

    List<BranchUserRole> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"branch"})
    List<BranchUserRole> findByUserIdAndArchiveYnAndBranch_DeleteYnAndBranch_ArchiveYn(Long userId, Boolean archiveYn, Boolean branchDeleteYn, Boolean branchArchiveYn);

    List<BranchUserRole> findByBranchIdAndUser_Phone(Long branchId, String phone);

    Optional<BranchUserRole> findByBranchIdAndUserIdAndRoleType(Long branchId, Long userId, UserRoleEnum roleType);

    List<BranchUserRole> findByBranchId(Long branchId);

    Optional<BranchUserRole> findByUserIdAndBranchId(Long userId, Long branchId);

    Page<BranchUserRole> findAllByUserId(Long userId, Pageable pageable);
    
    @Query(value = "SELECT DISTINCT bu.* FROM branch_user_role as bu " +
            "INNER JOIN user as u on u.id = bu.user_id " +
            "and bu.branch_id = :branchId " +
            "and bu.role_type = 'TUTOR' " +
            "and bu.archive_yn = false " +
            "and bu.delete_yn = false " +
            "and (bu.nickname like :searchWord " +
            "or u.username like :searchWord " +
            "or u.name like :searchWord " +
            "or u.phone like :searchWord) " ,
            countQuery = "SELECT count(DISTINCT bu.id) FROM branch_user_role as bu " +
                    "INNER JOIN user as u on u.id = bu.user_id " +
                    "and bu.branch_id = :branchId " +
                    "and bu.role_type = 'TUTOR'" +
                    "and bu.archive_yn = false " +
                    "and bu.delete_yn = false " +
                    "and (bu.nickname like :searchWord " +
                    "or u.username like :searchWord " +
                    "or u.name like :searchWord " +
                    "or u.phone like :searchWord) " ,
            nativeQuery = true)
    Page<BranchUserRole> findTutorNativeQuery(
            @Param("branchId") Long branchId,
            @Param("searchWord") String searchWord,
            Pageable pageable);


    @Query(value = "SELECT DISTINCT bu.* FROM branch_user_role as bu " +
            "INNER JOIN user as u on u.id = bu.user_id " +
            "and bu.user_id = :userId " +
            "and bu.connection_type = 'CONNECTED' " +
            "and bu.archive_yn = false " +
            "and bu.delete_yn = false " +
            "and (bu.role_type = 'TUTOR' or bu.role_type = 'MANAGER')  " +
            "INNER JOIN branch as b on b.id = bu.branch_id " +
            "and b.archive_yn = false " +
            "and b.delete_yn = false ",
            countQuery = "SELECT count(DISTINCT bu.id) FROM branch_user_role as bu " +
                    "INNER JOIN user as u on u.id = bu.user_id " +
                    "and bu.user_id = :userId " +
                    "and bu.connection_type = 'CONNECTED' " +
                    "and bu.archive_yn = false " +
                    "and bu.delete_yn = false " +
                    "and (bu.role_type = 'TUTOR' or bu.role_type = 'MANAGER')  " +
                    "INNER JOIN branch as b on b.id = bu.branch_id " +
                    "and b.archive_yn = false " +
                    "and b.delete_yn = false ",
            nativeQuery = true
    )
    Page<BranchUserRole> findBranchListNativeQuery(
            @Param("userId") Long userId,
            Pageable pageable);

    Page<BranchUserRole> findByBranchIdAndRoleTypeAndArchiveYn(Long branchId, UserRoleEnum roleType, Boolean archiveYn, Pageable pageable);

    Integer countByUserId(Long userId);
}
