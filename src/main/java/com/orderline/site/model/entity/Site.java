package com.orderline.site.model.entity;

import com.orderline.basic.model.entity.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@DynamicUpdate
@DynamicInsert    //null field 지워줌
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
@Where(clause = "delete_yn = 0")
@Table(name="site")
public class Site extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private ConstructionCompany constructionCompany;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "manager_name")
    private String managerName;

    public void deleteSite(){
        this.deleteYn = true;
    }

    public void updateName(String name){
        this.name = name;
    }

}