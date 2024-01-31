package com.orderline.branch.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderline.branch.enums.*;
import com.orderline.branch.model.dto.BranchDto;
import com.orderline.branch.model.dto.BranchUserDto;
import com.orderline.branch.model.entity.Branch;
import com.orderline.branch.model.entity.BranchUserPermission;
import com.orderline.branch.model.entity.BranchUserRole;
import com.orderline.branch.service.BranchService;
import com.orderline.tutor.service.TutorService;
import com.orderline.common.user.enums.UserRoleEnum;
import com.orderline.common.user.model.dto.UserDto;
import com.orderline.common.user.model.entity.User;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BranchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private BranchService branchService;

    @MockBean
    private TutorService tutorService;

    public Branch makeBranch(){
        return Branch.builder()
                .id(1342L)
                .branchName("테스트지점")
                .build();
    }

    public Branch makeBranch(Long branchId){
        return Branch.builder()
                .id(branchId)
                .branchName("테스트지점")
                .build();
    }

    public User makeUser() {
        return User.builder()
                .id(5346L)
                .lastLoginRoleType(UserRoleEnum.MANAGER)
                .build();
    }

    public BranchUserRole makeBranchUserRole(){
        return BranchUserRole.builder()
                .id(5876L)
                .branch(makeBranch())
                .user(makeUser())
                .roleType(UserRoleEnum.MANAGER)
                .build();
    }

    public List<BranchUserPermission> makePermissionList(BranchUserRole branchUserRole) {
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

    private ArgumentMatcher<BranchDto.RequestBranchDto> hasSameValuesAs(BranchDto.RequestBranchDto expectedRequestBranchDto) {
        return actualRequestBranchDto ->
                actualRequestBranchDto.getBranchName().equals(expectedRequestBranchDto.getBranchName())
                        && actualRequestBranchDto.getSubjectType() == expectedRequestBranchDto.getSubjectType()
                        && actualRequestBranchDto.getSubjectDetail().equals(expectedRequestBranchDto.getSubjectDetail())
                        && actualRequestBranchDto.getReservationType() == expectedRequestBranchDto.getReservationType();
    }

    @Nested
    @DisplayName("get test")
    class getTest {

        @Test
        @DisplayName("ok test")
        @WithMockUser(username = "test", roles = "MANAGER")
        void okTest() throws Exception {
            //given
            Branch branch = makeBranch();
            Long branchId = branch.getId();
            BranchDto.ResponseBranchDto expectedBranch = BranchDto.ResponseBranchDto.toDto(branch);
            given(branchService.get(branchId)).willReturn(expectedBranch);
            //when
            ResultActions result = mockMvc.perform(get("/manager/branch/" + branchId)
                    .with(csrf())).andDo(print());
            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.branchId").value(branchId))
                    .andExpect(jsonPath("$.branchName").value(branch.getBranchName()))
                    .andDo(print());
        }

        @Test
        @DisplayName("unauthorized test")
        void unauthorizedTest() throws Exception {
            //given
            Branch branch = makeBranch();
            Long branchId = branch.getId();
            BranchDto.ResponseBranchDto expectedBranch = BranchDto.ResponseBranchDto.toDto(branch);
            given(branchService.get(branchId)).willReturn(expectedBranch);
            //when
            ResultActions result = mockMvc.perform(get("/manager/branch/" + branchId)
                    .with(csrf())).andDo(print());
            //then
            result.andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.systemTitle").value("Full authentication is required to access this resource"))
                    .andDo(print());
        }

        @Test
        @DisplayName("access_denied test")
        @WithMockUser(username = "test", roles = "TUTOR")
        void accessDeniedTest() throws Exception {
            //given
            Branch branch = makeBranch();
            Long branchId = branch.getId();
            BranchDto.ResponseBranchDto expectedBranch = BranchDto.ResponseBranchDto.toDto(branch);
            given(branchService.get(branchId)).willReturn(expectedBranch);
            //when
            ResultActions result = mockMvc.perform(get("/manager/branch/" + branchId)
                    .with(csrf())).andDo(print());
            //then
            result.andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.systemTitle").value("Access is denied"))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("getList test")
    class getListTest {

        @Test
        @DisplayName("ok test")
        @WithMockUser(username = "test", roles = "MANAGER")
        void okTest() throws Exception {
            //given
            Branch branch1 = makeBranch(123L);
            Branch branch2 = makeBranch(456L);
            Long userId = 999L;
            Pageable pageable = PageRequest.of(0, 10);
            List<Branch> branchList = Arrays.asList(branch1, branch2);
            Page<Branch> expectedBranchPage = new PageImpl<>(branchList, pageable, branchList.size());
            Page<BranchDto.ResponseBranchDto> expectedBranchPageDto = expectedBranchPage.map(BranchDto.ResponseBranchDto::toDto);
            given(branchService.getList(userId, pageable)).willReturn(expectedBranchPageDto);
            //when
            ResultActions result = mockMvc.perform(get("/manager/branch")
                            .requestAttr("userId", userId)
                            .param("page", "0")
                            .param("maxResults", "10")
                    .with(csrf())).andDo(print());
            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.results.[*].branchId",
                            containsInAnyOrder(
                            Matchers.equalTo(branch1.getId().intValue()),
                            Matchers.equalTo(branch2.getId().intValue()))))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("select test")
    class select{

        @Test
        @DisplayName("ok test")
        void okTest() throws Exception{
            //given
            Branch branch = makeBranch();
            BranchUserRole branchUserRole = makeBranchUserRole();
            Long branchId = branch.getId();
            Long branchUserRoleId = branchUserRole.getId();
            Long userId = branchUserRole.getUser().getId();
            UserDto.UserInfoDto userInfoDto = UserDto.UserInfoDto.toDto(branchUserRole.getUser());
            given(branchService.select(userId, branchUserRoleId)).willReturn(userInfoDto);
            //when
            ResultActions result = mockMvc.perform(patch("/common/branch/" + branchId + "/select/" + branchUserRoleId)
                    .requestAttr("userId", userId)
                    .with(csrf())).andDo(print());
            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.lastLoginRoleType").value(branchUserRole.getRoleType().getId()))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("delete test")
    class deleteTest{

        @Test
        @DisplayName("ok test")
        @WithMockUser(username = "test", roles = "MANAGER")
        void okTest() throws Exception{
            //given
            Branch branch = makeBranch();
            Long branchId = branch.getId();
            //when
            ResultActions result = mockMvc.perform(delete("/manager/branch/" + branchId)
                    .with(csrf())).andDo(print());
            //then
            result.andExpect(status().isNoContent())
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("update test")
    class updateTest{

        @Test
        @DisplayName("ok test")
        @WithMockUser(username = "test", roles = "MANAGER")
        void okTest() throws Exception{
            //given
            Branch branch = makeBranch();
            Long branchId = branch.getId();
            BranchDto.RequestBranchDto requestBranchDto = BranchDto.RequestBranchDto.builder()
                    .branchName("지점 수정 테스트")
                    .subjectType(SubjectTypeEnum.SPORTS)
                    .subjectDetail("골프")
                    .reservationType(ReservationTypeEnum.FREE_CHOICE)
                    .build();
            branch.updateBranch(requestBranchDto);
            BranchDto.ResponseBranchDto expectedBranch = BranchDto.ResponseBranchDto.toDto(branch);
            given(branchService.update(eq(branchId), argThat(hasSameValuesAs(requestBranchDto)))).willReturn(expectedBranch);
            //when
            ResultActions result = mockMvc.perform(patch("/manager/branch/" + branchId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(requestBranchDto))
                    .with(csrf())).andDo(print());
            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.branchName").value(requestBranchDto.getBranchName()))
                    .andExpect(jsonPath("$.subjectType").value(requestBranchDto.getSubjectType().getId()))
                    .andExpect(jsonPath("$.subjectDetail").value(requestBranchDto.getSubjectDetail()))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("create test")
    class createTest{

        @Test
        @DisplayName("ok test")
        @WithMockUser(username = "test", roles = "MANAGER")
        void okTest() throws Exception {
            //given
            BranchDto.RequestBranchDto requestBranchDto = BranchDto.RequestBranchDto.builder()
                    .branchName("지점 생성 테스트")
                    .subjectType(SubjectTypeEnum.SPORTS)
                    .subjectDetail("골프")
                    .reservationType(ReservationTypeEnum.FREE_CHOICE)
                    .build();
            User user = makeUser();
            Branch branch = requestBranchDto.toEntity(user);
            Long userId = user.getId();
            BranchDto.ResponseBranchDto expectedBranch = BranchDto.ResponseBranchDto.toDto(branch);
            given(branchService.create(eq(userId), argThat(hasSameValuesAs(requestBranchDto)))).willReturn(expectedBranch);
            //when
            ResultActions result = mockMvc.perform(post("/manager/branch")
                    .contentType(MediaType.APPLICATION_JSON)
                    .requestAttr("userId", userId)
                    .content(objectMapper.writeValueAsBytes(requestBranchDto))
                    .with(csrf())).andDo(print());
            //then
            result.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.branchName").value(requestBranchDto.getBranchName()))
                    .andExpect(jsonPath("$.subjectType").value(requestBranchDto.getSubjectType().getId()))
                    .andExpect(jsonPath("$.subjectDetail").value(requestBranchDto.getSubjectDetail()))
                    .andExpect(jsonPath("$.reservationType").value(requestBranchDto.getReservationType().getId()))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("archive test")
    class archiveTest{

        @Test
        @DisplayName("ok test")
        @WithMockUser(username = "test", roles = "MANAGER")
        void okTest() throws Exception{
            //given
            Branch branch = makeBranch();
            Long branchId = branch.getId();
            branch.archiveBranch();
            BranchDto.ResponseBranchDto expectedBranch = BranchDto.ResponseBranchDto.toDto(branch);
            given(branchService.archive(branchId)).willReturn(expectedBranch);
            //when
            ResultActions result = mockMvc.perform(patch("/manager/branch/" + branchId + "/archive")
                    .with(csrf())).andDo(print());
            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.branchArchiveYn").value(true))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("getMyPermission test")
    class getMyPermission{

        @Test
        @DisplayName("ok test")
        @WithMockUser(username = "test", roles = "MANAGER")
        void okTest() throws Exception{
            //given
            BranchUserRole branchUserRole = makeBranchUserRole();
            Long branchId = branchUserRole.getBranch().getId();
            Long userId = branchUserRole.getUser().getId();
            UserRoleEnum roleType = branchUserRole.getRoleType();
            List<BranchUserPermission> branchUserPermissionList = makePermissionList(branchUserRole);
            List<BranchUserDto.ResponseBranchUserPermissionDto> expectedPermissions
                    = branchUserPermissionList.stream().map(BranchUserDto.ResponseBranchUserPermissionDto::toDto).collect(Collectors.toList());
            given(branchService.getMyPermission(branchId, userId, roleType)).willReturn(expectedPermissions);
            //when
            ResultActions result = mockMvc.perform(get("/manager/branch/" + branchId + "/permission")
                    .requestAttr("userId", userId)
                    .param("roleType", roleType.name())
                    .with(csrf())).andDo(print());
            //then
            result.andExpect(status().isOk())
                    .andDo(print());

        }
    }

    @Nested
    @DisplayName("disconnectBranch test")
    class disconnectBranch{

        @Test
        @DisplayName("ok test")
        void okTest() throws Exception{
            //given
            Branch branch = makeBranch();
            BranchUserRole branchUserRole = makeBranchUserRole();
            Long branchId = branch.getId();
            Long userId = branchUserRole.getUser().getId();
            UserRoleEnum roleType = branchUserRole.getRoleType();
            branchUserRole.updateConnectType(ConnectionTypeEnum.DISCONNECTED);
            BranchUserDto.ResponseBranchUserDto expectedBranchUser = BranchUserDto.ResponseBranchUserDto.toDto(branchUserRole);
            given(branchService.disconnectBranch(branchId, userId, roleType)).willReturn(expectedBranchUser);
            //when
            ResultActions result = mockMvc.perform(patch("/common/branch/" + branchId + "/disconnect" )
                    .requestAttr("userId", userId)
                    .param("roleType", roleType.name())
                    .with(csrf())).andDo(print());
            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.connectionType.stateCode").value(ConnectionTypeEnum.DISCONNECTED.getId()))
                    .andDo(print());
        }
    }
}
