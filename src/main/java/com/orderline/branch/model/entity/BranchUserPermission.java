package com.orderline.branch.model.entity;

import com.orderline.basic.model.entity.BaseTimeEntity;
import com.orderline.common.user.model.entity.User;
import com.orderline.branch.enums.FunctionTypeEnum;
import com.orderline.branch.enums.PermissionTypeEnum;
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
@Table(name="branch_user_permission")
public class BranchUserPermission extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_user_id")
    private BranchUserRole branchUserRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "function_type")
    private FunctionTypeEnum functionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission_type")
    private PermissionTypeEnum permissionType;

    public void updatePermission(PermissionTypeEnum permissionType) {
        this.permissionType = permissionType;
    }

    public void deletePermission(){
        this.deleteYn = true;
    }
}
