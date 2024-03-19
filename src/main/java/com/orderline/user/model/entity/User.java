package com.orderline.user.model.entity;

import com.orderline.basic.model.entity.BaseTimeEntity;
import com.orderline.site.model.entity.Site;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@DynamicUpdate
@DynamicInsert    //null field 지워줌
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
@Where(clause = "delete_yn = 0")
@Table(name="user")
public class User  extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "phone")
    private String phone;

    @Column(name = "join_dt")
    private ZonedDateTime joinDt;

    @Column(name = "last_login_dt")
    private ZonedDateTime lastLoginDt;

    @Column(name = "admin_yn")
    private Boolean adminYn;

    @Column(name = "site_id")
    private Long siteId;

    public void updateLastLoginDt(){
        this.lastLoginDt = ZonedDateTime.now(ZoneId.of("UTC"));
    }

    public void updatePassword(String password){
        this.password = password;
    }

    public void updateSite(Site site) {
        this.siteId = site.getId();
    }
}
