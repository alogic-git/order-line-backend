package com.orderline.admin.user_qa_comment.service;

import com.orderline.admin.user_qa_comment.repository.UserQaCommentRepository;
import com.orderline.common.user_qa.model.entity.UserQa;
import com.orderline.common.user_qa.repository.UserQaRepository;
import com.orderline.admin.user_qa_comment.model.dto.UserQaCommentDto;
import com.orderline.admin.user_qa_comment.model.entity.UserQaComment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@DependsOn("Env")
@Slf4j
@Service
public class UserQaCommentService {

    @Resource(name = "userQaRepository")
    private UserQaRepository userQaRepository;

    @Resource(name = "userQaCommentRepository")
    private UserQaCommentRepository userQaCommentRepository;


    public UserQaCommentDto.ResponseUserQaDto getUserQa(Long userQaId) {
        Optional<UserQa> userQaOptional = userQaRepository.findById(userQaId);
        if(userQaOptional.isPresent()){
            UserQa userQa = userQaOptional.get();
            return UserQaCommentDto.ResponseUserQaDto.toDto(userQa);
        }
        return null;
    }

    public Page<UserQaCommentDto.ResponseUserQaDto> getUserQaList(Pageable pageable) {
        // Pagination 구현을 위해 Pageable 객체 생성
        Page<UserQa> systemQuestions = userQaRepository.findAll(pageable);
        return systemQuestions.map(UserQaCommentDto.ResponseUserQaDto::toDto);
    }

    public UserQaCommentDto.ResponseUserQaCommentDto getUserQaComment(Long userQaCommentId) {
        Optional<UserQaComment> systemQuestionAnswerOptional = userQaCommentRepository.findById(userQaCommentId);
        if(systemQuestionAnswerOptional.isPresent()){
            UserQaComment systemQuestionAnswer = systemQuestionAnswerOptional.get();
            return UserQaCommentDto.ResponseUserQaCommentDto.toDto(systemQuestionAnswer);
        }
        return null;
    }

    public Page<UserQaCommentDto.ResponseUserQaCommentDto> getUserQaCommentList(Long userQaId, Pageable pageable) {
        // Pagination 구현을 위해 Pageable 객체 생성
        Page<UserQaComment> userQaComments = userQaCommentRepository.findByUserQaId(userQaId, pageable);
        return userQaComments.map(UserQaCommentDto.ResponseUserQaCommentDto::toDto);
    }

    public Long createUserQaComment(Long userQaId, UserQaCommentDto.RequestUserQaCommentDto createUserQaCommentRequest) {
        Optional<UserQa> userQaOptional = userQaRepository.findById(userQaId);
        if(userQaOptional.isPresent()){
            UserQa userQa = userQaOptional.get();
            return userQaCommentRepository.save(createUserQaCommentRequest.toEntity(userQa, createUserQaCommentRequest)).getId();
        }
        return 0L;
    }

    public Long deleteUserQaComment(Long userQaCommentId) {
        Optional<UserQaComment> userQaCommentOptional = userQaCommentRepository.findById(userQaCommentId);
        if(userQaCommentOptional.isPresent()){
            UserQaComment userQaComment = userQaCommentOptional.get();
            userQaComment.delete();
            return userQaCommentRepository.save(userQaComment).getId();
        }
        return 0L;
    }

}
