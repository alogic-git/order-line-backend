package com.ptglue.klass.repository;

import com.ptglue.klass.model.entity.Klass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KlassRepository extends JpaRepository<Klass, Long> {

    @Query(value = "SELECT DISTINCT k.* FROM klass as k " +
            "INNER JOIN branch_user_role as bu " +
            "on (bu.id = k.main_tutor_id or bu.id = k.sub_tutor_id) " +
            "and bu.role_type = 'TUTOR' " +
            "and bu.archive_yn = false " +
            "and bu.delete_yn = false " +
            "INNER JOIN user as u " +
            "on (u.id = bu.user_id) " +
            "and u.delete_yn = false " +
            "and k.branch_id = :branchId " +
            "and k.delete_yn = false " +
            "and k.archive_yn = false "+
            "and (bu.nickname like :searchWord " +
            "or u.name like :searchWord " +
            "or k.klass_name like :searchWord) " ,
            countQuery = "SELECT count(DISTINCT k.id) FROM klass as k " +
                    "INNER JOIN branch_user_role as bu " +
                    "on (bu.id = k.main_tutor_id or bu.id = k.sub_tutor_id) " +
                    "and bu.role_type = 'TUTOR' " +
                    "and bu.archive_yn = false " +
                    "and bu.delete_yn = false " +
                    "INNER JOIN user as u " +
                    "on (u.id = bu.user_id) " +
                    "and u.delete_yn = false " +
                    "and k.branch_id = :branchId " +
                    "and k.delete_yn = false " +
                    "and k.archive_yn = false "+
                    "and (bu.nickname like :searchWord " +
                    "or u.name like :searchWord " +
                    "or k.klass_name like :searchWord) " ,
            nativeQuery = true)
    Page<Klass> findKlassNativeQuery(
            @Param("branchId") Long branchId,
            @Param("searchWord") String searchWord,
            Pageable pageable);

    List<Klass> findByIdIn(List<Long> klassIds);
    Page<Klass> findByIdInAndReservationEnableYn(List<Long> klassIds, Boolean reservationEnableYn, Pageable pageable);
    List<Klass> findByBranchId(Long branchId);

}
