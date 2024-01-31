package com.orderline.common.user_qa.service;

import com.orderline.admin.user_qa_comment.model.entity.UserQaComment;
import com.orderline.admin.user_qa_comment.repository.UserQaCommentRepository;
import com.orderline.common.user.model.entity.User;
import com.orderline.common.user.repository.UserRepository;
import com.orderline.common.user_qa.model.dto.UserQaDto;
import com.orderline.common.user_qa.model.entity.UserQa;
import com.orderline.common.user_qa.repository.UserQaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

@DependsOn("Env")
@Slf4j
@Service
public class UserQaService {

    @Resource(name = "userQaRepository")
    private UserQaRepository userQaRepository;

    @Resource(name = "userQaCommentRepository")
    private UserQaCommentRepository userQaCommentRepository;

    @Resource(name = "userRepository")
    private UserRepository userRepository;

    public UserQaDto.ResponseUserQaDto get(Long userQaId, Long userId){
        Optional<UserQa> userQaOptional = userQaRepository.findByIdAndUserId(userQaId, userId);
        if(userQaOptional.isPresent()){
            UserQa userQa = userQaOptional.get();
            return UserQaDto.ResponseUserQaDto.toDto(userQa);
        }
        return null;
    }
    @Transactional
    public Page<UserQaDto.ResponseUserQaDto> getList(Long userId, Pageable pageable) {
        // Pagination 구현을 위해 Pageable 객체 생성
        Page<UserQa> userQas = userQaRepository.findByUserId(userId, pageable);
        return userQas.map(UserQaDto.ResponseUserQaDto::toDto);
    }

    public Long create(Long userId, UserQaDto.RequestUserQaDto createUserQaRequest) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            return userQaRepository.save(createUserQaRequest.toEntity(user, createUserQaRequest)).getId();
        }
        return 0L;
    }

    public Long delete(Long userQaId) {
        Optional<UserQa> userQaOptional = userQaRepository.findById(userQaId);
        if(userQaOptional.isPresent()){
            UserQa userQa = userQaOptional.get();
            userQa.delete();
            return userQaRepository.save(userQa).getId();
        }
        return 0L;
    }

    public UserQaDto.ResponseUserQaCommentDto getComment(Long userQaCommentId) {
        Optional<UserQaComment> userQaCommentOptional = userQaCommentRepository.findById(userQaCommentId);
        if(userQaCommentOptional.isPresent()){
            UserQaComment userQaComment = userQaCommentOptional.get();
            return UserQaDto.ResponseUserQaCommentDto.toDto(userQaComment);
        }
        return null;
    }

    public Page<UserQaDto.ResponseUserQaCommentDto> getCommentList(Long userQaId, Pageable pageable) {
        // Pagination 구현을 위해 Pageable 객체 생성
        Page<UserQaComment> userQaComments = userQaCommentRepository.findByUserQaId(userQaId, pageable);
        return userQaComments.map(UserQaDto.ResponseUserQaCommentDto::toDto);
    }

}
