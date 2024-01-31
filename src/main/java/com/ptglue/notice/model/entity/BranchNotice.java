package com.ptglue.notice.model.entity;

import com.ptglue.basic.model.entity.BaseTimeEntity;
import com.ptglue.branch.model.entity.Branch;
import com.ptglue.notice.enums.NoticeTypeEnum;
import com.ptglue.notice.enums.NotificationTypeEnum;
import com.ptglue.notice.enums.BranchNoticeTargetTypeEnum;
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
@Table(name = "branch_notice")
public class BranchNotice extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Enumerated(EnumType.STRING)
    @Column(name = "notice_type")
    private NoticeTypeEnum noticeType;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type")
    private NotificationTypeEnum notificationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type")
    private BranchNoticeTargetTypeEnum targetType;

    @Column(name = "title")
    private String title;

    @Column(name = "contents")
    private String contents;

    @Column(name = "popup_display_yn")
    private Boolean popupDisplayYn;

    @Column(name = "important_yn")
    private Boolean importantYn;

    @Column(name = "comment_yn")
    private Boolean commentYn;

    @Column(name = "like_yn")
    private Boolean likeYn;

    @Column(name = "hits")
    private Integer hits;

    @Column(name = "like")
    private Integer like;

    public void incrementsHits(){ this.hits += 1; }

    public void incrementsLike(){
        this.like += 1;
    }
}
