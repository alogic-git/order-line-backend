package com.orderline.extra.model.entity;

import com.orderline.basic.model.entity.BaseTimeEntity;
import com.orderline.branch.model.entity.Branch;
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
@Table(name = "extra_product")
public class ExtraProduct extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Column(name = "extra_product_name")
    private String extraProductName;

    @Column(name = "price")
    private Integer price;

    @Column(name = "private_memo")
    private String privateMemo;

    @Column(name = "public_memo")
    private String publicMemo;

    @Column(name = "archive_yn")
    private Boolean archiveYn;

    public void archiveExtraProduct(){
        this.archiveYn = true;
    }
}
