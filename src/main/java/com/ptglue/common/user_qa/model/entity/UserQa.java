package com.ptglue.common.user_qa.model.entity;

import com.ptglue.basic.enums.QaDetailTypeEnum;
import com.ptglue.basic.enums.QaTypeEnum;
import com.ptglue.basic.model.entity.BaseTimeEntity;
import com.ptglue.common.user.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@DynamicInsert    //null field 지워줌
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
@Where(clause = "delete_yn = 0")
@Table(name="user_qa")
public class UserQa extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "question_type")
    @Enumerated(EnumType.STRING)
    private QaTypeEnum qaType;

    @Column(name = "question_detail_type")
    @Enumerated(EnumType.STRING)
    private QaDetailTypeEnum qaDetailType;

    private String title;

    private String contents;

    private String email;

    public void delete(){
        this.deleteYn = true;
    }
}
