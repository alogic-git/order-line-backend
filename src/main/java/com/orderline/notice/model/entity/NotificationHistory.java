package com.orderline.notice.model.entity;

import com.orderline.notice.enums.NotificationStatusTypeEnum;
import com.orderline.notice.enums.NotificationTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@DynamicUpdate
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Where(clause = "delete_yn = 0")
@Entity
@Table(name = "notification_history")
public class NotificationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "branch_id")
//    private Branch branch;

    @Column(name = "target_user_id")
    private Long targetUserId;

    @Column(name = "message_id")
    private Long messageId;

    @Column(name = "original_table_name")
    private String originalTableName;

    @Column(name = "original_table_id")
    private Long originalTableId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_type")
    private NotificationStatusTypeEnum statusType;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type")
    private NotificationTypeEnum notificationType;

    @Column(name = "title")
    private String title;

    @Column(name = "contents")
    private String contents;

    @Column(name = "device_info")
    private String deviceInfo;

    public void updateStatusType(NotificationStatusTypeEnum statusType){ this.statusType = statusType; }

    public void updateMessageId(Long messageId) { this.messageId = messageId; }
}
