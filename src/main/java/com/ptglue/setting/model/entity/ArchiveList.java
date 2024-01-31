package com.ptglue.setting.model.entity;

import com.ptglue.branch.enums.FunctionTypeEnum;
import com.ptglue.branch.model.entity.Branch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;

@IdClass(CategoryCompositeId.class)
@Immutable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "archive_list")
public class ArchiveList {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Id
    @Enumerated(EnumType.STRING)
    private FunctionTypeEnum category;

    @Id
    @Column(name = "category_id")
    private Long categoryId;

    private String name;

    private String info1;

    private String info2;

    private String info3;

    private String info4;

}
