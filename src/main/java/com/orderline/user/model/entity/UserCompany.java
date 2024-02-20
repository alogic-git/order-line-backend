package com.orderline.user.model.entity;

import com.orderline.basic.model.entity.BaseTimeEntity;
import com.orderline.site.model.entity.ConstructionCompany;

import javax.persistence.*;

public class UserCompany extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private ConstructionCompany constructionCompany;
}
