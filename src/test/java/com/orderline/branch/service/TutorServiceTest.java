package com.orderline.branch.service;


import com.orderline.basic.exception.InternalServerErrorException;
import com.orderline.basic.exception.NotFoundException;
import com.orderline.branch.enums.ConnectionTypeEnum;
import com.orderline.branch.enums.FunctionTypeEnum;
import com.orderline.branch.enums.PermissionTypeEnum;
import com.orderline.branch.model.dto.BranchUserDto;
import com.orderline.branch.model.entity.Branch;
import com.orderline.branch.model.entity.BranchUserPermission;
import com.orderline.branch.model.entity.BranchUserRole;
import com.orderline.branch.repository.BranchRepository;
import com.orderline.branch.repository.BranchUserPermissionRepository;
import com.orderline.branch.repository.BranchUserRoleRepository;
import com.orderline.common.user.enums.UserRoleEnum;
import com.orderline.common.user.model.entity.User;
import com.orderline.common.user.repository.UserRepository;
import com.orderline.tutor.service.TutorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TutorServiceTest {

    @InjectMocks
    private TutorService tutorService;

    @Mock
    UserRepository userRepository;

    @Mock
    BranchRepository branchRepository;

    @Mock
    BranchUserRoleRepository branchUserRoleRepository;

    @Mock
    BranchUserPermissionRepository branchUserPermissionRepository;

    public User makeUser() {
        return User.builder()
                .id(18578L)
                .phoneActiveYn(true)
                .build();
    }

    public Branch makeBranch(){
        return Branch.builder()
                .id(1574L)
                .branchName("test")
                .build();
    }

    public BranchUserRole makeBranchUserRole(){
        return BranchUserRole.builder()
                .id(234L)
                .user(makeUser())
                .branch(makeBranch())
                .connectionType(ConnectionTypeEnum.CONNECTED)
                .build();
    }

    public BranchUserRole makeBranchUserRole(Long id){
        return BranchUserRole.builder()
                .id(id)
                .user(makeUser())
                .branch(makeBranch())
                .connectionType(ConnectionTypeEnum.CONNECTED)
                .build();
    }

    public BranchUserRole makeBranchUserRole(Long id, Boolean activeYn){
        return BranchUserRole.builder()
                .id(id)
                .branch(makeBranch())
                .archiveYn(activeYn)
                .build();
    }

    public List<BranchUserPermission> makePermissionList(BranchUserRole branchUserRole){
        List<BranchUserPermission> permissionList = new ArrayList<>();
        permissionList.add(BranchUserPermission.builder()
                .id(1L)
                .branchUserRole(branchUserRole)
                .functionType(FunctionTypeEnum.EXTRAPRODUCT)
                .permissionType(PermissionTypeEnum.EDIT)
                .build());
        permissionList.add(BranchUserPermission.builder()
                .id(2L)
                .branchUserRole(branchUserRole)
                .functionType(FunctionTypeEnum.NOTICE)
                .permissionType(PermissionTypeEnum.VIEW)
                .build());
        return permissionList;
    }

    @Nested
    @DisplayName("get test")
    class getTest{

        @Test
        @DisplayName("ok test")
        void okTest() {
            //given
            BranchUserRole branchUserRole = makeBranchUserRole();
            BranchUserDto.ResponseBranchUserDto expectedBranchDto = BranchUserDto.ResponseBranchUserDto.toDto(branchUserRole);
            Long branchId = branchUserRole.getBranch().getId();
            Long userId = branchUserRole.getUser().getId();
            Long branchUserRoleId = branchUserRole.getId();
            given(branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR)).willReturn(Optional.of(branchUserRole));
            //when
            BranchUserDto.ResponseBranchUserDto actualBranchDto = tutorService.get(branchId, userId);
            //then
            assertEquals(branchUserRoleId, actualBranchDto.getBranchUserRoleId());
            assertThat(actualBranchDto).usingRecursiveComparison().isEqualTo(expectedBranchDto);
            verify(branchUserRoleRepository, times(1)).findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR);
            verifyNoMoreInteractions(branchUserRoleRepository);
        }

        @Test
        @DisplayName("notFoundException test")
        void notFoundException() {
            //given
            BranchUserRole branchUserRole = makeBranchUserRole();
            Long branchId = branchUserRole.getBranch().getId();
            Long userId = branchUserRole.getUser().getId();
            given(branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR)).willReturn(Optional.empty());
            //when
            NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> tutorService.get(branchId, userId));
            //then
            assertEquals("지점 강사를 찾을 수 없습니다.", notFoundException.getMessage());
            verify(branchUserRoleRepository, times(1)).findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR);
            verifyNoMoreInteractions(branchUserRoleRepository);
        }
    }

    @Nested
    @DisplayName("getList test")
    class getList{

        @Test
        @DisplayName("ok test")
        void okTest(){
            //given
            BranchUserRole branchUserRole1 = makeBranchUserRole(1L);
            BranchUserRole branchUserRole2 = makeBranchUserRole(2L);
            List<BranchUserRole> branchUserRoleList = Arrays.asList(branchUserRole1, branchUserRole2);
            Pageable pageable = PageRequest.of(0, 10);
            Page<BranchUserRole> expectedBranchUserRolePage = new PageImpl<>(branchUserRoleList, pageable, branchUserRoleList.size());
            Long branchId = branchUserRole1.getBranch().getId();
            String searchWord = "%";
            given(branchUserRoleRepository.findTutorNativeQuery(branchId, searchWord, pageable)).willReturn(expectedBranchUserRolePage);
            //when
            Page<BranchUserDto.ResponseBranchUserDto> actualBranchUserDtoPage = tutorService.getList(branchId, searchWord, pageable);
            //then
            assertThat(actualBranchUserDtoPage.getTotalElements()).usingRecursiveComparison().isEqualTo(expectedBranchUserRolePage.getTotalElements());
            verify(branchUserRoleRepository, times(1)).findTutorNativeQuery(branchId, searchWord, pageable);
            verifyNoMoreInteractions(branchUserRoleRepository);
        }
    }

    @Nested
    @DisplayName("archive test")
    class archive{

        @Test
        @DisplayName("ok test")
        void okTest(){
            //given
            BranchUserRole branchUserRole = makeBranchUserRole();
            Long branchId = branchUserRole.getBranch().getId();
            Long userId = branchUserRole.getUser().getId();
            given(branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR)).willReturn(Optional.of(branchUserRole));
            given(branchUserRoleRepository.save(branchUserRole)).willReturn(branchUserRole);
            //when
            BranchUserDto.ResponseBranchUserDto actualBranchDto = tutorService.archive(branchId, userId);
            //then
            assertTrue(actualBranchDto.getArchiveYn());
            verify(branchUserRoleRepository, times(1)).findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR);
            verify(branchUserRoleRepository, times(1)).save(branchUserRole);
            verifyNoMoreInteractions(branchUserRoleRepository);
        }

        @Test
        @DisplayName("notFoundException test")
        void notFoundException(){
            //given
            BranchUserRole branchUserRole = makeBranchUserRole();
            Long branchId = branchUserRole.getBranch().getId();
            Long userId = branchUserRole.getUser().getId();
            given(branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR)).willReturn(Optional.empty());
            //when
            NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> tutorService.archive(branchId, userId));
            //then
            assertEquals("지점 강사를 찾을 수 없습니다.", notFoundException.getMessage());
            verify(branchUserRoleRepository, times(1)).findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR);
            verifyNoMoreInteractions(branchUserRoleRepository);
        }
    }

    @Nested
    @DisplayName("update test")
    class update{

        @Test
        @DisplayName("ok test")
        void okTest(){
            //given
            BranchUserRole branchUserRole = makeBranchUserRole();
            BranchUserDto.RequestUpdateNickname updateBranchUserRoleDto = BranchUserDto.RequestUpdateNickname.builder()
                    .nickname("updateNickname")
                    .build();
            Long branchId = branchUserRole.getBranch().getId();
            Long userId = branchUserRole.getUser().getId();
            given(branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR)).willReturn(Optional.of(branchUserRole));
            //when
            BranchUserDto.ResponseBranchUserDto actualBranchDto = tutorService.updateNickname(branchId, userId, updateBranchUserRoleDto);
            //then
            assertEquals(updateBranchUserRoleDto.getNickname(), actualBranchDto.getNickname());
            verify(branchUserRoleRepository, times(1)).findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR);
            verify(branchUserRoleRepository, times(1)).save(branchUserRole);
            verifyNoMoreInteractions(branchUserRoleRepository);
        }

        @Test
        @DisplayName("notFoundException test")
        void notFoundException(){
            //given
            BranchUserRole branchUserRole = makeBranchUserRole();
            BranchUserDto.RequestUpdateNickname updateBranchUserRoleDto = BranchUserDto.RequestUpdateNickname.builder()
                    .nickname("updateNickname")
                    .build();
            Long branchId = branchUserRole.getBranch().getId();
            Long userId = branchUserRole.getUser().getId();
            given(branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR)).willReturn(Optional.of(branchUserRole));
            //when
            BranchUserDto.ResponseBranchUserDto actualBranchDto = tutorService.updateNickname(branchId, userId, updateBranchUserRoleDto);
            //then
            assertEquals(updateBranchUserRoleDto.getNickname(), actualBranchDto.getNickname());
            verify(branchUserRoleRepository, times(1)).findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR);
            verify(branchUserRoleRepository, times(1)).save(branchUserRole);
            verifyNoMoreInteractions(branchUserRoleRepository);
        }
    }

    @Nested
    @DisplayName("createBranchUser test")
    class createBranchUser{

        @Test
        @DisplayName("ok test")
        void okTest(){
            //given
            User user = makeUser();
            Branch branch = makeBranch();
            Long branchId = branch.getId();
            BranchUserDto.RequestBranchUserDto requestBranchUserDto = BranchUserDto.RequestBranchUserDto.builder()
                    .userId(1L)
                    .nickname("nickname")
                    .roleType(UserRoleEnum.TUTOR)
                    .permissionDtoList(Arrays.asList(
                            BranchUserDto.RequestBranchUserPermissionDto.builder()
                                    .functionType(FunctionTypeEnum.KLASS)
                                    .permissionType(PermissionTypeEnum.EDIT)
                                    .build(),
                            BranchUserDto.RequestBranchUserPermissionDto.builder()
                                    .functionType(FunctionTypeEnum.SCHEDULE)
                                    .permissionType(PermissionTypeEnum.VIEW)
                                    .build()
                    ))
                    .build();
            given(branchRepository.findById(branchId)).willReturn(Optional.of(branch));
            given(userRepository.findById(requestBranchUserDto.getUserId())).willReturn(Optional.of(user));
            given(branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, user.getId(), requestBranchUserDto.getRoleType())).willReturn(Optional.empty());
            //when
            BranchUserDto.ResponseBranchUserDto actualBranchDto = tutorService.createBranchUser(branchId, requestBranchUserDto);
            //then
            assertEquals(requestBranchUserDto.getNickname(), actualBranchDto.getNickname());
            assertEquals(requestBranchUserDto.getRoleType(), actualBranchDto.getRoleType());
            verify(branchRepository, times(1)).findById(branchId);
            verify(userRepository, times(1)).findById(requestBranchUserDto.getUserId());
            verify(branchUserRoleRepository, times(1)).findByBranchIdAndUserIdAndRoleType(branchId, user.getId(), requestBranchUserDto.getRoleType());
            verify(branchUserRoleRepository, times(1)).save(any(BranchUserRole.class));
            verify(branchUserPermissionRepository, times(1)).saveAll(anyList());
            verifyNoMoreInteractions(branchRepository);
            verifyNoMoreInteractions(userRepository);
            verifyNoMoreInteractions(branchUserRoleRepository);
            verifyNoMoreInteractions(branchUserPermissionRepository);
        }

        @Test
        @DisplayName("notFoundException_branch test")
        void notFoundExceptionBranchTest(){
            //given
            User user = makeUser();
            Branch branch = makeBranch();
            Long branchId = branch.getId();
            BranchUserDto.RequestBranchUserDto requestBranchUserDto = BranchUserDto.RequestBranchUserDto.builder()
                    .userId(1L)
                    .nickname("nickname")
                    .roleType(UserRoleEnum.TUTOR)
                    .permissionDtoList(Arrays.asList(
                            BranchUserDto.RequestBranchUserPermissionDto.builder()
                                    .functionType(FunctionTypeEnum.KLASS)
                                    .permissionType(PermissionTypeEnum.EDIT)
                                    .build(),
                            BranchUserDto.RequestBranchUserPermissionDto.builder()
                                    .functionType(FunctionTypeEnum.SCHEDULE)
                                    .permissionType(PermissionTypeEnum.VIEW)
                                    .build()
                    ))
                    .build();
            given(branchRepository.findById(branchId)).willReturn(Optional.empty());
            //when
            NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> tutorService.createBranchUser(branchId, requestBranchUserDto));
            //then
            assertEquals("지점을 찾을 수 없습니다.", notFoundException.getMessage());
            verify(branchRepository, times(1)).findById(branchId);
            verify(userRepository, never()).findById(requestBranchUserDto.getUserId());
            verify(branchUserRoleRepository, never()).findByBranchIdAndUserIdAndRoleType(branchId, user.getId(), requestBranchUserDto.getRoleType());
            verify(branchUserRoleRepository, never()).save(any(BranchUserRole.class));
            verify(branchUserPermissionRepository, never()).saveAll(anyList());
            verifyNoMoreInteractions(branchRepository);
            verifyNoInteractions(userRepository);
            verifyNoInteractions(branchUserRoleRepository);
            verifyNoInteractions(branchUserPermissionRepository);
        }

        @Test
        @DisplayName("notFoundException_user test")
        void notFoundExceptionUserTest(){
            //given
            User user = makeUser();
            Branch branch = makeBranch();
            Long branchId = branch.getId();
            BranchUserDto.RequestBranchUserDto requestBranchUserDto = BranchUserDto.RequestBranchUserDto.builder()
                    .userId(1L)
                    .nickname("nickname")
                    .roleType(UserRoleEnum.TUTOR)
                    .permissionDtoList(Arrays.asList(
                            BranchUserDto.RequestBranchUserPermissionDto.builder()
                                    .functionType(FunctionTypeEnum.KLASS)
                                    .permissionType(PermissionTypeEnum.EDIT)
                                    .build(),
                            BranchUserDto.RequestBranchUserPermissionDto.builder()
                                    .functionType(FunctionTypeEnum.SCHEDULE)
                                    .permissionType(PermissionTypeEnum.VIEW)
                                    .build()
                    ))
                    .build();
            given(branchRepository.findById(branchId)).willReturn(Optional.of((branch)));
            given(userRepository.findById(requestBranchUserDto.getUserId())).willReturn(Optional.empty());
            //when
            NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> tutorService.createBranchUser(branchId, requestBranchUserDto));
            //then
            assertEquals("회원을 찾을 수 없습니다.", notFoundException.getMessage());
            verify(branchRepository, times(1)).findById(branchId);
            verify(userRepository, times(1)).findById(requestBranchUserDto.getUserId());
            verify(branchUserRoleRepository, never()).findByBranchIdAndUserIdAndRoleType(branchId, user.getId(), requestBranchUserDto.getRoleType());
            verify(branchUserRoleRepository, never()).save(any(BranchUserRole.class));
            verify(branchUserPermissionRepository, never()).saveAll(anyList());
            verifyNoMoreInteractions(branchRepository);
            verifyNoMoreInteractions(userRepository);
            verifyNoInteractions(branchUserRoleRepository);
            verifyNoInteractions(branchUserPermissionRepository);
        }

        @Test
        @DisplayName("InternalServerErrorException_archive test")
        void InternalServerErrorArchiveException(){
            //given
            User user = makeUser();
            Branch branch = makeBranch();
            Long branchId = branch.getId();
            BranchUserDto.RequestBranchUserDto requestBranchUserDto = BranchUserDto.RequestBranchUserDto.builder()
                    .userId(1L)
                    .nickname("nickname")
                    .roleType(UserRoleEnum.TUTOR)
                    .permissionDtoList(Arrays.asList(
                            BranchUserDto.RequestBranchUserPermissionDto.builder()
                                    .functionType(FunctionTypeEnum.KLASS)
                                    .permissionType(PermissionTypeEnum.EDIT)
                                    .build(),
                            BranchUserDto.RequestBranchUserPermissionDto.builder()
                                    .functionType(FunctionTypeEnum.SCHEDULE)
                                    .permissionType(PermissionTypeEnum.VIEW)
                                    .build()
                    ))
                    .build();
            BranchUserRole branchUserRole = makeBranchUserRole(1L, true);
            given(branchRepository.findById(branchId)).willReturn(Optional.of(branch));
            given(userRepository.findById(requestBranchUserDto.getUserId())).willReturn(Optional.of(user));
            given(branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, user.getId(), requestBranchUserDto.getRoleType())).willReturn(Optional.of(branchUserRole));
            //when
            InternalServerErrorException internalServerErrorException = assertThrows(InternalServerErrorException.class, () -> tutorService.createBranchUser(branchId, requestBranchUserDto));
            //then
            assertEquals("보관함에 있습니다.", internalServerErrorException.getMessage());
            verify(branchRepository, times(1)).findById(branchId);
            verify(userRepository, times(1)).findById(requestBranchUserDto.getUserId());
            verify(branchUserRoleRepository, times(1)).findByBranchIdAndUserIdAndRoleType(branchId, user.getId(), requestBranchUserDto.getRoleType());
            verify(branchUserRoleRepository, never()).save(any(BranchUserRole.class));
            verify(branchUserPermissionRepository, never()).saveAll(anyList());
            verifyNoMoreInteractions(branchRepository);
            verifyNoMoreInteractions(userRepository);
            verifyNoMoreInteractions(branchUserRoleRepository);
            verifyNoInteractions(branchUserPermissionRepository);
        }

        @Test
        @DisplayName("InternalServerErrorException_exist test")
        void InternalServerErrorExistException(){
            //given
            User user = makeUser();
            Branch branch = makeBranch();
            Long branchId = branch.getId();
            BranchUserDto.RequestBranchUserDto requestBranchUserDto = BranchUserDto.RequestBranchUserDto.builder()
                    .userId(1L)
                    .nickname("nickname")
                    .roleType(UserRoleEnum.TUTOR)
                    .permissionDtoList(Arrays.asList(
                            BranchUserDto.RequestBranchUserPermissionDto.builder()
                                    .functionType(FunctionTypeEnum.KLASS)
                                    .permissionType(PermissionTypeEnum.EDIT)
                                    .build(),
                            BranchUserDto.RequestBranchUserPermissionDto.builder()
                                    .functionType(FunctionTypeEnum.SCHEDULE)
                                    .permissionType(PermissionTypeEnum.VIEW)
                                    .build()
                    ))
                    .build();
            BranchUserRole branchUserRole = makeBranchUserRole(1L, false);
            given(branchRepository.findById(branchId)).willReturn(Optional.of(branch));
            given(userRepository.findById(requestBranchUserDto.getUserId())).willReturn(Optional.of(user));
            given(branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, user.getId(), requestBranchUserDto.getRoleType())).willReturn(Optional.of(branchUserRole));
            //when
            InternalServerErrorException internalServerErrorException = assertThrows(InternalServerErrorException.class, () -> tutorService.createBranchUser(branchId, requestBranchUserDto));
            //then
            assertEquals("이미 등록되어 있습니다.", internalServerErrorException.getMessage());
            verify(branchRepository, times(1)).findById(branchId);
            verify(userRepository, times(1)).findById(requestBranchUserDto.getUserId());
            verify(branchUserRoleRepository, times(1)).findByBranchIdAndUserIdAndRoleType(branchId, user.getId(), requestBranchUserDto.getRoleType());
            verify(branchUserRoleRepository, never()).save(any(BranchUserRole.class));
            verify(branchUserPermissionRepository, never()).saveAll(anyList());
            verifyNoMoreInteractions(branchRepository);
            verifyNoMoreInteractions(userRepository);
            verifyNoMoreInteractions(branchUserRoleRepository);
            verifyNoInteractions(branchUserPermissionRepository);
        }
    }

    @Nested
    @DisplayName("updatePermission test")
    class updatePermission{

        @Test
        @DisplayName("ok test")
        void okTest(){
            //given
            BranchUserRole branchUserRole = makeBranchUserRole();
            List<BranchUserPermission> permissionList = makePermissionList(branchUserRole);
            Long branchId = branchUserRole.getBranch().getId();
            Long userId = branchUserRole.getUser().getId();
            Long branchUserRoleId = branchUserRole.getId();
            List<BranchUserDto.RequestBranchUserPermissionDto> requestBranchUserPermissionDtoList = Arrays.asList(
                    BranchUserDto.RequestBranchUserPermissionDto.builder()
                            .functionType(FunctionTypeEnum.KLASS)
                            .permissionType(PermissionTypeEnum.EDIT)
                            .build(),
                    BranchUserDto.RequestBranchUserPermissionDto.builder()
                            .functionType(FunctionTypeEnum.SCHEDULE)
                            .permissionType(PermissionTypeEnum.VIEW)
                            .build()
            );
            given(branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR)).willReturn(Optional.of(branchUserRole));
            given(branchUserPermissionRepository.findByBranchUserRoleId(branchUserRoleId)).willReturn(permissionList);
            //when
            List<BranchUserDto.ResponseBranchUserPermissionDto> actualPermissonList = tutorService.updatePermission(branchId, userId, requestBranchUserPermissionDtoList);
            //then
            for (BranchUserPermission permission : permissionList) {
                assertTrue(permission.getDeleteYn());
            }
            for (BranchUserDto.RequestBranchUserPermissionDto requestBranchUserPermissionDto : requestBranchUserPermissionDtoList) {
                assertTrue(actualPermissonList.stream()
                        .anyMatch(permission -> permission.getFunctionType().equals(requestBranchUserPermissionDto.getFunctionType())
                                && permission.getPermissionType().equals(requestBranchUserPermissionDto.getPermissionType())));
            }
            verify(branchUserRoleRepository, times(1)).findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR);
            verify(branchUserPermissionRepository, times(1)).findByBranchUserRoleId(branchUserRoleId);
            verify(branchUserPermissionRepository, times(2)).saveAll(anyList());
            verifyNoMoreInteractions(branchUserRoleRepository);
            verifyNoMoreInteractions(branchUserPermissionRepository);
        }

        @Test
        @DisplayName("notFoundException test")
        void notFoundException(){
            //given
            BranchUserRole branchUserRole = makeBranchUserRole();
            Long branchId = branchUserRole.getBranch().getId();
            Long userId = branchUserRole.getUser().getId();
            Long branchUserRoleId = branchUserRole.getBranch().getId();
            List<BranchUserDto.RequestBranchUserPermissionDto> requestBranchUserPermissionDtoList = Arrays.asList(
                    BranchUserDto.RequestBranchUserPermissionDto.builder()
                            .functionType(FunctionTypeEnum.KLASS)
                            .permissionType(PermissionTypeEnum.EDIT)
                            .build(),
                    BranchUserDto.RequestBranchUserPermissionDto.builder()
                            .functionType(FunctionTypeEnum.SCHEDULE)
                            .permissionType(PermissionTypeEnum.VIEW)
                            .build()
            );
            given(branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR)).willReturn(Optional.empty());
            //when
            NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> tutorService.updatePermission(branchId, userId, requestBranchUserPermissionDtoList));
            //then
            assertEquals("지점 강사를 찾을 수 없습니다.", notFoundException.getMessage());
            verify(branchUserRoleRepository, times(1)).findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR);
            verify(branchUserPermissionRepository, never()).findByBranchUserRoleId(branchUserRoleId);
            verify(branchUserPermissionRepository, never()).saveAll(anyList());
            verifyNoMoreInteractions(branchUserRoleRepository);
            verifyNoInteractions(branchUserPermissionRepository);
        }

    }

    @Nested
    @DisplayName("getUserPermission test")
    class getUserPermission{

        @Test
        @DisplayName("ok test")
        void okTest(){
            //given
            BranchUserRole branchUserRole = makeBranchUserRole();
            List<BranchUserPermission> permissionList = makePermissionList(branchUserRole);
            Long branchId = branchUserRole.getBranch().getId();
            Long userId = branchUserRole.getUser().getId();
            Long branchUserRoleId = branchUserRole.getId();
            given(branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR)).willReturn(Optional.of(branchUserRole));
            given(branchUserPermissionRepository.findByBranchUserRoleId(branchUserRoleId)).willReturn(permissionList);
            //when
            List<BranchUserDto.ResponseBranchUserPermissionDto> actualPermissonList = tutorService.getUserPermission(branchId, userId);
            //then
            for (BranchUserPermission permission : permissionList) {
                assertTrue(actualPermissonList.stream()
                        .anyMatch(actualPermission -> actualPermission.getFunctionType().equals(permission.getFunctionType())
                                && actualPermission.getPermissionType().equals(permission.getPermissionType())));
            }
            verify(branchUserRoleRepository, times(1)).findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR);
            verify(branchUserPermissionRepository, times(1)).findByBranchUserRoleId(branchUserRoleId);
            verifyNoMoreInteractions(branchUserPermissionRepository);
        }
    }

    @Nested
    @DisplayName("getPermissionAllList test")
    class getPermissionAllList{

        @Test
        @DisplayName("ok test")
        void getPermissionAllList(){
            //given
            BranchUserRole branchUserRole = makeBranchUserRole();
            List<BranchUserPermission> permissionList = makePermissionList(branchUserRole);
            //when
            List<BranchUserDto.ResponseBranchUserPermissionDto> actualPermissonList = tutorService.getPermissionAllList(permissionList);
            //then
            assertEquals(FunctionTypeEnum.values().length, actualPermissonList.size());
            for (BranchUserDto.ResponseBranchUserPermissionDto permissionDto : actualPermissonList) {
                if(permissionList.stream().filter(permission -> permission.getFunctionType().equals(permissionDto.getFunctionType())).count() == 0){
                    assertEquals(PermissionTypeEnum.NONE, permissionDto.getPermissionType());
                }
            }
        }
    }

    @Nested
    @DisplayName("disconnectUser test")
    class disconnectUser{

        @Test
        @DisplayName("ok test")
        void okTest(){
            //given
            BranchUserRole branchUserRole = makeBranchUserRole();
            Long branchId = branchUserRole.getBranch().getId();
            Long userId = branchUserRole.getUser().getId();
            given(branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR)).willReturn(Optional.of(branchUserRole));
            given(branchUserRoleRepository.save(branchUserRole)).willReturn(branchUserRole);
            //when
            BranchUserDto.ResponseBranchUserDto actualBranchUserDto = tutorService.disconnectUser(branchId, userId);
            //then
            assertEquals(ConnectionTypeEnum.DISCONNECTED.getId(), actualBranchUserDto.getConnectionType().getStateCode());
            verify(branchUserRoleRepository, times(1)).findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR);
            verify(branchUserRoleRepository, times(1)).save(branchUserRole);
            verifyNoMoreInteractions(branchUserRoleRepository);
        }

        @Test
        @DisplayName("notFoundException test")
        void notFoundException(){
            //given
            BranchUserRole branchUserRole = makeBranchUserRole();
            Long branchId = branchUserRole.getBranch().getId();
            Long userId = branchUserRole.getUser().getId();
            given(branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR)).willReturn(Optional.empty());
            //when
            NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> tutorService.disconnectUser(branchId, userId));
            //then
            assertEquals("지점 강사를 찾을 수 없습니다.", notFoundException.getMessage());
            verify(branchUserRoleRepository, times(1)).findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR);
            verify(branchUserRoleRepository, never()).save(branchUserRole);
            verifyNoMoreInteractions(branchUserRoleRepository);
        }
    }

    @Nested
    @DisplayName("disconnectBranch test")
    class disconnectBranch{

        @Test
        @DisplayName("ok test")
        void okTest(){
            //given
            BranchUserRole branchUserRole = makeBranchUserRole();
            Long branchId = branchUserRole.getBranch().getId();
            Long userId = branchUserRole.getUser().getId();
            given(branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR)).willReturn(Optional.of(branchUserRole));
            given(branchUserRoleRepository.save(branchUserRole)).willReturn(branchUserRole);
            //when
            BranchUserDto.ResponseBranchUserDto actualBranchUserDto = tutorService.disconnectUser(branchId, userId);
            //then
            assertEquals(ConnectionTypeEnum.DISCONNECTED.getId(), actualBranchUserDto.getConnectionType().getStateCode());
            verify(branchUserRoleRepository, times(1)).findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR);
            verify(branchUserRoleRepository, times(1)).save(branchUserRole);
            verifyNoMoreInteractions(branchUserRoleRepository);
        }

        @Test
        @DisplayName("notFoundException test")
        void notFoundException(){
            //given
            BranchUserRole branchUserRole = makeBranchUserRole();
            Long branchId = branchUserRole.getBranch().getId();
            Long userId = branchUserRole.getUser().getId();
            Long branchUserRoleId = branchUserRole.getId();
            given(branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR)).willReturn(Optional.empty());
            //when
            NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> tutorService.disconnectUser(branchId, userId));
            //then
            assertEquals("지점 강사를 찾을 수 없습니다.", notFoundException.getMessage());
            verify(branchUserRoleRepository, times(1)).findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR);
            verify(branchUserRoleRepository, never()).save(branchUserRole);
            verifyNoMoreInteractions(branchUserRoleRepository);
        }
    }

    @Nested
    @DisplayName("reconnectUser test")
    class reconnectUser{

        @Test
        @DisplayName("ok test")
        void okTest(){
            //given
            BranchUserRole branchUserRole = makeBranchUserRole();
            Long branchId = branchUserRole.getBranch().getId();
            Long userId = branchUserRole.getUser().getId();
            given(branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR)).willReturn(Optional.of(branchUserRole));
            given(branchUserRoleRepository.save(branchUserRole)).willReturn(branchUserRole);
            //when
            BranchUserDto.ResponseBranchUserDto actualBranchUserDto = tutorService.reconnectUser(branchId, userId);
            //then
            assertEquals(ConnectionTypeEnum.WAIT.getId(), actualBranchUserDto.getConnectionType().getStateCode());
            verify(branchUserRoleRepository, times(1)).findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR);
            verify(branchUserRoleRepository, times(1)).save(branchUserRole);
            verifyNoMoreInteractions(branchUserRoleRepository);
        }

        @Test
        @DisplayName("notFoundException test")
        void notFoundException(){
            //given
            BranchUserRole branchUserRole = makeBranchUserRole();
            Long branchId = branchUserRole.getBranch().getId();
            Long userId = branchUserRole.getUser().getId();
            given(branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR)).willReturn(Optional.empty());
            //when
            NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> tutorService.reconnectUser(branchId, userId));
            //then
            assertEquals("지점 강사를 찾을 수 없습니다.", notFoundException.getMessage());
            verify(branchUserRoleRepository, times(1)).findByBranchIdAndUserIdAndRoleType(branchId, userId, UserRoleEnum.TUTOR);
            verify(branchUserRoleRepository, never()).save(branchUserRole);
            verifyNoMoreInteractions(branchUserRoleRepository);
        }
    }
}
