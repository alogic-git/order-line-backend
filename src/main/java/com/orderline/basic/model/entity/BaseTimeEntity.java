package com.orderline.basic.model.entity;


import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @LastModifiedBy
    @Column(name = "mod_user_id")
    private Long modUserId;

    @Column(name = "mod_dt")
    private LocalDateTime modDateTime;

    @CreatedBy
    @Column(name = "reg_user_id")
    private Long regUserId;

    @Column(name = "reg_dt")
    private LocalDateTime regDateTime;

    @Column(name = "delete_yn")
    protected Boolean deleteYn = false;

    public void createDateTime(LocalDateTime regDateTime) {
        this.regDateTime = regDateTime;
    }

    public void modifiedDateTime(LocalDateTime modDateTime) {
        this.modDateTime = modDateTime;
    }
}
