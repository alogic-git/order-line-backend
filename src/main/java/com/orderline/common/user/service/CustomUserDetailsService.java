package com.orderline.common.user.service;

import com.orderline.common.user.repository.UserRepository;
import com.orderline.basic.exception.NotFoundException;
import com.orderline.branch.enums.ReservationTypeEnum;
import com.orderline.branch.model.entity.Branch;
import com.orderline.branch.model.entity.BranchUserRole;
import com.orderline.common.user.model.entity.User;
import com.orderline.branch.repository.BranchUserRoleRepository;
import com.orderline.common.user.model.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Resource(name = "userRepository")
    UserRepository userRepository;

    @Resource(name = "branchUserRoleRepository")
    BranchUserRoleRepository branchUserRoleRepository;

    public UserDetails loadUserByUsername(String userId) {

        UserDto.RoleDto userRole = getUserRoleById(Long.valueOf(userId));
        return new CustomUserDetails(userRole);
    }

    public UserDto.RoleDto getUserRoleById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<BranchUserRole> branchUserRoleList = branchUserRoleRepository.findByUserIdAndArchiveYnAndBranch_DeleteYnAndBranch_ArchiveYn(userId, false, false, false);
            Optional<Branch> lastSelectedBranchOptional = branchUserRoleList.stream().filter(branchUserRole -> branchUserRole.getLastViewYn() != null && branchUserRole.getLastViewYn()).map(BranchUserRole::getBranch).findFirst();
            Long branchId = 0L;
            ReservationTypeEnum reservationType = null;
            if (lastSelectedBranchOptional.isPresent()) {
                Branch lastSelectedBranch = lastSelectedBranchOptional.get();
                branchId = lastSelectedBranch.getId();
                reservationType = lastSelectedBranch.getReservationType();
            }
            Long branchCount = branchUserRoleList
                    .stream().filter(branchUserRole -> branchUserRole.getBranch() != null).count();
            return UserDto.RoleDto.builder()
                    .id(userId)
                    .password(user.getPassword())
                    .roleType(user.getLastLoginRoleType())
                    .branchId(branchId)
                    .branchCount(branchCount)
                    .reservationType(reservationType)
                    .build();
        }
        throw new NotFoundException("사용자 정보가 없습니다.");
    }
}