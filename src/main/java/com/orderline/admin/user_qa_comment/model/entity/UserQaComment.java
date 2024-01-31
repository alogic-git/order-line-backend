package com.orderline.admin.user_qa_comment.model.entity;

import com.orderline.basic.model.entity.BaseTimeEntity;
import com.orderline.common.user_qa.model.entity.UserQa;
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
@Table(name="user_qa_comment")
public class UserQaComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_qa_id")
    private UserQa userQa;

    private String title;

    private String contents;

    public void delete(){
        this.deleteYn = true;
    }

}
