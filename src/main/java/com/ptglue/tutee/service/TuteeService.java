package com.ptglue.tutee.service;

import com.ptglue.basic.exception.InternalServerErrorException;
import com.ptglue.basic.exception.NotFoundException;
import com.ptglue.branch.enums.ConnectionTypeEnum;
import com.ptglue.branch.enums.StateTypeEnum;
import com.ptglue.branch.model.dto.BranchTuteeDto;
import com.ptglue.branch.model.entity.*;
import com.ptglue.branch.repository.*;
import com.ptglue.common.user.enums.UserRoleEnum;
import com.ptglue.common.user.model.entity.User;
import com.ptglue.common.user.repository.UserRepository;
import com.ptglue.ticket.model.dto.TicketDto;
import com.ptglue.ticket.service.TicketService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TuteeService {

    @Resource(name = "branchUserRoleRepository")
    BranchUserRoleRepository branchUserRoleRepository;

    @Resource(name = "branchRepository")
    BranchRepository branchRepository;

    @Resource(name = "userRepository")
    UserRepository userRepository;

    @Resource(name = "branchUserPermissionRepository")
    BranchUserPermissionRepository branchUserPermissionRepository;

    @Resource(name = "ticketService")
    TicketService ticketService;

    @Resource(name = "branchUserOngoingListRepository")
    BranchUserOngoingListRepository branchUserOngoingListRepository;

    @Resource(name = "branchUserEndListRepository")
    BranchUserEndListRepository branchUserEndListRepository;

    public Page<BranchTuteeDto.ResponseBranchTuteeDto> getList(StateTypeEnum stateType, Long branchId, String searchWord, Pageable pageable) {

        if (stateType == StateTypeEnum.ONGOING) {
            Page<BranchUserOngoingList> branchUserOngoingList = branchUserOngoingListRepository.findTuteeBySearchWord(branchId, searchWord, pageable);

            return branchUserOngoingList.map(branchUserOngoing -> BranchTuteeDto.ResponseBranchTuteeDto.toDto(branchUserOngoing));
        } else {
            Page<BranchUserEndList> branchUserEndList = branchUserEndListRepository.findEndTuteeBySearchWord(branchId, searchWord, pageable);
            return branchUserEndList.map(branchUserEnd -> BranchTuteeDto.ResponseBranchTuteeDto.toDto(branchUserEnd));
        }
    }

    @Transactional
    public BranchTuteeDto.ResponseBranchTuteeDto createBranchTutee(Long branchId, BranchTuteeDto.RequestBranchTuteeDto requestBranchTuteeDto) {
        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new NotFoundException("지점을 찾을 수 없습니다."));

        User user = userRepository.findById(requestBranchTuteeDto.getUserId()).orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        //한번 더 체크? 삭제?
        Optional<BranchUserRole> branchUserRoleOptional = branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, user.getId(), UserRoleEnum.TUTEE);
        if(branchUserRoleOptional.isPresent()) {
            BranchUserRole branchUserRole = branchUserRoleOptional.get();
            if(branchUserRole.getArchiveYn()){
                throw new InternalServerErrorException("보관함에 있습니다.");
            }
            throw new InternalServerErrorException("이미 등록되어 있습니다.");
        }

        BranchUserRole branchUserRole = requestBranchTuteeDto.toEntity(branch, user);
        branchUserRoleRepository.save(branchUserRole);

        branch.updateTuteeNumPlus();
        branchRepository.save(branch);

        //tutee도 권한 설정 필요함. 나중에 개별 설정이 추가될 수 있음
        List<BranchUserPermission> branchUserPermissionList = requestBranchTuteeDto.toEntity(branchUserRole);
        branchUserPermissionRepository.saveAll(branchUserPermissionList);
        return BranchTuteeDto.ResponseBranchTuteeDto.toDto(branchUserRole);
    }

    public BranchTuteeDto.ResponseBranchTuteeDto get(Long branchId, Long userId) {
        BranchUserRole branchUserRole = branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTEE)
                .orElseThrow(() -> new NotFoundException("지점 회원을 찾을 수 없습니다."));
        return BranchTuteeDto.ResponseBranchTuteeDto.toDto(branchUserRole);
    }

    public Page<TicketDto.ResponseTicketDto> getTicketList(Long branchId, Long userId, Pageable pageable){
        branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTEE)
                .orElseThrow(() -> new NotFoundException("지점 회원을 찾을 수 없습니다."));
        return ticketService.getList(branchId, userId, pageable);
    }

    @Transactional
    public BranchTuteeDto.ResponseBranchTuteeDto disconnectTutee (Long branchId, Long userId) {
        BranchUserRole branchUserRole = branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTEE)
                .orElseThrow(() -> new NotFoundException("지점 회원을 찾을 수 없습니다."));
        branchUserRole.updateConnectType(ConnectionTypeEnum.DISCONNECTED);

        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new NotFoundException("지점을 찾을 수 없습니다."));
        branch.updateTuteeNumMinus();
        branchRepository.save(branch);

        return BranchTuteeDto.ResponseBranchTuteeDto.toDto(branchUserRoleRepository.save(branchUserRole));
    }

    @Transactional
    public BranchTuteeDto.ResponseBranchTuteeDto reconnectTutee (Long branchId, Long userId) {
        BranchUserRole branchUserRole = branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTEE)
                .orElseThrow(() -> new NotFoundException("지점 회원을 찾을 수 없습니다."));
        branchUserRole.updateConnectType(ConnectionTypeEnum.WAIT);

        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new NotFoundException("지점을 찾을 수 없습니다."));
        branch.updateTuteeNumPlus();
        branchRepository.save(branch);
        //TODO : 알림 보내는 방법 확인
        return BranchTuteeDto.ResponseBranchTuteeDto.toDto(branchUserRoleRepository.save(branchUserRole));
    }
}