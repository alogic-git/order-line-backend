package com.ptglue.common.user.model.entity;

import com.ptglue.basic.model.entity.BaseTimeEntity;
import com.ptglue.common.user.enums.LoginTypeEnum;
import com.ptglue.common.user.enums.UserOutTypeEnum;
import com.ptglue.common.user.enums.UserRoleEnum;
import com.ptglue.common.user.model.dto.UserDto;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
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

    private String username;

    private String password;

    @Column(name = "name")
    private String name;

    private String phone;

    @Column(name = "phone_active_yn")
    private Boolean phoneActiveYn;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    private String gender;

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

    @Column(name = "dark_mode_yn")
    private Boolean darkModeYn;

    @Column(name = "admin_yn")
    private Boolean adminYn;

    public void updateLastLoginDt(){
        this.lastLoginDt = ZonedDateTime.now(ZoneId.of("UTC"));
    }

    public void activateUser(UserDto.UserInfoDto userInfoDto){
        this.name = userInfoDto.getName();
        this.phone = userInfoDto.getPhone();
        this.phoneActiveYn = userInfoDto.getPhoneActiveYn();
        this.birthDate = userInfoDto.getBirthDate();
        this.loginType = userInfoDto.getLoginType();
        this.profileUri = userInfoDto.getProfileUri();
        this.recommendedUserId = userInfoDto.getRecommendUserId();
        this.advertiseYn = userInfoDto.getAdvertiseYn();
        this.joinDt = userInfoDto.getJoinDt();
        this.lastLoginDt = userInfoDto.getLastLoginDt() ;
        this.outType = UserOutTypeEnum.ACTIVE;
    }

    public void deleteUser(){
        this.deleteYn = true;
    }

    public void updateUsernameToTmp(String username, String tmpPassword){
        this.username = username;
        this.password = tmpPassword;
    }

    public void updateProfileUrl(String profileUri){
        this.profileUri = profileUri;
    }

    public void encodePassword(String password){
        this.password = password;
    }

    public void updatePassword(String password){
        this.password = password;
    }

    public void updateLastLoginRoleType(UserRoleEnum lastLoginRoleType){
        this.lastLoginRoleType = lastLoginRoleType;
    }

    public void updateUserPhone(String phone){ this.phone = phone; }

    public void updatePhoneActiveYn(Boolean phoneActiveYn){ this.phoneActiveYn = phoneActiveYn; }

    public void updateName (String name) { this.name = name; }

    public void updateBirthDate (LocalDate birthDate) { this.birthDate = birthDate;}
}
