package com.orderline.common.user.model.entity;

import com.orderline.basic.model.entity.BaseTimeEntity;
import com.orderline.common.user.enums.LoginTypeEnum;
import com.orderline.common.user.enums.UserOutTypeEnum;
import com.orderline.common.user.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@DynamicInsert    //null field 지워줌
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
@Table(name="user_out_rest")
@Where(clause = "delete_yn = 0")
public class UserOutRest extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column
    private String username;

    private String password;

    private String name;

    @Column
    private String phone;

    @Column(name = "phone_active_yn")
    private Boolean phoneActiveYn;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "last_login_role_type")
    private UserRoleEnum lastLoginRoleType;

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type")
    private LoginTypeEnum loginType;

    @Column(name = "profile_uri")
    private String profileUri;

    @Column(name = "recommended_user_id")
    private Long recommendedUserId;

    @Column(name = "advertise_yn")
    private Boolean advertiseYn;

    @Column(name = "join_dt")
    private ZonedDateTime joinDt;

    @Column(name = "last_login_dt")
    private ZonedDateTime lastLoginDt;

    @Enumerated(EnumType.STRING)
    @Column(name = "out_type")
    private UserOutTypeEnum outType;

    @Column(name = "out_reason")
    private String outReason;

    public void updatePassword(String password){
        this.password = password;
    }

    public void delete(){
        this.deleteYn = true;
    }
}
