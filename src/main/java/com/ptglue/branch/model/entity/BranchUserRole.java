package com.ptglue.branch.model.entity;

import com.ptglue.basic.model.entity.BaseTimeEntity;
import com.ptglue.common.user.enums.UserRoleEnum;
import com.ptglue.common.user.model.entity.User;
import com.ptglue.branch.enums.ConnectionTypeEnum;
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
@Entity
@Builder
@Getter
@Where(clause = "delete_yn = 0")
@Table(name="branch_user_role")
public class BranchUserRole extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "branch_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Branch branch;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type")
    private UserRoleEnum roleType;

    @Column(name = "nickname")
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "connection_type")
    private ConnectionTypeEnum connectionType;

    @Column(name = "last_view_yn")
    private Boolean lastViewYn;

    @Column(name = "active_ticket_num")
    private Integer activeTicketNum;

    @Column(name = "other_branch_added_yn")
    private Boolean otherBranchAddedYn;

    @Column(name = "archive_yn")
    private Boolean archiveYn;

    public void updateLastViewYn(Boolean lastViewYn) {
        this.lastViewYn = lastViewYn;
    }

    public void updateArchive(Boolean archiveYn) {
        this.archiveYn = archiveYn;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateConnectType(ConnectionTypeEnum connectionType) {
        this.connectionType = connectionType;
    }

    public void delete() {
        this.deleteYn = true;
    }
}
