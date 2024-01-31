package com.ptglue.common.repository.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptglue.common.user.enums.SnsTypeEnum;
import com.ptglue.common.user.enums.UserRoleEnum;
import com.ptglue.common.user.model.dto.ActivationCodeDto;
import com.ptglue.common.user.model.dto.UserDto;
import com.ptglue.common.user.model.entity.User;
import com.ptglue.basic.exception.InternalServerErrorException;
import com.ptglue.common.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

//    @MockBean
//    private CommonService commonService;

    public User makeTutee(Long id){
        return User.builder()
                .id(id)
                .username("tuteeid")
                .password("password")
                .name("testTutee")
                .phone("01022222222")
                .joinDt(ZonedDateTime.now(ZoneId.of("UTC")))
                .lastLoginDt(ZonedDateTime.now(ZoneId.of("UTC")))
                .build();
    }

    @Test
    @DisplayName("doSignIn test")
    void doSignInTest() throws Exception {
        //given
        User expectedUser = makeTutee(1L);
        UserDto.RequestUserSignInDto signInDto = UserDto.RequestUserSignInDto.builder()
                .username(expectedUser.getUsername())
                .password(expectedUser.getPassword())
                .build();
        UserDto.UserInfoDto userInfoDto = UserDto.UserInfoDto.toDto(expectedUser);
        UserDto.ResponseJwtDto jwtDto = UserDto.ResponseJwtDto.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .expiresIn(12345678L)
                .build();
        UserDto.RoleDto roleDto = UserDto.RoleDto.builder()
                .roleType(UserRoleEnum.MANAGER)
                .branchId(0L)
                .branchCount(0L)
                .build();
        UserDto.ResponseUserInfoWithAuthDto expectedUserDto = UserDto.ResponseUserInfoWithAuthDto.toDto(userInfoDto, jwtDto, roleDto);
        given(userService.getUserInfoByUsernameAndPassword(signInDto.getUsername(), signInDto.getPassword())).willReturn(userInfoDto);
        given(userService.doSignIn(userInfoDto)).willReturn(expectedUserDto);
        //when
        ResultActions result = mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(signInDto))
                .with(csrf()))
                .andDo(print());
        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.phone").exists())
                .andExpect(jsonPath("$.responseJwtDto").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("doSignOut test")
    void doSignOutTest() throws Exception {
        //given
        User user = makeTutee(1L);
        //when
        ResultActions result = mockMvc.perform(post("/user/logout")
                .requestAttr("userId", user.getId()))
                .andDo(print());
        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.systemTitle").value("로그아웃이 완료되었습니다."))
                .andExpect(jsonPath("$.data").value(true))
                .andDo(print());
    }

    @Nested
    @DisplayName("checkId Test")
    class checkIdTest{

        @Test
        @DisplayName("ok test")
        void okTest() throws Exception {
            //given
            String username = "testId";
            //when
            ResultActions result = mockMvc.perform(get("/user/check/" + username)
                    .with(csrf())).andDo(print());
            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.systemTitle").value("사용 가능한 ID 입니다."))
                    .andExpect(jsonPath("$.data").value(true))
                    .andDo(print());
        }

        @Test
        @DisplayName("badRequest test")
        void badRequestTest() throws Exception {
            //given
            User user = makeTutee(1L);
            String username = user.getUsername();
            doThrow(new InternalServerErrorException("이미 가입된 ID 입니다.")).when(userService).getUserInfoByUsername(username);
            //when
            ResultActions result = mockMvc.perform(get("/user/check/"+ username)
                            .with(csrf())).andDo(print());
            //then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.systemTitle").value("이미 가입된 ID 입니다."))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("doSignUp test")
    class doSignUpTest{

        @Test
        @DisplayName("createTest")
        void createTest() throws Exception {
            //given
            UserDto.RequestUserSignUpDto signUpDto = UserDto.RequestUserSignUpDto.builder()
                    .name("testname")
                    .username("testid")
                    .password("asdf123!")
                    .phone("01012345678")
                    .lastLoginRoleType(UserRoleEnum.TUTEE)
                    .build();
            UserDto.UserInfoDto userInfoDto = UserDto.UserInfoDto.toDto(signUpDto.toUserSignUpEntity());
            UserDto.ResponseJwtDto jwtDto = UserDto.ResponseJwtDto.builder()
                    .accessToken("accessToken")
                    .refreshToken("refreshToken")
                    .expiresIn(12345678L)
                    .build();
            UserDto.RoleDto roleDto = UserDto.RoleDto.builder()
                    .roleType(UserRoleEnum.MANAGER)
                    .branchId(0L)
                    .branchCount(0L)
                    .build();
            UserDto.ResponseUserInfoWithAuthDto expectedUserDto = UserDto.ResponseUserInfoWithAuthDto.toDto(userInfoDto, jwtDto, roleDto);
            //any아니면 왜 안되는지 확인 필요
//            given(userService.doSignUp(signUpDto)).willReturn(userInfoDto);
            given(userService.doSignUp(any())).willReturn(userInfoDto);
            given(userService.doSignIn(userInfoDto)).willReturn(expectedUserDto);
            //when
            ResultActions result = mockMvc.perform(post("/user/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(signUpDto))
                    .with(csrf())).andDo(print());
            //then
            result.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.username").exists())
                    .andExpect(jsonPath("$.name").exists())
                    .andExpect(jsonPath("$.phone").exists())
                    .andExpect(jsonPath("$.responseJwtDto").exists())
                    .andDo(print());
        }

        @Test
        @DisplayName("BadRequestTest")
        void badRequestTest() throws Exception {
//            given
            User user = makeTutee(1L);
            UserDto.RequestUserSignUpDto signUpDto = UserDto.RequestUserSignUpDto.builder()
                    .name("testname")
                    .username(user.getUsername())
                    .password("asdf123!")
                    .phone("01012345678")
                    .phoneActiveYn(true)
                    .lastLoginRoleType(UserRoleEnum.TUTEE)
                    .build();
            UserDto.UserInfoDto userInfoDto = UserDto.UserInfoDto.toDto(signUpDto.toUserSignUpEntity());
            UserDto.ResponseJwtDto jwtDto = UserDto.ResponseJwtDto.builder()
                    .accessToken("accessToken")
                    .refreshToken("refreshToken")
                    .expiresIn(12345678L)
                    .build();
            UserDto.RoleDto roleDto = UserDto.RoleDto.builder()
                    .roleType(UserRoleEnum.MANAGER)
                    .branchId(0L)
                    .branchCount(0L)
                    .build();
            UserDto.ResponseUserInfoWithAuthDto expectedUserDto = UserDto.ResponseUserInfoWithAuthDto.toDto(userInfoDto, jwtDto, roleDto);
            doThrow(new InternalServerErrorException("이미 가입된 ID 입니다.")).when(userService).getUserInfoByUsername(signUpDto.getUsername());
            given(userService.doSignUp(signUpDto)).willReturn(userInfoDto);
            given(userService.doSignIn(userInfoDto)).willReturn(expectedUserDto);
            //when
            ResultActions result = mockMvc.perform(post("/user/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(signUpDto))
                    .with(csrf())).andDo(print());
            //then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.systemTitle").value("이미 가입된 ID 입니다."))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("checkRecommendedId test")
    class checkRecommendedIdTest{

        @Test
        @DisplayName("okTest")
        void okTest() throws Exception {
            //given
            User user = makeTutee(1L);
            String username = user.getUsername();
            Long userId = user.getId();
            given(userService.getUserInfoByUsername(username)).willReturn(userId);
            //when
            ResultActions result = mockMvc.perform(get("/user/check/recommended/" + username)
                    .with(csrf())).andDo(print());
            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(userId))
                    .andDo(print());
        }

        @Test
        @DisplayName("not found test")
        void notFoundTest() throws Exception {
            //given
            User user = makeTutee(1L);
            String username = user.getUsername();
            Long userId = user.getId();
            given(userService.getUserInfoByUsername(username)).willReturn(0L);
            //when
            ResultActions result = mockMvc.perform(get("/user/check/recommended/" + username)
                    .with(csrf())).andDo(print());
            //then
            result.andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.systemTitle").value("추천인 아이디가 존재하지 않습니다."))
                    .andDo(print());
        }
    }

    @Test
    @DisplayName("doSignUpSocialCheck test")
    void doSignUpSocialCheckTest() throws Exception {
        //given
        UserDto.RequestUserSignUpSocialCheckDto snsCheckDto = UserDto.RequestUserSignUpSocialCheckDto.builder()
                .snsId("testname")
                .snsType(SnsTypeEnum.KAKAO)
                .build();
        UserDto.UserInfoDto userInfoDto = UserDto.UserInfoDto.builder()
                .name("testname")
                .username("testid")
                .phone("01012345678")
                .snsId("testname")
                .snsType(SnsTypeEnum.KAKAO)
                .lastLoginRoleType(UserRoleEnum.MANAGER)
                .joinDt(ZonedDateTime.now(ZoneId.of("UTC")))
                .lastLoginDt(ZonedDateTime.now(ZoneId.of("UTC")))
                .build();
        UserDto.ResponseJwtDto jwtDto = UserDto.ResponseJwtDto.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .expiresIn(12345678L)
                .build();
        UserDto.RoleDto roleDto = UserDto.RoleDto.builder()
                .roleType(UserRoleEnum.MANAGER)
                .branchId(0L)
                .branchCount(0L)
                .build();
        UserDto.ResponseUserInfoWithAuthDto expectedUserDto = UserDto.ResponseUserInfoWithAuthDto.toDto(userInfoDto, jwtDto, roleDto);
        given(userService.getUserInfoBySnsInfo(snsCheckDto.getSnsType(), snsCheckDto.getSnsId())).willReturn(userInfoDto);
        given(userService.doSignIn(userInfoDto)).willReturn(expectedUserDto);
        //when
        ResultActions result = mockMvc.perform(post("/user/signup/social/check")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(snsCheckDto))
                .with(csrf())).andDo(print());
        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.phone").exists())
                .andExpect(jsonPath("$.responseJwtDto").exists())
                .andDo(print());
    }

    @Nested
    @DisplayName("doSignUpNaverCheck test")
    class doSignUpNaverCheckTest {

        @Test
        @DisplayName("doSignInNaver Test")
        void doSignInNaverTest() throws Exception {
            //given
            UserDto.RequestNaverDto requestNaverDto = UserDto.RequestNaverDto.builder()
                    .code("naverCode")
                    .state("naverState")
                    .build();
            String accessToken = "naverAccessToken";
            UserDto.NaverUserProfileDto naverUserProfileDto = UserDto.NaverUserProfileDto.builder()
                    .snsId("naverId")
                    .name("naverName")
                    .build();
            UserDto.UserInfoDto userInfoDto = UserDto.UserInfoDto.builder()
                    .name("naverName")
                    .username("testid")
                    .phone("01012345678")
                    .snsId("naverId")
                    .snsType(SnsTypeEnum.NAVER)
                    .lastLoginRoleType(UserRoleEnum.MANAGER)
                    .joinDt(ZonedDateTime.now(ZoneId.of("UTC")))
                    .lastLoginDt(ZonedDateTime.now(ZoneId.of("UTC")))
                    .build();
            UserDto.ResponseJwtDto jwtDto = UserDto.ResponseJwtDto.builder()
                    .accessToken("accessToken")
                    .refreshToken("refreshToken")
                    .expiresIn(12345678L)
                    .build();
            UserDto.RoleDto roleDto = UserDto.RoleDto.builder()
                    .roleType(UserRoleEnum.MANAGER)
                    .branchId(0L)
                    .branchCount(0L)
                    .build();
            UserDto.ResponseUserInfoWithAuthDto expectedUserDto = UserDto.ResponseUserInfoWithAuthDto.toDto(userInfoDto, jwtDto, roleDto);
            given(userService.getNaverAccessToken(any())).willReturn(accessToken); //왜 any()?
            given(userService.getNaverUserProfileInfo(accessToken)).willReturn(naverUserProfileDto);
            given(userService.getUserInfoBySnsInfo(SnsTypeEnum.NAVER, naverUserProfileDto.getSnsId())).willReturn(userInfoDto);
            given(userService.doSignIn(userInfoDto)).willReturn(expectedUserDto);
            //when
            ResultActions result = mockMvc.perform(post("/user/signup/naver/check")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(requestNaverDto))
                    .with(csrf())).andDo(print());
            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.username").exists())
                    .andExpect(jsonPath("$.name").exists())
                    .andExpect(jsonPath("$.phone").exists())
                    .andExpect(jsonPath("$.snsId").exists())
                    .andExpect(jsonPath("$.responseJwtDto").exists())
                    .andDo(print());
        }

        @Test
        @DisplayName("return SnsId Test")
        void returnSnsIdTest() throws Exception {
            //given
            UserDto.RequestNaverDto naverCheckDto = UserDto.RequestNaverDto.builder()
                    .code("naverCode")
                    .state("naverState")
                    .build();
            String accessToken = "naverAccessToken";
            UserDto.NaverUserProfileDto naverUserProfileDto = UserDto.NaverUserProfileDto.builder()
                    .snsId("naverId")
                    .name("naverName")
                    .build();

            given(userService.getNaverAccessToken(any())).willReturn(accessToken); //왜 any()?
            given(userService.getNaverUserProfileInfo(accessToken)).willReturn(naverUserProfileDto);
            given(userService.getUserInfoBySnsInfo(SnsTypeEnum.NAVER, naverUserProfileDto.getSnsId())).willReturn(null);
            //when
            ResultActions result = mockMvc.perform(post("/user/signup/naver/check")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(naverCheckDto))
                    .with(csrf())).andDo(print());
            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.snsId").exists())
                    .andExpect(jsonPath("$.snsToken").exists())
                    .andExpect(jsonPath("$.responseJwtDto").doesNotExist())
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("doSignUpSocial Test")
    class doSignUpSocialTest{

        @Test
        @DisplayName("createTest")
        void createTest() throws Exception {
            //given
            UserDto.RequestUserSignUpSocialDto signUpDto = UserDto.RequestUserSignUpSocialDto.builder()
                    .name("testname")
                    .username("testid")
                    .password("asdf123!")
                    .phone("01012345678")
                    .snsId("testname")
                    .snsType(SnsTypeEnum.KAKAO)
                    .lastLoginRoleType(UserRoleEnum.MANAGER)
                    .build();
            UserDto.UserInfoDto userInfoDto = UserDto.UserInfoDto.toDto(signUpDto.toUserSignUpEntity());
            UserDto.ResponseJwtDto jwtDto = UserDto.ResponseJwtDto.builder()
                    .accessToken("accessToken")
                    .refreshToken("refreshToken")
                    .expiresIn(12345678L)
                    .build();
            UserDto.RoleDto roleDto = UserDto.RoleDto.builder()
                    .roleType(UserRoleEnum.MANAGER)
                    .branchId(0L)
                    .branchCount(0L)
                    .build();
            UserDto.ResponseUserInfoWithAuthDto expectedUserDto = UserDto.ResponseUserInfoWithAuthDto.toDto(userInfoDto, jwtDto, roleDto);
            given(userService.getUserInfoBySnsInfo(signUpDto.getSnsType(), signUpDto.getSnsId())).willReturn(null);
            given(userService.doSignUpSocial(any())).willReturn(userInfoDto);
            given(userService.doSignIn(userInfoDto)).willReturn(expectedUserDto);
            //when
            ResultActions result = mockMvc.perform(post("/user/signup/social")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(signUpDto))
                    .with(csrf())).andDo(print());
            //then
            result.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.username").exists())
                    .andExpect(jsonPath("$.name").exists())
                    .andExpect(jsonPath("$.phone").exists())
                    .andExpect(jsonPath("$.responseJwtDto").exists())
                    .andDo(print());
        }

        @Test
        @DisplayName("signInTest")
        void signInTest() throws Exception {
            //given
            UserDto.RequestUserSignUpSocialDto signUpDto = UserDto.RequestUserSignUpSocialDto.builder()
                    .name("testname")
                    .username("testid")
                    .password("asdf123!")
                    .phone("01012345678")
                    .snsId("testname")
                    .snsType(SnsTypeEnum.KAKAO)
                    .lastLoginRoleType(UserRoleEnum.MANAGER)
                    .build();
            UserDto.UserInfoDto userInfoDto = UserDto.UserInfoDto.toDto(signUpDto.toUserSignUpEntity());
            UserDto.ResponseJwtDto jwtDto = UserDto.ResponseJwtDto.builder()
                    .accessToken("accessToken")
                    .refreshToken("refreshToken")
                    .expiresIn(12345678L)
                    .build();
            UserDto.RoleDto roleDto = UserDto.RoleDto.builder()
                    .roleType(UserRoleEnum.MANAGER)
                    .branchId(0L)
                    .branchCount(0L)
                    .build();
            UserDto.ResponseUserInfoWithAuthDto expectedUserDto = UserDto.ResponseUserInfoWithAuthDto.toDto(userInfoDto, jwtDto, roleDto);
            given(userService.getUserInfoBySnsInfo(signUpDto.getSnsType(), signUpDto.getSnsId())).willReturn(userInfoDto);
            given(userService.doSignIn(userInfoDto)).willReturn(expectedUserDto);
            //when
            ResultActions result = mockMvc.perform(post("/user/signup/social")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(signUpDto))
                    .with(csrf())).andDo(print());
            //then
            result.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.username").exists())
                    .andExpect(jsonPath("$.name").exists())
                    .andExpect(jsonPath("$.phone").exists())
                    .andExpect(jsonPath("$.responseJwtDto").exists())
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("sendActivationCodeToPhone Test")
    class sendActivationCodeToPhoneTest{

        @Test
        @DisplayName("ok test")
        void okTest() throws Exception {
            //given
            ActivationCodeDto.RequestActivationCodeDto requestActivationCodeDto = ActivationCodeDto.RequestActivationCodeDto.builder()
                    .phone("01234567890")
                    .build();
            //when
            ResultActions result = mockMvc.perform(post("/user/activation-code/send/phone")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(requestActivationCodeDto))
                    .with(csrf())).andDo(print());
            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.systemTitle").value("01234567890으로 인증번호가 전송되었습니다."))
                    .andExpect(jsonPath("$.data").value(true))
                    .andDo(print());
        }

//        문자 전송 실패 test 필요
//        @Test
//        @DisplayName("CustomBooleanException test")
//        void customBooleanException() throws Exception {
//            //given
//            ActivationCodeDto.RequestActivationCodeDto requestActivationCodeDto = ActivationCodeDto.RequestActivationCodeDto.builder()
//                    .phone("01234567890")
//                    .build();
//            //when
//            ResultActions result = mockMvc.perform(post("/user/activation-code/send/phone")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsBytes(requestActivationCodeDto))
//                    .with(csrf())).andDo(print());
//            //then
//            result.andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.systemTitle").value("문자 발송중 오류가 발생했습니다."))
//                    .andExpect(jsonPath("$.data").value(true))
//                    .andDo(print());
//        }
    }

    @Test
    @DisplayName("signupVerifyActivationCode test")
    void signupVerifyActivationCodeTest() throws Exception{
        //given
        ActivationCodeDto.VerifyRequestDto verifyRequest = ActivationCodeDto.VerifyRequestDto.builder()
                .phone("01234567890")
                .activationCode("000000")
                .build();
        ActivationCodeDto.PhoneActivationInfoDto expectedActivationInfo = ActivationCodeDto.PhoneActivationInfoDto.builder()
                .activationCodeId(1L)
                .phone("01234567890")
                .activationCode("000000")
                .activationYn(true)
                .build();
        given(userService.verifyPhoneActivation(any())).willReturn(expectedActivationInfo);
        //when
        ResultActions result = mockMvc.perform(post("/user/signup/activation-code/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(verifyRequest))
                .with(csrf())).andDo(print());
        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.activationCodeId").exists())
                .andExpect(jsonPath("$.phone").exists())
                .andExpect(jsonPath("$.activationCode").exists())
                .andExpect(jsonPath("$.activationYn").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("verifyActivationCode test")
    void verifyActivationCodeTest() throws Exception {
        //given
        ActivationCodeDto.VerifyRequestDto verifyRequest = ActivationCodeDto.VerifyRequestDto.builder()
                .phone("01234567890")
                .activationCode("000000")
                .build();
        ActivationCodeDto.PhoneActivationInfoDto expectedActivationInfo = ActivationCodeDto.PhoneActivationInfoDto.builder()
                .activationCodeId(1L)
                .phone("01234567890")
                .activationCode("000000")
                .activationYn(true)
                .build();
        given(userService.verifyPhoneActivation(any())).willReturn(expectedActivationInfo);
        //when
        ResultActions result = mockMvc.perform(post("/user/activation-code/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(verifyRequest))
                .with(csrf())).andDo(print());
        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.activationCodeId").exists())
                .andExpect(jsonPath("$.phone").exists())
                .andExpect(jsonPath("$.activationCode").exists())
                .andExpect(jsonPath("$.activationYn").exists())
                .andDo(print());
    }

    @Nested
    @DisplayName("findId Test")
    class findIdTest{

        @Test
        @DisplayName("ok test")
        void okTest() throws Exception{
            //given
            UserDto.RequestFindIdDto requestFindIdDto = UserDto.RequestFindIdDto.builder()
                    .phone("01234567890")
                    .build();
            UserDto.UserInfoDto userInfoDto = UserDto.UserInfoDto.builder()
                    .username("test")
                    .build();
            given(userService.checkActivation(requestFindIdDto.getPhone())).willReturn(true);
            given(userService.getUserInfoByPhone(requestFindIdDto.getPhone())).willReturn(userInfoDto);

            //when
            ResultActions result = mockMvc.perform(post("/user/find/id")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(requestFindIdDto))
                    .with(csrf())).andDo(print());
            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").exists())
                    .andDo(print());
        }

        @Test
        @DisplayName("badRequest test")
        void badRequest() throws Exception{
            //given
            UserDto.RequestFindIdDto requestFindIdDto = UserDto.RequestFindIdDto.builder()
                    .phone("01234567890")
                    .build();
            UserDto.UserInfoDto userInfoDto = UserDto.UserInfoDto.builder()
                    .username("test")
                    .build();
            given(userService.checkActivation(requestFindIdDto.getPhone())).willReturn(false);

            //when
            ResultActions result = mockMvc.perform(post("/user/find/id")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(requestFindIdDto))
                    .with(csrf())).andDo(print());
            //then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.systemTitle").value("인증을 다시 진행해주세요."))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("passwordVerify Test")
    class passwordVerifyTest{

        @Test
        @DisplayName("ok test")
        void okTest() throws Exception{
            //given
            UserDto.RequestPasswordVerifyDto requestPasswordVerifyDto = UserDto.RequestPasswordVerifyDto.builder()
                    .username("testid")
                    .phone("01234567890")
                    .build();
           given(userService.checkActivation(requestPasswordVerifyDto.getPhone())).willReturn(true);
           //when
           ResultActions result = mockMvc.perform(post("/user/password/verify")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(requestPasswordVerifyDto))
                    .with(csrf())).andDo(print());
           //then
              result.andExpect(status().isOk())
                     .andDo(print());
        }

        @Test
        @DisplayName("badRequest test")
        void badRequestTest() throws Exception{
            //given
            UserDto.RequestPasswordVerifyDto requestPasswordVerifyDto = UserDto.RequestPasswordVerifyDto.builder()
                    .username("testid")
                    .phone("01234567890")
                    .build();
            given(userService.checkActivation(requestPasswordVerifyDto.getPhone())).willReturn(false);
            //when
            ResultActions result = mockMvc.perform(post("/user/password/verify")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(requestPasswordVerifyDto))
                    .with(csrf())).andDo(print());
            //then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.systemTitle").value("인증을 다시 진행해주세요."))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("resetPassword Test")
    class resetPasswordTest{

        @Test
        @DisplayName("ok test")
        void okTest() throws Exception{
            //given
            UserDto.RequestResetPassword requestResetPassword = UserDto.RequestResetPassword.builder()
                    .username("testid")
                    .password("asdf1234!")
                    .build();
            //when
            ResultActions result = mockMvc.perform(patch("/user/reset/password")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(requestResetPassword))
                    .with(csrf())).andDo(print());
            //then
            result.andExpect(status().isOk())
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("activateUser Test")
    class activateUserTest{

        @Test
        @DisplayName("ok test")
        void okTest() throws Exception{
            //given
            Long userId = 1L;
            given(userService.doActivateUser(any())).willReturn(true);
            //when
            ResultActions result = mockMvc.perform(post("/user/activate")
                    .contentType(MediaType.APPLICATION_JSON)
                            .requestAttr("userId", userId)
                    .with(csrf())).andDo(print());
            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.systemTitle").value("휴면 해제가 완료되었습니다."))
                    .andExpect(jsonPath("$.data").value(true))
                    .andDo(print());
        }

        @Test
        @DisplayName("badRequest test")
        void badRequestTest() throws Exception{
            //given
            Long userId = 1L;
            given(userService.doActivateUser(any())).willReturn(false);
            //when
            ResultActions result = mockMvc.perform(post("/user/activate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .requestAttr("userId", userId)
                    .with(csrf())).andDo(print());
            //then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.systemTitle").value("휴면 해제에 실패했습니다."))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("createTemporaryUser Test")
    class createTemporaryUserTest{

        @Test
        @DisplayName("created test")
        void createdTest() throws Exception{
            //given
            UserDto.RequestTempUserSignUpDto requestTempUserSignUpDto = UserDto.RequestTempUserSignUpDto.builder()
                    .name("test")
                    .phone("01234567890")
                    .lastLoginRoleType(UserRoleEnum.MANAGER)
                    .build();
            UserDto.UserInfoDto userInfoDto = UserDto.UserInfoDto.builder()
                    .username("test7890")
                    .phone("01234567890")
                    .build();
            given(userService.createTemporaryUser(any())).willReturn(userInfoDto);
            //when
            ResultActions result = mockMvc.perform(post("/user/temporary")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(requestTempUserSignUpDto))
                    .with(csrf())).andDo(print());
            //then
            result.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.username").value("test7890"))
                    .andExpect(jsonPath("$.phone").value("01234567890"))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("getUser Test")
    class getUserTest{

        @Test
        @DisplayName("ok test")
        void okTest() throws Exception{
            //given
            User expectedUser = User.builder()
                    .id(1L)
                    .username("testid")
                    .name("테스트")
                    .phone("01234567890")
                    .build();
            String phone = expectedUser.getPhone();
            UserDto.ResponseUser expectedResponseUser = UserDto.ResponseUser.toDto(expectedUser);
            given(userService.getUser(any())).willReturn(expectedResponseUser);
            //when
            ResultActions result = mockMvc.perform(get("/user/search/phone?phone={phone}", phone)
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(csrf())).andDo(print());
            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.username").value("test**"))
                    .andExpect(jsonPath("$.name").value("테스*"))
                    .andExpect(jsonPath("$.phone").value("01234567890"))
                    .andDo(print());
        }

    }
}
