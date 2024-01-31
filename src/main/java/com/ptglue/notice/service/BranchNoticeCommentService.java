package com.ptglue.notice.service;

import com.ptglue.basic.exception.NotFoundException;
import com.ptglue.branch.model.entity.Branch;
import com.ptglue.branch.repository.BranchRepository;
import com.ptglue.common.user.model.entity.User;
import com.ptglue.common.user.repository.UserRepository;
import com.ptglue.notice.model.dto.BranchNoticeCommentDto;
import com.ptglue.notice.model.entity.BranchNotice;
import com.ptglue.notice.model.entity.BranchNoticeComment;
import com.ptglue.notice.repository.BranchNoticeCommentRepository;
import com.ptglue.notice.repository.BranchNoticeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class BranchNoticeCommentService {
    @Resource(name = "branchNoticeCommentRepository")
    BranchNoticeCommentRepository branchNoticeCommentRepository;

    @Resource(name = "branchNoticeRepository")
    BranchNoticeRepository branchNoticeRepository;

    @Resource(name = "branchRepository")
    BranchRepository branchRepository;

    @Resource(name = "userRepository")
    UserRepository userRepository;

    public Page<BranchNoticeCommentDto.ResponseBranchNoticeCommentDto> getList(Long branchNoticeId, Pageable pageable){
        Page<BranchNoticeComment> branchNoticeCommentList = branchNoticeCommentRepository.findAllByBranchNoticeId(branchNoticeId, pageable);

        return branchNoticeCommentList.map(BranchNoticeCommentDto.ResponseBranchNoticeCommentDto::toDto);
    }

    public BranchNoticeCommentDto.ResponseBranchNoticeCommentDto create(Long branchNoticeId, Long branchId, Long userId, BranchNoticeCommentDto.RequestBranchNoticeCommentDto requestBranchNoticeCommentDto){
        Optional<Branch> branchOptional = branchRepository.findById(branchId);
        if (!branchOptional.isPresent()) {
            throw new NotFoundException("유효하지 않은 지점입니다.");
        }
        Branch branch = branchOptional.get();

        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new NotFoundException("존재하지 않은 회원입니다.");
        }
        User user = userOptional.get();

        Optional<BranchNotice> branchNoticeOptional = branchNoticeRepository.findById(branchNoticeId);
        if (!branchNoticeOptional.isPresent()) {
            throw new NotFoundException("존재하지 않는 공지사항 입니다.");
        }
        BranchNotice branchNotice = branchNoticeOptional.get();

        BranchNoticeComment branchNoticeComment = requestBranchNoticeCommentDto.toEntity(branchNotice, branch, user);
        branchNoticeCommentRepository.save(branchNoticeComment);

        return BranchNoticeCommentDto.ResponseBranchNoticeCommentDto.toDto(branchNoticeComment);
    }

    public BranchNoticeCommentDto.ResponseBranchNoticeCommentDto get(Long branchNoticeCommentId){
        Optional<BranchNoticeComment> branchNoticeCommentOptional = branchNoticeCommentRepository.findById(branchNoticeCommentId);

        if (!branchNoticeCommentOptional.isPresent()){
            throw new NotFoundException("존재하지 않는 댓글 입니다.");
        }

        BranchNoticeComment branchNoticeComment = branchNoticeCommentOptional.get();

        return BranchNoticeCommentDto.ResponseBranchNoticeCommentDto.toDto(branchNoticeComment);
    }

    @Transactional
    public BranchNoticeCommentDto.ResponseBranchNoticeCommentDto update(Long branchNoticeCommentId, BranchNoticeCommentDto.RequestBranchNoticeCommentDto requestBranchNoticeCommentDto){
        Optional<BranchNoticeComment> branchNoticeCommentOptional = branchNoticeCommentRepository.findById(branchNoticeCommentId);

        if (!branchNoticeCommentOptional.isPresent()){
            throw new NotFoundException("존재하지 않는 댓글 입니다.");
        }

        BranchNoticeComment branchNoticeComment = branchNoticeCommentOptional.get();

        branchNoticeComment.updateTitle(requestBranchNoticeCommentDto.getTitle());
        branchNoticeComment.updateContents(requestBranchNoticeCommentDto.getContents());

        branchNoticeCommentRepository.save(branchNoticeComment);

        return BranchNoticeCommentDto.ResponseBranchNoticeCommentDto.toDto(branchNoticeComment);
    }

    @Transactional
    public Long delete(Long branchNoticeCommentId){
        Optional<BranchNoticeComment> branchNoticeCommentOptional = branchNoticeCommentRepository.findById(branchNoticeCommentId);

        if (!branchNoticeCommentOptional.isPresent()){
            throw new NotFoundException("존재하지 않는 댓글 입니다.");
        }

        BranchNoticeComment branchNoticeComment = branchNoticeCommentOptional.get();
        branchNoticeComment.delete();

        return branchNoticeCommentRepository.save(branchNoticeComment).getId();
    }
}
