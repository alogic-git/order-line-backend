package com.orderline.common.user.model.entity;


import com.orderline.basic.model.entity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;

// 로그인 후 User 의 refresh token 을 저장하기 위한 Entity
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Table(name="activation_code")
@Where(clause = "delete_yn = 0")
public class ActivationCode extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phone;

    private String email;

    @Column(name = "activation_code")
    private String activationCode;

    @Column(name = "activation_yn")
    private Boolean activationYn;

    public void deleteActivationCode() {
        this.deleteYn = true;
    }

    public void updateActivationStatus() {
        this.activationYn = true;
    }

    public void updateDisableActivationStatus() {
        this.activationYn = false;
    }

}

