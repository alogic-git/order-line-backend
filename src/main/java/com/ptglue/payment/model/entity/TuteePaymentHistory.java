package com.ptglue.payment.model.entity;

import com.ptglue.basic.model.entity.BaseTimeEntity;
import com.ptglue.payment.enums.PaymentHistoryPaidTypeEnum;
import com.ptglue.payment.enums.PaymentHistoryPaidMethodTypeEnum;
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
@Entity
@Where(clause = "delete_yn = 0")
@Table(name = "tutee_payment_history")
public class TuteePaymentHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Column(name = "ticket_id")
    private Long ticketId;

    @Column(name = "extra_product_purchase_id")
    private Long extraProductPurchaseId;

    @Column(name = "paid_date")
    private LocalDate paidDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "paid_method_type")
    private PaymentHistoryPaidMethodTypeEnum paidMethodType;

    @Enumerated(EnumType.STRING)
    @Column(name = "paid_type")
    private PaymentHistoryPaidTypeEnum paidType;

    @Column(name = "paid_price")
    private Integer paidPrice;

    @Column(name = "poss_yn")
    private Boolean possYn;
}
