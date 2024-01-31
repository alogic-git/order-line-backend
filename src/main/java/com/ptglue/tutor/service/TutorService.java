package com.ptglue.tutor.service;

import com.ptglue.basic.exception.InternalServerErrorException;
import com.ptglue.basic.exception.NotFoundException;
import com.ptglue.branch.enums.ConnectionTypeEnum;
import com.ptglue.branch.enums.FunctionTypeEnum;
import com.ptglue.branch.enums.PermissionTypeEnum;
import com.ptglue.branch.model.entity.Branch;
import com.ptglue.branch.model.entity.BranchUserPermission;
import com.ptglue.branch.model.entity.BranchUserRole;
import com.ptglue.branch.repository.BranchRepository;
import com.ptglue.branch.repository.BranchUserPermissionRepository;
import com.ptglue.branch.repository.BranchUserRoleRepository;
import com.ptglue.common.user.enums.UserRoleEnum;
import com.ptglue.common.user.model.dto.UserDto;
import com.ptglue.common.user.model.entity.User;
import com.ptglue.common.user.repository.UserRepository;
import com.ptglue.branch.model.dto.BranchUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TutorService {

    @Resource(name = "branchUserRoleRepository")
    BranchUserRoleRepository branchUserRoleRepository;

    @Resource(name = "branchUserPermissionRepository")
    BranchUserPermissionRepository branchUserPermissionRepository;

    @Resource(name = "userRepository")
    UserRepository userRepository;

    @Resource(name = "branchRepository")
    BranchRepository branchRepository;

    public BranchUserDto.ResponseBranchUserDto get(Long branchId, Long userId) {
        BranchUserRole branchUser = branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR)
                .orElseThrow(() -> new NotFoundException("지점 강사를 찾을 수 없습니다."));
        return BranchUserDto.ResponseBranchUserDto.toDto(branchUser);
    }

    public Page<BranchUserDto.ResponseBranchUserDto> getList(Long branchId, String searchWord, Pageable pageable) {

        Page<BranchUserRole> branchUserRoles = branchUserRoleRepository.findTutorNativeQuery(branchId, searchWord, pageable);
        return branchUserRoles.map(BranchUserDto.ResponseBranchUserDto::toDto);
    }

    @Transactional
    public BranchUserDto.ResponseBranchUserDto archive(Long branchId, Long userId) {
        // TBD 관련 정보 처리 기능 구현
        BranchUserRole branchUserRole = branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR)
                .orElseThrow(() -> new NotFoundException("지점 강사를 찾을 수 없습니다."));
        branchUserRole.updateLastViewYn(false);
        branchUserRole.updateArchive(true);
        return BranchUserDto.ResponseBranchUserDto.toDto(branchUserRoleRepository.save(branchUserRole));
    }

    @Transactional
    public BranchUserDto.ResponseBranchUserDto updateNickname(Long branchId, Long userId, BranchUserDto.RequestUpdateNickname requestUpdateNickname){
        BranchUserRole branchUserRole = branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR)
                .orElseThrow(() -> new NotFoundException("지점 강사를 찾을 수 없습니다."));
        branchUserRole.updateNickname(requestUpdateNickname.getNickname());
        branchUserRoleRepository.save(branchUserRole);
        return BranchUserDto.ResponseBranchUserDto.toDto(branchUserRole);
    }

    public UserDto.ResponseUser getBranchUser(Long branchId, UserRoleEnum role, String phone) {

        List<BranchUserRole> branchUserRoleList = branchUserRoleRepository.findByBranchIdAndUser_Phone(branchId, phone);
        //현재 선택 지점에 등록된 고객인지 조회
        if(branchUserRoleList.isEmpty()){
            throw new NotFoundException("지점에 없는 고객 입니다.");
        }

        /*1. 글루에 가입이 안된경우 - exception
        2. 가입은 돼있고, 지점에 등록이 안된경우 - 회원정보 + , branchid 비어있음 - 중요정보 마스킹
        3. 가입도 돼있고, 지점에 등록도 됐고, 다른 role이 있음 - 회원정보 +, branchid 있음 - 중요정보 마스킹 필요없음
        4. 가입도 돼있고, 지점에 등록도 됐고, 같은 role이 있음 - exception
         */


        //등록하고자 하는 role로 해당 지점에 있는지 확인
        Optional<BranchUserRole> branchUserRoleOptional = branchUserRoleList.stream().filter(branchUserRole -> branchUserRole.getRoleType() == role).findFirst();
        if (branchUserRoleOptional.isPresent()) {
            BranchUserRole branchUserRole = branchUserRoleOptional.get();
            if(branchUserRole.getArchiveYn()){
                throw new InternalServerErrorException("보관함에 있습니다.");
            }
            throw new InternalServerErrorException("이미 등록되어 있습니다.");
        }

        // 없으면 정보 반환
        User user = branchUserRoleList.get(0).getUser();
        return UserDto.ResponseUser.toDto(user);
    }

    @Transactional
    public BranchUserDto.ResponseBranchUserDto createBranchUser(Long branchId, BranchUserDto.RequestBranchUserDto requestBranchUserDto) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new NotFoundException("지점을 찾을 수 없습니다."));

        User user = userRepository.findById(requestBranchUserDto.getUserId())
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        //한번 더 체크? 삭제?
        Optional<BranchUserRole> branchUserRoleOptional = branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, user.getId(), requestBranchUserDto.getRoleType());
        if(branchUserRoleOptional.isPresent()) {
            BranchUserRole branchUserRole = branchUserRoleOptional.get();
            if(branchUserRole.getArchiveYn()){
                throw new InternalServerErrorException("보관함에 있습니다.");
            }
            throw new InternalServerErrorException("이미 등록되어 있습니다.");
        }

        //지점 유저 등록
        BranchUserRole branchUserRole = requestBranchUserDto.toEntity(branch, user);
        branchUserRoleRepository.save(branchUserRole);

        branch.updateTutorNumPlus();
        branchRepository.save(branch);

        List<BranchUserPermission> branchUserPermissionList = requestBranchUserDto.toEntity(branchUserRole);
        branchUserPermissionRepository.saveAll(branchUserPermissionList);
        return BranchUserDto.ResponseBranchUserDto.toDto(branchUserRole);
    }

    @Transactional
    public List<BranchUserDto.ResponseBranchUserPermissionDto> updatePermission(
            Long branchId,
            Long userId,
            List<BranchUserDto.RequestBranchUserPermissionDto> requestBranchUserPermissionDtoList){
        BranchUserRole branchUserRole = branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR)
                .orElseThrow(() -> new NotFoundException("지점 강사를 찾을 수 없습니다."));
        //기존 권한 list 찾아서 다 삭제
        List<BranchUserPermission> branchUserPermissionList = branchUserPermissionRepository.findByBranchUserRoleId(branchUserRole.getId());
        branchUserPermissionList.forEach(BranchUserPermission::deletePermission);
        branchUserPermissionRepository.saveAll(branchUserPermissionList);

        //새로 등록
        List<BranchUserPermission> newPermisstionList = new ArrayList<>();
        requestBranchUserPermissionDtoList.forEach(requestBranchUserPermissionDto -> {
            BranchUserPermission branchUserPermission = requestBranchUserPermissionDto.toEntity(branchUserRole);
            newPermisstionList.add(branchUserPermission);
        });

        branchUserPermissionRepository.saveAll(newPermisstionList);
        return getPermissionAllList(newPermisstionList);
    }

    public List<BranchUserDto.ResponseBranchUserPermissionDto> getUserPermission(Long branchId, Long userId){
        BranchUserRole branchUserRole = branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR)
                .orElseThrow(() -> new NotFoundException("지점 강사를 찾을 수 없습니다."));
        List<BranchUserPermission> branchUserPermissionList = branchUserPermissionRepository.findByBranchUserRoleId(branchUserRole.getId());
        return getPermissionAllList(branchUserPermissionList);
    }

    public List<BranchUserDto.ResponseBranchUserPermissionDto> getPermissionAllList(List<BranchUserPermission> branchUserPermissionList) {

        //List에 없는 경우 NONE으로 추가
        List<FunctionTypeEnum> functionTypeEnumList = Arrays.asList(FunctionTypeEnum.values());
        functionTypeEnumList.forEach(functionTypeEnum -> {
            Optional<BranchUserPermission> branchUserPermissionOptional = branchUserPermissionList.stream()
                    .filter(branchUserPermission -> branchUserPermission.getFunctionType().equals(functionTypeEnum))
                    .findFirst();
            if(!branchUserPermissionOptional.isPresent()){
                BranchUserPermission branchUserPermission = BranchUserPermission.builder()
                        .functionType(functionTypeEnum)
                        .permissionType(PermissionTypeEnum.NONE)
                        .build();
                branchUserPermissionList.add(branchUserPermission);
            }
        });

        return branchUserPermissionList.stream().map(BranchUserDto.ResponseBranchUserPermissionDto::toDto)
                .sorted(Comparator.comparing(BranchUserDto.ResponseBranchUserPermissionDto::getFunctionType))
                .collect(Collectors.toList());
    }

    @Transactional
    public BranchUserDto.ResponseBranchUserDto disconnectUser (Long branchId, Long userId) {
        BranchUserRole branchUserRole = branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR)
                .orElseThrow(() -> new NotFoundException("지점 강사를 찾을 수 없습니다."));
        branchUserRole.updateConnectType(ConnectionTypeEnum.DISCONNECTED);

        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new NotFoundException("지점을 찾을 수 없습니다."));
        branch.updateTuteeNumMinus();
        branchRepository.save(branch);
        return BranchUserDto.ResponseBranchUserDto.toDto(branchUserRoleRepository.save(branchUserRole));
    }

    @Transactional
    public BranchUserDto.ResponseBranchUserDto reconnectUser (Long branchId, Long userId) {

        BranchUserRole branchUserRole = branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR)
                .orElseThrow(() -> new NotFoundException("지점 강사를 찾을 수 없습니다."));
        branchUserRole.updateConnectType(ConnectionTypeEnum.WAIT);

        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new NotFoundException("지점을 찾을 수 없습니다."));
        branch.updateTuteeNumPlus();
        branchRepository.save(branch);
        //TODO : 알림 보내는 방법 확인
        return BranchUserDto.ResponseBranchUserDto.toDto(branchUserRoleRepository.save(branchUserRole));
    }
}
