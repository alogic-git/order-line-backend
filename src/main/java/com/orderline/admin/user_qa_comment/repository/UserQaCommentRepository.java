package com.orderline.admin.user_qa_comment.repository;

import com.orderline.admin.user_qa_comment.model.entity.UserQaComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserQaCommentRepository extends JpaRepository<UserQaComment, Long> {

    Page<UserQaComment> findByUserQaId(Long userQaId, Pageable pageable);


}
