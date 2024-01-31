package com.ptglue.branch.model.entity;

import com.ptglue.branch.enums.ConnectionTypeEnum;
import com.ptglue.common.user.enums.UserRoleEnum;
import com.ptglue.common.user.model.entity.User;
import com.ptglue.product.model.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.time.LocalDate;

@IdClass(BranchUserProductCompositeId.class)
@Immutable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "branch_user_ongoing_list_grouped_product")
public class BranchUserOngoingListGroupedProduct {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="branch_id")
    private Branch branch;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="product_id")
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "connection_type")
    private ConnectionTypeEnum connectionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type")
    private UserRoleEnum roleType;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_user_role_id")
    private BranchUserRole branchUserRole;

    @Column(name = "total_reservation_count")
    private Integer totalReservationCount;

    @Column(name = "remain_reservation_count")
    private Integer remainReservationCount;

    @Column(name = "available_reservation_count")
    private Integer availableReservationCount;

    @Column(name = "total_other_branch_reservation_count")
    private Integer totalOtherBranchReservationCount;

    @Column(name = "remain_other_branch_reservation_count")
    private Integer remainOtherBranchReservationCount;

    @Column(name = "available_other_branch_reservation_count")
    private Integer availableOtherBranchReservationCount;

    private Integer price;

    @Column(name = "total_paid_price")
    private Integer totalPaidPrice;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

}
