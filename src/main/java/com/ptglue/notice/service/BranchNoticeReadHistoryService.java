package com.ptglue.notice.service;

import com.ptglue.basic.exception.NotFoundException;
import com.ptglue.branch.model.entity.Branch;
import com.ptglue.branch.repository.BranchRepository;
import com.ptglue.common.user.model.entity.User;
import com.ptglue.common.user.repository.UserRepository;
import com.ptglue.notice.model.entity.BranchNotice;
import com.ptglue.notice.model.entity.BranchNoticeReadHistory;
import com.ptglue.notice.repository.BranchNoticeReadHistoryRepository;
import com.ptglue.notice.repository.BranchNoticeRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class BranchNoticeReadHistoryService {
    @Resource(name = "branchNoticeReadHistoryRepository")
    BranchNoticeReadHistoryRepository branchNoticeReadHistoryRepository;

    @Resource(name = "branchRepository")
    BranchRepository branchRepository;

    @Resource(name = "branchNoticeRepository")
    BranchNoticeRepository branchNoticeRepository;

    @Resource(name = "userRepository")
    UserRepository userRepository;


    public Boolean isExist(Long branchNoticeId, Long userId){
        return branchNoticeReadHistoryRepository.existsByBranchNoticeIdAndUserId(branchNoticeId, userId);
    }

    public void create(Long branchNoticeId, Long branchId, Long userId){
        Optional<BranchNotice> branchNoticeOptional = branchNoticeRepository.findById(branchNoticeId);

        if (!branchNoticeOptional.isPresent()){
            throw new NotFoundException("존재하지 않는 공지입니다.");
        }

        Optional<Branch> branchOptional = branchRepository.findById(branchId);
        if (!branchOptional.isPresent()) {
            throw new NotFoundException("유효하지 않은 지점입니다.");
        }

        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new NotFoundException("존재하지 않은 회원입니다.");
        }

        BranchNoticeReadHistory branchNoticeReadHistory = new BranchNoticeReadHistory();

        BranchNotice branchNotice= branchNoticeOptional.get();
        Branch branch = branchOptional.get();
        User user = userOptional.get();

        branchNoticeReadHistoryRepository.save(branchNoticeReadHistory.toEntity(branchNotice, branch, user));
    }
}
