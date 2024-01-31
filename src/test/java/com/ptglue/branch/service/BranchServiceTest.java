package com.ptglue.branch.service;


import com.ptglue.basic.exception.NotFoundException;
import com.ptglue.branch.enums.FunctionTypeEnum;
import com.ptglue.branch.enums.PermissionTypeEnum;
import com.ptglue.branch.enums.SubjectTypeEnum;
import com.ptglue.branch.model.dto.BranchDto;
import com.ptglue.branch.model.dto.BranchUserDto;
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
class BranchServiceTest {

    @InjectMocks
    private BranchService branchService;

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
                .id(1L)
                .build();
    }

    public Branch makeBranch(){
        return Branch.builder()
                .id(1L)
                .branchName("test")
                .build();
    }

    public BranchUserRole makeBranchUserRole(){
        return BranchUserRole.builder()
                .id(1L)
                .branch(makeBranch())
                .user(makeUser())
                .roleType(UserRoleEnum.TUTOR)
                .build();
    }

    public BranchUserRole makeBranchUserRole(Long id){
        return BranchUserRole.builder()
                .id(id)
                .branch(makeBranch())
                .build();
    }

    public BranchUserRole makeBranchUserRole(Long id, UserRoleEnum userRoleEnum){
        return BranchUserRole.builder()
                .id(id)
                .branch(makeBranch())
                .roleType(userRoleEnum)
                .build();
    }

    @Nested
    @DisplayName("get Test")
    class getTest {

        @Test
        @DisplayName("ok Test")
        void ok() {
            //given
            Branch branch = makeBranch();
            Long branchId = branch.getId();
            BranchDto.ResponseBranchDto expectedBranchDto = BranchDto.ResponseBranchDto.toDto(branch);
            given(branchRepository.findById(branchId)).willReturn(Optional.of(branch));
            //when
            BranchDto.ResponseBranchDto actualBranchDto = branchService.get(branchId);
            //then
            assertEquals(expectedBranchDto.getBranchId(), actualBranchDto.getBranchId());
            assertEquals(expectedBranchDto.getBranchName(), actualBranchDto.getBranchName());
            verify(branchRepository, times(1)).findById(branchId);
            verifyNoMoreInteractions(branchRepository);
        }

        @Test
        @DisplayName("NotFoundException Test")
        void notFoundException() {
            //given
            Branch branch = makeBranch();
            Long branchId = branch.getId();
            BranchDto.ResponseBranchDto expectedBranchDto = BranchDto.ResponseBranchDto.toDto(branch);
            given(branchRepository.findById(branchId)).willReturn(Optional.empty());
            //when
            NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> branchService.get(branchId));
            //then
            assertEquals("지점이 존재하지 않습니다.", notFoundException.getMessage());
            verify(branchRepository, times(1)).findById(branchId);
            verifyNoMoreInteractions(branchRepository);
        }
    }

    @Nested
    @DisplayName("getList Test")
    class getListTest{

        @Test
        @DisplayName("ok Test")
        void ok(){
            //given
            BranchUserRole branchUserRole1 = makeBranchUserRole(1L);
            BranchUserRole branchUserRole2= makeBranchUserRole(2L);
            Long userId = 1L;
            Pageable pageable = PageRequest.of(0, 10);
            List<BranchUserRole> branchUserRoleList = Arrays.asList(branchUserRole1, branchUserRole2);
            Page<BranchUserRole> branchUserRolePage = new PageImpl<>(branchUserRoleList);
            Page<BranchDto.ResponseBranchDto> expectedBranchDtoList = branchUserRolePage.map(BranchDto.ResponseBranchDto::toDto);
            given(branchUserRoleRepository.findBranchListNativeQuery(userId, pageable)).willReturn(branchUserRolePage);
            //when
            Page<BranchDto.ResponseBranchDto> actualBranchDtoList = branchService.getList(userId, pageable);
            //then
            assertEquals(expectedBranchDtoList.getTotalElements(), actualBranchDtoList.getTotalElements());
            for (int i = 0; i < expectedBranchDtoList.getTotalElements(); i++) {
                assertThat(actualBranchDtoList.getContent().contains(expectedBranchDtoList.getContent().get(i)));
            }
            verify(branchUserRoleRepository, times(1)).findBranchListNativeQuery(userId, pageable);
            verifyNoMoreInteractions(branchUserRoleRepository);
        }
    }

    @Nested
    @DisplayName("select Test")
    class selectTest{

        @Test
        @DisplayName("ok Test")
        void ok(){
            //given
            User user = makeUser();
            BranchUserRole branchUserRole1 = makeBranchUserRole(1L, UserRoleEnum.MANAGER);
            BranchUserRole branchUserRole2 = makeBranchUserRole(2L, UserRoleEnum.TUTOR);
            List<BranchUserRole> branchUserRoleList = Arrays.asList(branchUserRole1, branchUserRole2);
            Long userId = user.getId();
            Long branchUserId = branchUserRole1.getId();
            given(userRepository.findById(userId)).willReturn(Optional.of(user));
            given(branchUserRoleRepository.findByUserId(userId)).willReturn(branchUserRoleList);
            //when
            UserDto.UserInfoDto actualUserDto = branchService.select(userId, branchUserId);
            //then
            assertEquals(userId, actualUserDto.getUserId());
            assertEquals(UserRoleEnum.MANAGER, actualUserDto.getLastLoginRoleType());
            assertTrue(branchUserRole1.getLastViewYn());
            assertFalse(branchUserRole2.getLastViewYn());
            verify(userRepository, times(1)).findById(userId);
            verify(branchUserRoleRepository, times(1)).findByUserId(userId);
            verify(branchUserRoleRepository, times(1)).saveAll(branchUserRoleList);
            verify(userRepository, times(1)).save(user);
            verifyNoMoreInteractions(userRepository);
            verifyNoMoreInteractions(branchUserRoleRepository);
        }

        @Test
        @DisplayName("notFoundException Test")
        void notFoundException(){
            //given
            User user = makeUser();
            BranchUserRole branchUserRole1 = makeBranchUserRole(1L, UserRoleEnum.MANAGER);
            BranchUserRole branchUserRole2 = makeBranchUserRole(2L, UserRoleEnum.TUTOR);
            List<BranchUserRole> branchUserRoleList = Arrays.asList(branchUserRole1, branchUserRole2);
            Long userId = user.getId();
            Long branchUserId = branchUserRole1.getId();
            given(userRepository.findById(userId)).willReturn(Optional.empty());
            //when
            NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> branchService.select(userId, branchUserId));
            //then
            assertEquals("사용자가 존재하지 않습니다.", notFoundException.getMessage());
            verify(userRepository, times(1)).findById(userId);
            verify(branchUserRoleRepository, never()).findByUserId(userId);
            verify(branchUserRoleRepository, never()).saveAll(branchUserRoleList);
            verify(userRepository, never()).save(user);
            verifyNoMoreInteractions(userRepository);
            verifyNoInteractions(branchUserRoleRepository);
        }
    }

    @Nested
    @DisplayName("delete Test")
    class deleteTest{

        @Test
        @DisplayName("ok Test")
        void ok(){
            //given
            Branch branch = makeBranch();
            Long branchId = branch.getId();
            BranchUserRole branchUserRole1 = makeBranchUserRole(1L);
            BranchUserRole branchUserRole2 = makeBranchUserRole(2L);
            List<BranchUserRole> branchUserRoleList = Arrays.asList(branchUserRole1, branchUserRole2);
            given(branchRepository.findById(branchId)).willReturn(Optional.of(branch));
            given(branchUserRoleRepository.findByBranchId(branchId)).willReturn(branchUserRoleList);
            //when
            branchService.delete(branchId);
            //then
            assertTrue(branch.getDeleteYn());
            assertFalse(branchUserRole1.getDeleteYn());
            assertFalse(branchUserRole2.getDeleteYn());
            verify(branchRepository, times(1)).findById(branchId);
            verify(branchRepository, times(1)).save(any());
            verify(branchUserRoleRepository, times(1)).findByBranchId(branchId);
            verify(branchUserRoleRepository, times(1)).saveAll(any());
            verifyNoMoreInteractions(branchRepository);
            verifyNoMoreInteractions(branchUserRoleRepository);
        }

        @Test
        @DisplayName("notFoundException Test")
        void notFoundException(){
            //given
            Branch branch = makeBranch();
            Long branchId = branch.getId();
            BranchUserRole branchUserRole1 = makeBranchUserRole(1L);
            BranchUserRole branchUserRole2 = makeBranchUserRole(2L);
            List<BranchUserRole> branchUserRoleList = Arrays.asList(branchUserRole1, branchUserRole2);
            given(branchRepository.findById(branchId)).willReturn(Optional.empty());
            //when
            NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> branchService.delete(branchId));
            //then
            assertEquals("지점이 존재하지 않습니다.", notFoundException.getMessage());
            verify(branchRepository, times(1)).findById(branchId);
            verify(branchRepository, never()).save(any());
            verify(branchUserRoleRepository, never()).findByBranchId(branchId);
            verify(branchUserRoleRepository, never()).saveAll(any());
            verifyNoMoreInteractions(branchRepository);
            verifyNoInteractions(branchUserRoleRepository);
        }
    }

    @Nested
    @DisplayName("update Test")
    class updateTest{

        @Test
        @DisplayName("ok Test")
        void ok() {
            //given
            Branch branch = makeBranch();
            BranchDto.RequestBranchDto updateBranchDto = BranchDto.RequestBranchDto.builder()
                    .branchName("updateBranchName")
                    .imageUri("updateImageUri")
                    .subjectType(SubjectTypeEnum.SPORTS)
                    .subjectDetail("요가")
                    .build();
            Long branchId = branch.getId();
            given(branchRepository.findById(branchId)).willReturn(Optional.of(branch));
            given(branchRepository.save(branch)).willReturn(branch);
            //when
            BranchDto.ResponseBranchDto actualBranchDto = branchService.update(branchId, updateBranchDto);
            //then
            assertEquals(branchId, actualBranchDto.getBranchId());
            assertEquals(updateBranchDto.getBranchName(), actualBranchDto.getBranchName());
            assertEquals(updateBranchDto.getImageUri(), actualBranchDto.getImageUri());
            assertEquals(updateBranchDto.getSubjectType(), actualBranchDto.getSubjectType());
            assertEquals(updateBranchDto.getSubjectDetail(), actualBranchDto.getSubjectDetail());
            verify(branchRepository, times(1)).findById(branchId);
            verify(branchRepository, times(1)).save(any());
            verifyNoMoreInteractions(branchRepository);
        }

        @Test
        @DisplayName("notFoundException Test")
        void notFoundException(){
            //given
            Branch branch = makeBranch();
            BranchDto.RequestBranchDto updateBranchDto = BranchDto.RequestBranchDto.builder()
                    .branchName("updateBranchName")
                    .imageUri("updateImageUri")
                    .subjectType(SubjectTypeEnum.SPORTS)
                    .subjectDetail("요가")
                    .build();
            Long branchId = branch.getId();
            given(branchRepository.findById(branchId)).willReturn(Optional.empty());
            //when
            NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> branchService.update(branchId, updateBranchDto));
            //then
            assertEquals("지점이 존재하지 않습니다.", notFoundException.getMessage());
            verify(branchRepository, times(1)).findById(branchId);
            verify(branchRepository, never()).save(any());
            verifyNoMoreInteractions(branchRepository);
        }
    }

    @Nested
    @DisplayName("create Test")
    class createTest{

        @Test
        @DisplayName("ok Test")
        void okTest(){
            //given
            User user = makeUser();
            BranchDto.RequestBranchDto requestBranchDto = BranchDto.RequestBranchDto.builder()
                    .branchName("createBranchName")
                    .imageUri("createImageUri")
                    .subjectType(SubjectTypeEnum.SPORTS)
                    .subjectDetail("요가")
                    .build();
            Branch branch = requestBranchDto.toEntity(user);
            Long userId = user.getId();
            given(userRepository.findById(userId)).willReturn(Optional.of(user));
            given(branchRepository.save(any())).willReturn(branch);
            //when
            BranchDto.ResponseBranchDto actualBranchDto = branchService.create(userId, requestBranchDto);
            //then
            assertEquals(requestBranchDto.getBranchName(), actualBranchDto.getBranchName());
            assertEquals(requestBranchDto.getImageUri(), actualBranchDto.getImageUri());
            assertEquals(requestBranchDto.getSubjectType(), actualBranchDto.getSubjectType());
            assertEquals(requestBranchDto.getSubjectDetail(), actualBranchDto.getSubjectDetail());
            verify(userRepository, times(1)).findById(userId);
            verify(branchRepository, times(1)).save(any());
            verify(branchUserRoleRepository, times(1)).save(any());
            verify(branchUserPermissionRepository, times(1)).saveAll(any());
            verifyNoMoreInteractions(userRepository);
            verifyNoMoreInteractions(branchRepository);
            verifyNoMoreInteractions(branchUserRoleRepository);
            verifyNoMoreInteractions(branchUserPermissionRepository);
        }

        @Test
        @DisplayName("notFoundException Test")
        void notFoundExceptionTest(){
            //given
            User user = makeUser();
            BranchDto.RequestBranchDto requestBranchDto = BranchDto.RequestBranchDto.builder()
                    .branchName("createBranchName")
                    .imageUri("createImageUri")
                    .subjectType(SubjectTypeEnum.SPORTS)
                    .subjectDetail("요가")
                    .build();
            Long userId = user.getId();
            given(userRepository.findById(userId)).willReturn(Optional.empty());
            //when
           NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> branchService.create(userId, requestBranchDto));
            //then
            assertEquals("사용자가 존재하지 않습니다.", notFoundException.getMessage());
            verify(userRepository, times(1)).findById(userId);
            verify(branchRepository, never()).save(any());
            verify(branchUserRoleRepository, never()).save(any());
            verify(branchUserPermissionRepository, never()).saveAll(any());
            verifyNoMoreInteractions(userRepository);
            verifyNoInteractions(branchRepository);
            verifyNoInteractions(branchUserRoleRepository);
            verifyNoInteractions(branchUserPermissionRepository);
        }
    }

    @Nested
    @DisplayName("archive Test")
    class archiveTest{

        @Test
        @DisplayName("ok Test")
        void okTest(){
            //given
            Branch branch = makeBranch();
            Long branchId = branch.getId();
            given(branchRepository.findById(branchId)).willReturn(Optional.of(branch));
            //when
            BranchDto.ResponseBranchDto actualBranchDto = branchService.archive(branchId);
            //then
            assertEquals(branchId, actualBranchDto.getBranchId());
            assertTrue(actualBranchDto.getBranchArchiveYn());
            verify(branchRepository, times(1)).findById(branchId);
            verify(branchRepository, times(1)).save(any());
            verify(branchUserRoleRepository, times(1)).findByBranchId(branchId);
            verify(branchUserRoleRepository, times(1)).saveAll(any());
            verifyNoMoreInteractions(branchRepository);
            verifyNoMoreInteractions(branchUserRoleRepository);
        }

        @Test
        @DisplayName("notFoundException Test")
        void notFoundException(){
            //given
            Branch branch = makeBranch();
            Long branchId = branch.getId();
            given(branchRepository.findById(branchId)).willReturn(Optional.empty());
            //when
            NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> branchService.archive(branchId));
            //then
            assertEquals("지점이 존재하지 않습니다.", notFoundException.getMessage());
            verify(branchRepository, times(1)).findById(branchId);
            verify(branchRepository, never()).save(any());
            verify(branchUserRoleRepository, never()).findByBranchId(branchId);
            verify(branchUserRoleRepository, never()).saveAll(any());
            verifyNoMoreInteractions(branchRepository);
            verifyNoInteractions(branchUserRoleRepository);
        }
    }

}
