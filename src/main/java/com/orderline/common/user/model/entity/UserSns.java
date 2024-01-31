package com.orderline.common.user.model.entity;

import com.orderline.basic.model.entity.BaseTimeEntity;
import com.orderline.common.user.enums.SnsTypeEnum;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@DynamicInsert    //null field 지워줌
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
@Table(name="user_sns")
@Where(clause = "delete_yn = 0")
public class UserSns extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "sns_type")
    private SnsTypeEnum snsType;

    @Column(name = "sns_id")
    private String snsId;

    @Column(name = "sns_token")
    private String snsToken;

    public void deleteSnsInfo(){
        this.deleteYn = false;
    }
}
