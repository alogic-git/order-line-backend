package com.orderline.notice.model.entity;

import com.orderline.basic.model.entity.BaseTimeEntity;
import com.orderline.branch.model.entity.Branch;
import com.orderline.common.user.model.entity.User;
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
@Table(name = "branch_notice_comment")
public class BranchNoticeComment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_notice_id")
    private BranchNotice branchNotice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "title")
    private String title;

    @Column(name = "contents")
    private String contents;

    public void delete(){
        this.deleteYn = true;
    }

    public void updateTitle(String title) { this.title = title;}

    public void updateContents(String contents) { this.contents = contents;}
}
