package com.orderline.branch.service;

import com.orderline.branch.model.dto.BranchUserDto;
import com.orderline.branch.repository.BranchUserPermissionRepository;
import com.orderline.branch.repository.BranchUserRoleRepository;
import com.orderline.common.user.enums.UserRoleEnum;
import com.orderline.common.user.model.dto.UserDto;
import com.orderline.common.user.model.entity.User;
import com.orderline.common.user.repository.UserRepository;
import com.orderline.common.user.repository.UserTokenRepository;
import com.orderline.common.user.service.CustomUserDetailsService;
import com.orderline.basic.config.security.JwtTokenProvider;
import com.orderline.basic.exception.NotFoundException;
import com.orderline.branch.enums.ConnectionTypeEnum;
import com.orderline.branch.enums.FunctionTypeEnum;
import com.orderline.branch.enums.PermissionTypeEnum;
import com.orderline.branch.model.entity.BranchUserPermission;
import com.orderline.branch.repository.BranchRepository;
import com.orderline.branch.model.entity.BranchUserRole;
import com.orderline.branch.model.dto.BranchDto;
import com.orderline.branch.model.entity.Branch;
import com.orderline.tutor.service.TutorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class BranchService {

    @Resource(name = "branchRepository")
    BranchRepository branchRepository;

    @Resource(name = "branchUserRoleRepository")
    BranchUserRoleRepository branchUserRoleRepository;

    @Resource(name = "userRepository")
    UserRepository userRepository;

    @Resource(name = "branchUserPermissionRepository")
    BranchUserPermissionRepository branchUserPermissionRepository;

    @Resource(name = "customUserDetailsService")
    CustomUserDetailsService customUserDetailsService;

    @Resource(name = "jwtTokenProvider")
    JwtTokenProvider jwtTokenProvider;

    @Resource(name = "userTokenRepository")
    UserTokenRepository userTokenRepository;

    @Resource(name = "tutorService")
    TutorService tutorService;

    public BranchDto.ResponseBranchDto get(Long branchId) {

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new NotFoundException("지점이 존재하지 않습니다."));
        return BranchDto.ResponseBranchDto.toDto(branch);
    }

    public Page<BranchDto.ResponseBranchDto> getList(Long userId, Pageable pageable) {

        Page<BranchUserRole> branchRoles = branchUserRoleRepository.findBranchListNativeQuery(userId, pageable);
        return branchRoles.map(BranchDto.ResponseBranchDto::toDto);
    }

    @Transactional
    public UserDto.UserInfoDto select(Long userId, Long branchUserRoleId) {

        //사용자 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("사용자가 존재하지 않습니다."));

        List<BranchUserRole> branchUserList = branchUserRoleRepository.findByUserId(userId);
        branchUserList
                .stream().filter(branchUserRole -> branchUserRole.getBranch() != null)
                .forEach(branchUser -> branchUser.updateLastViewYn(branchUser.getId().equals(branchUserRoleId)));
        branchUserRoleRepository.saveAll(branchUserList);

        //사용자의 마지막 로그인 지점의 권한을 가져온다.
        BranchUserRole branchUserRole = branchUserList.stream().filter(branchUser -> branchUser.getLastViewYn()).findFirst()
                .orElseThrow(() -> new NotFoundException("지점 강사가 존재하지 않습니다."));

        user.updateLastLoginRoleType(branchUserRole.getRoleType());
        userRepository.save(user);
        return UserDto.UserInfoDto.toDto(user);
    }

    @Transactional
    public void delete(Long branchId) {

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new NotFoundException("지점이 존재하지 않습니다."));

        branch.deleteBranch();
        // TBD : 지점 삭제 시 지점에 소속된 사용자들의 연결 해제하는 기능이 추가될 수도 있음
        branchRepository.save(branch);
        List<BranchUserRole> branchUserRoleList = branchUserRoleRepository.findByBranchId(branchId);
        branchUserRoleList.forEach(branchUserRole -> branchUserRole.updateLastViewYn(false));
        branchUserRoleRepository.saveAll(branchUserRoleList);
    }

    @Transactional
    public BranchDto.ResponseBranchDto update(Long branchId, BranchDto.RequestBranchDto requestBranchDto) {

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new NotFoundException("지점이 존재하지 않습니다."));

        branch.updateBranch(requestBranchDto);
        branch = branchRepository.save(branch);
        return BranchDto.ResponseBranchDto.toDto(branch);
    }

    @Transactional
    public BranchDto.ResponseBranchDto create(Long userId, BranchDto.RequestBranchDto requestBranchDto) {

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("사용자가 존재하지 않습니다."));

        Branch branch = branchRepository.save(requestBranchDto.toEntity(user));
        //지점 추가 시 지점 소유자도 권한은 모두 다 가지는 지점관리자 역할로 추가,
        BranchUserRole branchUserRole = BranchUserRole.builder()
                .branch(branch)
                .user(user)
                .roleType(UserRoleEnum.MANAGER)
                .connectionType(ConnectionTypeEnum.CONNECTED)
                .build();
        branchUserRoleRepository.save(branchUserRole);
        List<BranchUserPermission> branchUserPermissionList = new ArrayList<>();
        for (FunctionTypeEnum functionEnum : FunctionTypeEnum.values()) {
            branchUserPermissionList.add(BranchUserPermission.builder()
                    .branch(branch)
                    .user(user)
                    .branchUserRole(branchUserRole)
                    .functionType(functionEnum)
                    .permissionType(PermissionTypeEnum.EDIT)
                    .build());
        }
        branchUserPermissionRepository.saveAll(branchUserPermissionList);
        return BranchDto.ResponseBranchDto.toDto(branch);
    }

    @Transactional
    public BranchDto.ResponseBranchDto archive(Long branchId) {

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new NotFoundException("지점이 존재하지 않습니다."));

        branch.archiveBranch();
        branchRepository.save(branch);
        // TBD : 지점 보관 시 지점에 소속된 사용자들의 연결 해제하는 기능이 추가될 수도 있음
        List<BranchUserRole> branchUserRoleList = branchUserRoleRepository.findByBranchId(branchId);
        branchUserRoleList.forEach(branchUserRole -> branchUserRole.updateLastViewYn(false));
        branchUserRoleRepository.saveAll(branchUserRoleList);
        return BranchDto.ResponseBranchDto.toDto(branch);
    }

    @Transactional
    public BranchUserDto.ResponseBranchUserDto disconnectBranch (Long branchId, Long userId, UserRoleEnum roleType) {

        BranchUserRole branchUserRole = branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, userId, roleType)
                .orElseThrow(() -> new NotFoundException("지점 강사를 찾을 수 없습니다."));

        branchUserRole.updateConnectType(ConnectionTypeEnum.DISCONNECTED);
        return BranchUserDto.ResponseBranchUserDto.toDto(branchUserRoleRepository.save(branchUserRole));
    }

    public List<BranchUserDto.ResponseBranchUserPermissionDto> getMyPermission(Long branchId, Long userId, UserRoleEnum roleType) {

        BranchUserRole branchUserRole = branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, userId, roleType)
                .orElseThrow(() -> new NotFoundException("지점 강사를 찾을 수 없습니다."));

        List<BranchUserPermission> branchUserPermissionList = branchUserPermissionRepository.findByBranchUserRoleId(branchUserRole.getId());
        return tutorService.getPermissionAllList(branchUserPermissionList);
    }

    public BranchDto.ResponseBranchSettingDto getBranchSetting(Long branchId) {

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new NotFoundException("지점이 존재하지 않습니다."));

        return BranchDto.ResponseBranchSettingDto.toDto(branch);
    }

    public BranchDto.ResponseBranchSettingDto updateBranchSetting(Long branchId, BranchDto.RequestBranchSettingDto requestBranchSettingDto){

        Branch branch = branchRepository.findById(branchId)
                        .orElseThrow(() -> new NotFoundException("지점이 존재하지 않습니다."));
        branch.updateBranchSetting(requestBranchSettingDto);
        branch = branchRepository.save(branch);

        return BranchDto.ResponseBranchSettingDto.toDto(branch);
    }
}
