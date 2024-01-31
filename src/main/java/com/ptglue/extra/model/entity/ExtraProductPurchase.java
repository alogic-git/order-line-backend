package com.ptglue.extra.model.entity;

import com.ptglue.basic.model.entity.BaseTimeEntity;
import com.ptglue.common.user.model.entity.User;
import com.ptglue.extra.enums.ExtraPaidStatusTypeEnum;
import com.ptglue.extra.enums.ExtraStatusTypeEnum;
import com.ptglue.branch.model.entity.Branch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

@DynamicUpdate
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Where(clause = "delete_yn = 0")
@Entity
@Table(name = "extra_product_purchase")
public class ExtraProductPurchase extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "extra_product_id")
    private ExtraProduct extraProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutee_id")
    private User tutee;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "status_type")
    private ExtraStatusTypeEnum statusType;

    @Column(name = "paid_status_type")
    private ExtraPaidStatusTypeEnum paidStatusType;

    @Column(name = "archive_yn")
    private Boolean archiveYn;

    public void archiveExtraProductPurchase(){
        this.archiveYn = true;
    }
}
