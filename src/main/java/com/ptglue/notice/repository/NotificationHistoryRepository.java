package com.ptglue.notice.repository;

import com.ptglue.notice.enums.NotificationStatusTypeEnum;
import com.ptglue.notice.enums.NotificationTypeEnum;
import com.ptglue.notice.model.entity.NotificationHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface NotificationHistoryRepository extends JpaRepository<NotificationHistory, Long> {

    @Query(value = "SELECT DISTINCT nh.* FROM notification_history AS nh " +
            "INNER JOIN branch AS b ON b.id = nh.branch_id " +
            "AND nh.branch_id = :branchId " +
            "AND nh.delete_yn = false " +
            "AND nh.reg_dt BETWEEN :startDate AND :endDate " +
            "AND nh.notification_type = :#{#notificationType.name()} " +
            "AND (nh.title LIKE :searchWord " +
            "OR nh.contents LIKE :searchWord) " +
            "GROUP BY nh.message_id",
            countQuery = "SELECT count(DISTINCT nh.*) FROM notification_history AS nh " +
                    "INNER JOIN branch AS b ON b.id = nh.branch_id " +
                    "AND nh.branch_id = :branchId " +
                    "AND nh.delete_yn = false " +
                    "AND nh.reg_dt BETWEEN :startDate AND :endDate " +
                    "AND nh.notification_type = :#{#notificationType.name()} " +
                    "AND (nh.title LIKE :searchWord " +
                    "OR nh.contents LIKE :searchWord) " +
                    "GROUP BY nh.message_id",
            nativeQuery = true)
    Page<NotificationHistory> findNotificationHistoryNativeQuery(
            @Param("branchId") Long branchId,
            @Param("notificationType") NotificationTypeEnum notificationType,
            @Param("searchWord") String searchWord,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);

    Integer countByMessageId(Long messageId);

    Integer countByMessageIdAndStatusType(Long messageId, NotificationStatusTypeEnum notificationStatusTypeEnum);

    Page<NotificationHistory> findByMessageId(Long messageId, Pageable pageable);

    Page<NotificationHistory> findByMessageIdAndStatusType(Long messageId, NotificationStatusTypeEnum statusType ,Pageable pageable);
}
