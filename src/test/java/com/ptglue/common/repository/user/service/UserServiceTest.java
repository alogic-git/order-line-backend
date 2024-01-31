package com.ptglue.common.repository.user.service;

import com.ptglue.basic.config.security.JwtTokenProvider;
import com.ptglue.basic.service.AwsS3Service;
import com.ptglue.branch.model.entity.Branch;
import com.ptglue.branch.model.entity.BranchUserRole;
import com.ptglue.branch.repository.BranchUserRoleRepository;
import com.ptglue.common.user.enums.SnsTypeEnum;
import com.ptglue.common.user.enums.UserOutTypeEnum;
import com.ptglue.common.user.enums.UserRoleEnum;
import com.ptglue.common.user.model.dto.ActivationCodeDto;
import com.ptglue.common.user.model.dto.UserDto;
import com.ptglue.common.user.model.entity.*;
import com.ptglue.common.user.repository.*;
import com.ptglue.common.user.service.CustomUserDetailsService;
import com.ptglue.basic.exception.InternalServerErrorException;
import com.ptglue.basic.exception.NotFoundException;
import com.ptglue.common.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BranchUserRoleRepository branchUserRoleRepository;
    @Mock
    private UserTokenRepository userTokenRepository;
    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private UserSnsRepository userSnsRepository;
    @Mock
    private AwsS3Service awsS3Service;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ActivationCodeRepository activationCodeRepository;

    @Mock
    private UserOutRestRepository userOutRestRepository;

    private static final Long ACTIVATION_CODE_EXPIRATION_TIME = 60 * 60 * 24L;

    public User makeUser(Long id) {
        return User.builder()
                .id(id)
                .username("testuserid")
                .password("password")
                .name("test user")
                .phone("11111111111")
                .phoneActiveYn(true)
                .joinDt(ZonedDateTime.now(ZoneId.of("UTC")))
                .lastLoginDt(ZonedDateTime.now(ZoneId.of("UTC")))
                .build();
    }

    public UserToken makeUserToken(Long id, User user, Boolean use) {
        return UserToken.builder()
                .id(id)
                .user(user)
                .refreshToken("refreshToken")
                .build();
    }

    public BranchUserRole makeBranchManager(User user) {
        return BranchUserRole.builder()
                .id(4L)
                .user(user)
                .branch(Branch.builder().id(1L).build())
                .roleType(UserRoleEnum.MANAGER)
                .build();
    }


    public UserSns makeSnsInfo(User user) {
        return UserSns.builder()
                .user(user)
                .snsId("1234567")
                .snsType(SnsTypeEnum.KAKAO)
                .build();
    }

    public ActivationCode makeActivationCode() {
        return ActivationCode.builder()
                .phone("01012345678")
                .activationCode("123456")
                .activationYn(false)
                .build();
    }

    public UserOutRest makeUserOutRest(User user) {
        return UserOutRest.builder()
                .id(1L)
                .phone(user.getPhone())
                .build();
    }

    @Test
    @DisplayName("userService Layer doSignIn Test")
    void doSignInTest() {
        //given
        User user = makeUser(1L);
        UserDto.UserInfoDto userInfoDto = UserDto.UserInfoDto.toDto(user);
        BranchUserRole branchManger = makeBranchManager(user);
        UserDto.RoleDto roleDto = UserDto.RoleDto.builder()
                .roleType(branchManger.getRoleType())
                .branchId(branchManger.getBranch().getId())
                .branchCount(1L)
                .build();
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        Long expiresIn = 12345678L;
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(customUserDetailsService.getUserRoleById(1L)).willReturn(roleDto);
        given(jwtTokenProvider.createAccessToken(String.valueOf(user.getId()), roleDto)).willReturn(accessToken);
        given(jwtTokenProvider.createRefreshToken()).willReturn(refreshToken);
        given(jwtTokenProvider.getExpiresIn(accessToken)).willReturn(expiresIn);
        UserDto.ResponseJwtDto jwtDto = UserDto.ResponseJwtDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(expiresIn)
                .build();
        UserDto.ResponseUserInfoWithAuthDto expectedUserInfoDto
                = UserDto.ResponseUserInfoWithAuthDto.toDto(userInfoDto, jwtDto, roleDto);
        //when
        UserDto.ResponseUserInfoWithAuthDto actualUserInfoDto = userService.doSignIn(userInfoDto);
        //then
        assertThat(actualUserInfoDto).usingRecursiveComparison().isEqualTo(expectedUserInfoDto);
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any(User.class));
        verify(userTokenRepository, times(1)).save(any(UserToken.class));
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(userTokenRepository);
    }

    @Nested
    @DisplayName("userService Layer getUserInfoByUsernameAndPassword Test")
    class getUserInfoByUsernameAndPasswordTest {
        @Test
        @DisplayName("userService Layer getUserInfoByUsernameAndPassword Test")
        void getUserInfoByUsernameAndPasswordTest() {
            //given
            User user = makeUser(1L);
            String username = user.getUsername();
            String password = user.getPassword();
            given(userRepository.findByUsername(username)).willReturn(Optional.of(user));
            given(passwordEncoder.matches(password, user.getPassword())).willReturn(true);
            UserDto.UserInfoDto expectedUserInfoDto = UserDto.UserInfoDto.toDto(user);
            //when
            UserDto.UserInfoDto actualUserInfoDto = userService.getUserInfoByUsernameAndPassword(username, password);
            //then
            assertThat(actualUserInfoDto).usingRecursiveComparison().isEqualTo(expectedUserInfoDto);
            verify(userRepository, times(1)).findByUsername(anyString());
            verifyNoMoreInteractions(userRepository);
        }

        @Test
        @DisplayName("userService Layer badRequest Test")
        void badRequestTest() {
            //given
            User user = makeUser(1L);
            String username = user.getUsername();
            String password = user.getPassword();
            given(userRepository.findByUsername(username)).willReturn(Optional.of(user));
            given(passwordEncoder.matches(password, user.getPassword())).willThrow(new InternalServerErrorException("ID/비밀번호를 확인해주세요."));
            //when
            InternalServerErrorException internalServerErrorException = assertThrows(InternalServerErrorException.class, () -> userService.getUserInfoByUsernameAndPassword(username, password));
            //then
            assertEquals(internalServerErrorException.getMessage(), "ID/비밀번호를 확인해주세요.");
            verify(userRepository, times(1)).findByUsername(anyString());
            verifyNoMoreInteractions(userRepository);
        }
    }

    @Test
    @DisplayName("userService Layer getUserInfoBySnsInfo Test")
    void getUserInfoBySnsInfoTest() {
        //given
        User user = makeUser(1L);
        UserSns userSns = makeSnsInfo(user);
        SnsTypeEnum snsType = userSns.getSnsType();
        String snsId = userSns.getSnsId();
        UserDto.UserInfoDto expectedUserInfoDto = UserDto.UserInfoDto.toDto(user);
        given(userSnsRepository.findBySnsTypeAndSnsId(snsType, snsId)).willReturn(Optional.of(userSns));
        //when
        UserDto.UserInfoDto actualUserInfoDto = userService.getUserInfoBySnsInfo(snsType, snsId);
        //then
        assertThat(actualUserInfoDto).usingRecursiveComparison().isEqualTo(expectedUserInfoDto);
        verify(userSnsRepository, times(1)).findBySnsTypeAndSnsId(any(), anyString());
        verifyNoMoreInteractions(userSnsRepository);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("userService Layer doSignOut Test")
    void doSignOutTest() {
        //given
        User user = makeUser(1L);
        UserToken userToken1 = makeUserToken(1L, user, true);
        UserToken userToken2 = makeUserToken(2L, user, true);
        UserToken deleteToken1 = makeUserToken(1L, user, false);
        UserToken deleteToken2 = makeUserToken(2L, user, false);
        List<UserToken> userTokens = new ArrayList<>();
        userTokens.add(userToken1);
        userTokens.add(userToken2);
        given(userTokenRepository.findByUser_Id(user.getId())).willReturn(userTokens);
        given(userTokenRepository.save(userToken1)).willReturn(deleteToken1);
        given(userTokenRepository.save(userToken2)).willReturn(deleteToken2);
        //when
        userService.doSignOut(user.getId());
        //then
        assertFalse(userTokenRepository.existsById(1L));
        assertFalse(userTokenRepository.existsById(2L));
        verify(userTokenRepository, times(1)).findByUser_Id(anyLong());
        verify(userTokenRepository, times(2)).save(any());
    }

    @Test
    @DisplayName("userService Layer getUserInfoByUsername Test")
    void getUserInfoByUsernameTest() {
        //given
        String username = "newUsername";
        given(userRepository.findByUsername(username)).willReturn(Optional.empty());
        //when
        Long userId = userService.getUserInfoByUsername(username);
        //then
        assertThat(userId).isZero();
        verify(userRepository, times(1)).findByUsername(anyString());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("userService Layer doSignUp Test")
    void doSignUpTest() {
        //given
        User expectedUser = makeUser(1L);
        BranchUserRole expectedUserRole = makeBranchManager(expectedUser);
        given(userRepository.save(any())).willReturn(expectedUser);
        given(branchUserRoleRepository.save(any())).willReturn(expectedUserRole);
        UserDto.UserInfoDto expectedUserInfoDto = UserDto.UserInfoDto.toDto(expectedUser);
        UserDto.RequestUserSignUpDto signUpDto = UserDto.RequestUserSignUpDto.builder()
                .name(expectedUser.getName())
                .username(expectedUser.getUsername())
                .password(expectedUser.getPassword())
                .lastLoginRoleType(expectedUserRole.getRoleType())
                .build();

        //when
        UserDto.UserInfoDto actualUserInfoDto = userService.doSignUp(signUpDto);
        //then
        assertThat(actualUserInfoDto).usingRecursiveComparison().isEqualTo(expectedUserInfoDto);
        verify(userRepository, times(1)).save(any());
        verify(branchUserRoleRepository, times(1)).save(any());
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(branchUserRoleRepository);
    }

    @Test
    @DisplayName("userService Layer doSignUpSocial Test")
    void doSignUpSocialTest() {
        //given
        User user = makeUser(1L);
        BranchUserRole expectedUserRole = makeBranchManager(user);
        UserSns userSns = makeSnsInfo(user);
        UserDto.RequestUserSignUpSocialDto signUpDto = UserDto.RequestUserSignUpSocialDto.builder()
                .snsId(userSns.getSnsId())
                .snsType(userSns.getSnsType())
                .lastLoginRoleType(expectedUserRole.getRoleType())
                .build();
        given(userRepository.save(any())).willReturn(user);
        given(branchUserRoleRepository.save(any())).willReturn(expectedUserRole);
        given(userSnsRepository.save(any())).willReturn(userSns);
        UserDto.UserInfoDto expectedUserInfoDto = UserDto.UserInfoDto.toDto(user);
        //when
        UserDto.UserInfoDto actualUserInfoDto = userService.doSignUpSocial(signUpDto);
        //then
        assertThat(actualUserInfoDto).usingRecursiveComparison().isEqualTo(expectedUserInfoDto);
        verify(userRepository, times(1)).save(any());
        verify(userSnsRepository, times(1)).save(any());
        verify(branchUserRoleRepository, times(1)).save(any());
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(branchUserRoleRepository);
        verifyNoMoreInteractions(userSnsRepository);
    }

    //getNaverAccessToken 작성 방법 확인 필요

    //getNaverUserProfileInfo 작성 방법 확인 필요

    @Test
    @DisplayName("createPhoneActivationInfo Test")
    void createPhoneActivationInfoTest() {
        //given
        ActivationCodeDto.PhoneActivationInfoDto phoneActivationInfoDto = ActivationCodeDto.PhoneActivationInfoDto.builder()
                .phone("01012345678")
                .activationCode("123456")
                .build();
        //when
        userService.createPhoneActivationInfo(phoneActivationInfoDto);
        //then
        verify(activationCodeRepository, times(1)).save(any());
        verifyNoMoreInteractions(activationCodeRepository);
    }

    @Nested
    @DisplayName("verifyPhoneActivation Test")
    class verifyPhoneActivation {
        @Test
        @DisplayName("ok Test")
        void okTest() {
            //given
            ActivationCode expectedActivationCode = makeActivationCode();
            expectedActivationCode.createDateTime(LocalDateTime.now());
            ActivationCodeDto.VerifyRequestDto verifyRequestDto = ActivationCodeDto.VerifyRequestDto.builder()
                    .phone("01012345678")
                    .activationCode("123456")
                    .build();
            given(activationCodeRepository.findFirstByPhoneOrderByRegDateTimeDesc(anyString())).willReturn(expectedActivationCode);
            //when
            ActivationCodeDto.PhoneActivationInfoDto actualVerifyResponseDto = userService.verifyPhoneActivation(verifyRequestDto);
            //then
            assertTrue(actualVerifyResponseDto.getActivationYn());
            verify(activationCodeRepository, times(1)).findFirstByPhoneOrderByRegDateTimeDesc(anyString());
            verify(activationCodeRepository, times(1)).save(any());
            verifyNoMoreInteractions(activationCodeRepository);
        }

        @Test
        @DisplayName("notFound Test")
        void notFound() {
            //given
            ActivationCode expectedActivationCode = makeActivationCode();
            expectedActivationCode.createDateTime(LocalDateTime.now());
            ActivationCodeDto.VerifyRequestDto verifyRequestDto = ActivationCodeDto.VerifyRequestDto.builder()
                    .phone("01012345678")
                    .activationCode("123456")
                    .build();
            given(activationCodeRepository.findFirstByPhoneOrderByRegDateTimeDesc(anyString())).willReturn(null);
            //when
            NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> userService.verifyPhoneActivation(verifyRequestDto));
            //then
            assertEquals(notFoundException.getMessage(), "인증번호 받기를 클릭해주세요");
            verify(activationCodeRepository, times(1)).findFirstByPhoneOrderByRegDateTimeDesc(anyString());
            verifyNoMoreInteractions(activationCodeRepository);
        }

        @Test
        @DisplayName("badRequest Test")
        void badRequest() {
            //given
            ActivationCode expectedActivationCode = makeActivationCode();
            expectedActivationCode.createDateTime(LocalDateTime.now());
            ActivationCodeDto.VerifyRequestDto verifyRequestDto = ActivationCodeDto.VerifyRequestDto.builder()
                    .phone("01012345678")
                    .activationCode("999999") // 틀린인증번호
                    .build();
            given(activationCodeRepository.findFirstByPhoneOrderByRegDateTimeDesc(anyString())).willReturn(expectedActivationCode);
            //when
            InternalServerErrorException internalServerErrorException = assertThrows(InternalServerErrorException.class, () -> userService.verifyPhoneActivation(verifyRequestDto));
            //then
            assertEquals(internalServerErrorException.getMessage(), "인증번호가 다릅니다. 확인 후 다시 입력해주세요.");
            verify(activationCodeRepository, times(1)).findFirstByPhoneOrderByRegDateTimeDesc(anyString());
            verifyNoMoreInteractions(activationCodeRepository);
        }

        @Test
        @DisplayName("badRequest expired Test")
        void badRequestExpired() {
            //given
            ActivationCode expectedActivationCode = makeActivationCode();
            expectedActivationCode.createDateTime(LocalDateTime.of(2021, 1, 1, 0, 0, 0));
            ActivationCodeDto.VerifyRequestDto verifyRequestDto = ActivationCodeDto.VerifyRequestDto.builder()
                    .phone("01012345678")
                    .activationCode("123456")
                    .build();
            given(activationCodeRepository.findFirstByPhoneOrderByRegDateTimeDesc(anyString())).willReturn(expectedActivationCode);
            //when
            InternalServerErrorException internalServerErrorException = assertThrows(InternalServerErrorException.class, () -> userService.verifyPhoneActivation(verifyRequestDto));
            //then
            assertEquals(internalServerErrorException.getMessage(), "입력 시간이 초과되었습니다. 인증 번호를 다시 받아주세요.");
            verify(activationCodeRepository, times(1)).findFirstByPhoneOrderByRegDateTimeDesc(anyString());
            verify(activationCodeRepository, times(1)).save(any());
            verifyNoMoreInteractions(activationCodeRepository);
        }
    }

    @Nested
    @DisplayName("checkPhone Test")
    class checkPhone {
        @Test
        @DisplayName("ok Test")
        void okTest() {
            //given
            String phone = "99999999999";
            given(userRepository.findAllByPhone(phone)).willReturn(new ArrayList<>());
            given(userOutRestRepository.findAllByPhone(phone)).willReturn(new ArrayList<>());
            //when
            userService.checkPhone(phone);
            //then
            verify(userRepository, times(1)).findAllByPhone(phone);
            verify(userOutRestRepository, times(1)).findAllByPhone(phone);
            verifyNoMoreInteractions(userRepository);
            verifyNoMoreInteractions(userOutRestRepository);
        }

        @Test
        @DisplayName("badRequest Test(이미 가입된 번호입니다.)")
        void badRequestUser() {
            //given
            User user = makeUser(1L);
            String phone = "99999999999";
            given(userRepository.findAllByPhone(phone)).willReturn(Collections.singletonList(user));
            //when
            InternalServerErrorException internalServerErrorException = assertThrows(InternalServerErrorException.class, () -> userService.checkPhone(phone));
            //then
            assertEquals(internalServerErrorException.getMessage(), "이미 가입된 번호입니다.");
            verify(userRepository, times(1)).findAllByPhone(phone);
            verifyNoMoreInteractions(userRepository);
        }

        @Test
        @DisplayName("badRequest Test(휴면 계정 휴대폰 번호입니다.)")
        void badRequestUserOutRest() {
            //given
            User user = makeUser(1L);
            UserOutRest userOutRest = makeUserOutRest(user);
            String phone = "99999999999";
            given(userRepository.findAllByPhone(phone)).willReturn(new ArrayList<>());
            given(userOutRestRepository.findAllByPhone(phone)).willReturn(Collections.singletonList(userOutRest));
            //when
            InternalServerErrorException internalServerErrorException = assertThrows(InternalServerErrorException.class, () -> userService.checkPhone(phone));
            //then
            assertEquals(internalServerErrorException.getMessage(), "휴면 계정 휴대폰 번호입니다.");
            verify(userRepository, times(1)).findAllByPhone(phone);
            verify(userOutRestRepository, times(1)).findAllByPhone(phone);
            verifyNoMoreInteractions(userRepository);
            verifyNoMoreInteractions(userOutRestRepository);
        }
    }

    @Nested
    @DisplayName("checkActivation Test")
    class checkActivationTest {

        @Test
        @DisplayName("ok Test")
        void okTest() {
            //given
            ActivationCode expectedActivationCode = makeActivationCode();
            expectedActivationCode.updateActivationStatus();
            expectedActivationCode.modifiedDateTime(LocalDateTime.now());
            String phone = expectedActivationCode.getPhone();
            given(activationCodeRepository.findFirstByPhoneOrderByModDateTimeDesc(phone)).willReturn(expectedActivationCode);
            //when
            boolean checkActivation = userService.checkActivation(phone);
            //then
            assertThat(checkActivation).isTrue();
            verify(activationCodeRepository, times(1)).findFirstByPhoneOrderByModDateTimeDesc(phone);
            verifyNoMoreInteractions(activationCodeRepository);
        }

        @Test
        @DisplayName("NotFound Test")
        void notFoundTest() {
            //given
            given(activationCodeRepository.findFirstByPhoneOrderByModDateTimeDesc(anyString())).willReturn(null);
            //when
            NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> userService.checkActivation(anyString()));
            //then
            assertEquals(notFoundException.getMessage(), "인증번호를 먼저 요청해주세요.");
            verify(activationCodeRepository, times(1)).findFirstByPhoneOrderByModDateTimeDesc(anyString());
            verifyNoMoreInteractions(activationCodeRepository);
        }

        @Test
        @DisplayName("BadRequest(인증이 완료되지 않았습니다.) Test")
        void badRequestTest() {
            //given
            ActivationCode expectedActivationCode = makeActivationCode();
            expectedActivationCode.modifiedDateTime(LocalDateTime.now());
            String phone = expectedActivationCode.getPhone();
            given(activationCodeRepository.findFirstByPhoneOrderByModDateTimeDesc(phone)).willReturn(expectedActivationCode);
            //when
            InternalServerErrorException internalServerErrorException = assertThrows(InternalServerErrorException.class, () -> userService.checkActivation(phone));
            //then
            assertEquals(internalServerErrorException.getMessage(), "인증이 완료되지 않았습니다.");
            verify(activationCodeRepository, times(1)).findFirstByPhoneOrderByModDateTimeDesc(phone);
            verifyNoMoreInteractions(activationCodeRepository);
        }

        @Test
        @DisplayName("BadRequest(인증이 만료되었습니다.) Test")
        void badRequestExpiredTest() {
            //given
            ActivationCode expectedActivationCode = makeActivationCode();
            expectedActivationCode.updateActivationStatus();
            ZonedDateTime modifiedDateTime = ZonedDateTime.now(ZoneId.of("UTC")).minusSeconds(ACTIVATION_CODE_EXPIRATION_TIME + 1);
            expectedActivationCode.modifiedDateTime(modifiedDateTime.toLocalDateTime());
            String phone = expectedActivationCode.getPhone();
            given(activationCodeRepository.findFirstByPhoneOrderByModDateTimeDesc(phone)).willReturn(expectedActivationCode);
            //when
            InternalServerErrorException internalServerErrorException = assertThrows(InternalServerErrorException.class, () -> userService.checkActivation(phone));
            //then
            assertEquals(internalServerErrorException.getMessage(), "인증이 만료되었습니다.");
            verify(activationCodeRepository, times(1)).findFirstByPhoneOrderByModDateTimeDesc(phone);
            verifyNoMoreInteractions(activationCodeRepository);
        }
}

    @Nested
    @DisplayName("getUserInfoByPhone Test")
    class getUserInfoByPhoneTest{

        @Test
        @DisplayName("ok Test")
        void okTest() {
            //given
            User user = makeUser(1L);
            String phone = user.getPhone();
            given(userRepository.findFirstByPhoneOrderByModDateTimeDesc(phone)).willReturn(user);
            //when
            UserDto.UserInfoDto actualUserInfoDto = userService.getUserInfoByPhone(phone);
            //then
            assertThat(actualUserInfoDto).usingRecursiveComparison().isEqualTo(UserDto.UserInfoDto.toDto(user));
            verify(userRepository, times(1)).findFirstByPhoneOrderByModDateTimeDesc(phone);
            verifyNoMoreInteractions(userRepository);
        }

        @Test
        @DisplayName("ok(휴면회원) Test")
        void okRestUserTest() {
            //given
            User user = makeUser(1L);
            String phone = user.getPhone();
            UserOutRest userOutRest = makeUserOutRest(user);
            given(userRepository.findFirstByPhoneOrderByModDateTimeDesc(phone)).willReturn(null);
            given(userOutRestRepository.findFirstByPhoneOrderByModDateTimeDesc(phone)).willReturn(userOutRest);
            //when
            UserDto.UserInfoDto actualUserInfoDto = userService.getUserInfoByPhone(phone);
            //then
            assertThat(actualUserInfoDto).usingRecursiveComparison().isEqualTo(UserDto.UserInfoDto.toDto(userOutRest));
            verify(userRepository, times(1)).findFirstByPhoneOrderByModDateTimeDesc(phone);
            verify(userOutRestRepository, times(1)).findFirstByPhoneOrderByModDateTimeDesc(phone);
            verifyNoMoreInteractions(userRepository);
            verifyNoMoreInteractions(userOutRestRepository);
        }

        @Test
        @DisplayName("noFound Test")
        void noFoundTest() {
            //given
            User user = makeUser(1L);
            String phone = user.getPhone();
            given(userRepository.findFirstByPhoneOrderByModDateTimeDesc(phone)).willReturn(null);
            given(userOutRestRepository.findFirstByPhoneOrderByModDateTimeDesc(phone)).willReturn(null);
            //when
            NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> userService.getUserInfoByPhone(phone));
            //then
            assertEquals(notFoundException.getMessage(), "해당 휴대전화로 가입된 정보가 없습니다.");
            verify(userRepository, times(1)).findFirstByPhoneOrderByModDateTimeDesc(phone);
            verify(userOutRestRepository, times(1)).findFirstByPhoneOrderByModDateTimeDesc(phone);
            verifyNoMoreInteractions(userRepository);
            verifyNoMoreInteractions(userOutRestRepository);
        }
    }

    @Nested
    @DisplayName("getUserInfoByUsernameAndPhoneTest")
    class getUserInfoByUsernameAndPhoneTest{

        @Test
        @DisplayName("okTest")
        void okTest() {
            //given
            User user = makeUser(1L);
            UserDto.RequestPasswordVerifyDto requestPasswordVerifyDto = UserDto.RequestPasswordVerifyDto.builder()
                    .username(user.getUsername())
                    .phone(user.getPhone())
                    .build();
            String username = requestPasswordVerifyDto.getUsername();
            String phone = requestPasswordVerifyDto.getPhone();
            given(userRepository.findByUsernameAndPhone(username, phone)).willReturn(user);
            //when
            UserDto.UserInfoDto actualUserInfoDto = userService.getUserInfoByUsernameAndPhone(requestPasswordVerifyDto);
            //then
            assertThat(actualUserInfoDto).usingRecursiveComparison().isEqualTo(UserDto.UserInfoDto.toDto(user));
            verify(userRepository, times(1)).findByUsernameAndPhone(anyString(), anyString());
            verifyNoMoreInteractions(userRepository);
        }

        @Test
        @DisplayName("okRestUserTest")
        void okRestUserTest() {
            //given
            User user = makeUser(1L);
            UserDto.RequestPasswordVerifyDto requestPasswordVerifyDto = UserDto.RequestPasswordVerifyDto.builder()
                    .username(user.getUsername())
                    .phone(user.getPhone())
                    .build();
            UserOutRest userOutRest = makeUserOutRest(user);
            String username = requestPasswordVerifyDto.getUsername();
            String phone = requestPasswordVerifyDto.getPhone();
            given(userRepository.findByUsernameAndPhone(username, phone)).willReturn(null);
            given(userOutRestRepository.findByUsernameAndPhone(username, phone)).willReturn(userOutRest);
            //when
            UserDto.UserInfoDto actualUserInfoDto = userService.getUserInfoByUsernameAndPhone(requestPasswordVerifyDto);
            //then
            assertThat(actualUserInfoDto).usingRecursiveComparison().isEqualTo(UserDto.UserInfoDto.toDto(userOutRest));
            verify(userRepository, times(1)).findByUsernameAndPhone(username, phone);
            verify(userOutRestRepository, times(1)).findByUsernameAndPhone(username, phone);
            verifyNoMoreInteractions(userRepository);
            verifyNoMoreInteractions(userOutRestRepository);
        }

        @Test
        @DisplayName("NotFoundException")
        void NotFoundException() {
            //given
            User user = makeUser(1L);
            UserDto.RequestPasswordVerifyDto requestPasswordVerifyDto = UserDto.RequestPasswordVerifyDto.builder()
                    .username(user.getUsername())
                    .phone(user.getPhone())
                    .build();
            String username = requestPasswordVerifyDto.getUsername();
            String phone = requestPasswordVerifyDto.getPhone();
            given(userRepository.findByUsernameAndPhone(username, phone)).willReturn(null);
            given(userOutRestRepository.findByUsernameAndPhone(username, phone)).willReturn(null);
            //when
            NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> userService.getUserInfoByUsernameAndPhone(requestPasswordVerifyDto));
            //then
            assertEquals(notFoundException.getMessage(), "해당 휴대전화로 가입된 정보가 없습니다.");
            verify(userRepository, times(1)).findByUsernameAndPhone(username, phone);
            verify(userOutRestRepository, times(1)).findByUsernameAndPhone(username, phone);
            verifyNoMoreInteractions(userRepository);
            verifyNoMoreInteractions(userOutRestRepository);
        }

    }

    @Nested
    @DisplayName("resetPasswordTest")
    class resetPasswordTest{

        @Test
        @DisplayName("ok Test")
        void okTest() {
            User user = makeUser(1L);
            String newPassword = "newpassword";
            UserDto.RequestResetPassword requestPasswordResetDto = UserDto.RequestResetPassword.builder()
                    .username(user.getUsername())
                    .password(newPassword)
                    .build();
            String username = requestPasswordResetDto.getUsername();
            given(userRepository.findByUsername(username)).willReturn(Optional.of(user));
            given(userRepository.save(user)).willReturn(user);
            //when
            Long actualUserId = userService.resetPassword(requestPasswordResetDto);
            //then
            assertThat(actualUserId).isEqualTo(user.getId());
            verify(userRepository, times(1)).findByUsername(username);
            verify(userRepository, times(1)).save(user);
            verifyNoMoreInteractions(userRepository);
        }

        @Test
        @DisplayName("NotFoundException Test")
        void NotFoundException() {
            User user = makeUser(1L);
            String newPassword = "newpassword";
            UserDto.RequestResetPassword requestPasswordResetDto = UserDto.RequestResetPassword.builder()
                    .username(user.getUsername())
                    .password(newPassword)
                    .build();
            String username = requestPasswordResetDto.getUsername();
            given(userRepository.findByUsername(username)).willReturn(Optional.empty());
            //when
            NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> userService.resetPassword(requestPasswordResetDto));
            //then
            assertEquals(notFoundException.getMessage(), "해당 회원이 존재하지 않습니다.");
            verify(userRepository, times(1)).findByUsername(username);
            verifyNoMoreInteractions(userRepository);
        }

    }

    @Nested
    @DisplayName("doActivateUser")
    class doActivateUser{

        @Test
        @DisplayName("ok Test")
        void returnTrue() {
            //given
            User user = makeUser(1L);
            UserOutRest userOutRest = makeUserOutRest(user);
            Long userId = user.getId();
            given(userRepository.findById(userId)).willReturn(Optional.of(user));
            given(userOutRestRepository.findByUserId(userId)).willReturn(Optional.of(userOutRest));
            //when
            boolean activateYn = userService.doActivateUser(userId);
            //then
            assertTrue(activateYn);
            Assertions.assertThat(user.getOutType()).isEqualTo(UserOutTypeEnum.ACTIVE);
            org.junit.jupiter.api.Assertions.assertTrue(userOutRest.getDeleteYn());
            verify(userRepository, times(1)).findById(userId);
            verify(userOutRestRepository, times(1)).findByUserId(userId);
            verify(userRepository, times(1)).save(user);
            verify(userOutRestRepository, times(1)).save(userOutRest);
            verifyNoMoreInteractions(userRepository);
            verifyNoMoreInteractions(userOutRestRepository);
        }

        @Test
        @DisplayName("doActivateUser Test")
        void returnFalse() {
            //given
            User user = makeUser(1L);
            UserOutRest userOutRest = makeUserOutRest(user);
            Long userId = user.getId();
            given(userRepository.findById(userId)).willReturn(Optional.empty());
            given(userOutRestRepository.findByUserId(userId)).willReturn(Optional.empty());
            //when
            boolean activateYn = userService.doActivateUser(userId);
            //then
            assertFalse(activateYn);
            org.junit.jupiter.api.Assertions.assertFalse(userOutRest.getDeleteYn());
            verify(userRepository, times(1)).findById(userId);
            verify(userOutRestRepository, times(1)).findByUserId(userId);
            verifyNoMoreInteractions(userRepository);
            verifyNoMoreInteractions(userOutRestRepository);
        }

    }


    @Nested
    @DisplayName("createTemporaryUser")
    class createTemporaryUser{

        @Test
        @DisplayName("ok Test")
        void ok(){
            //given
            UserDto.RequestTempUserSignUpDto signUpDto = UserDto.RequestTempUserSignUpDto.builder()
                    .name("김임시")
                    .phone("01012345678")
                    .build();
            given(userRepository.findByUsername(anyString())).willReturn(Optional.empty());
            String expectedUsername = "김임시5678";
            //when
            UserDto.UserInfoDto actualUserInfoDto = userService.createTemporaryUser(signUpDto);
            //then
            assertThat(actualUserInfoDto.getUsername()).isEqualTo(expectedUsername);
            verify(userRepository, times(1)).findByUsername(anyString());
            verify(userRepository, times(1)).save(any());
            verifyNoMoreInteractions(userRepository);
        }

        @Test
        @DisplayName("InternalServerErrorException")
        void InternalServerErrorException(){
            //given
            User user = makeUser(1L);
            UserDto.RequestTempUserSignUpDto signUpDto = UserDto.RequestTempUserSignUpDto.builder()
                    .name("김임시")
                    .phone("01012345678")
                    .build();
            given(userRepository.findByUsername(anyString())).willReturn(Optional.of(user));
            String expectedUsername = "김임시5678";
            //when
            InternalServerErrorException internalServerErrorException = assertThrows(InternalServerErrorException.class, () -> userService.createTemporaryUser(signUpDto));
            //then
            assertEquals(internalServerErrorException.getMessage(), "임시 아이디 발급에 실패했습니다. 다시 시도해주세요.");
            verify(userRepository, times(11)).findByUsername(anyString());
            verifyNoMoreInteractions(userRepository);
        }
    }

    @Nested
    @DisplayName("getUser")
    class getUser{

        @Test
        @DisplayName("ok Test")
        void ok(){
            //given
            User user = makeUser(1L);
            String phone = user.getPhone();
            given(userRepository.findByPhoneAndPhoneActiveYn(phone, true)).willReturn(Optional.of(user));
            UserDto.ResponseUser expectedResponseUserDto = UserDto.ResponseUser.toDto(user);
            //when
            UserDto.ResponseUser actualResponseUserDto= userService.getUser(phone);
            //then
            assertThat(actualResponseUserDto).usingRecursiveComparison().isEqualTo(expectedResponseUserDto);
            verify(userRepository, times(1)).findByPhoneAndPhoneActiveYn(phone, true);
            verifyNoMoreInteractions(userRepository);
        }

        @Test
        @DisplayName("NotFoundException Test")
        void NotFoundException(){
            //given
            User user = makeUser(1L);
            String phone = user.getPhone();
            given(userRepository.findByPhoneAndPhoneActiveYn(phone, true)).willReturn(Optional.empty());
            //when
            NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> userService.getUser(phone));
            //then
            assertEquals(notFoundException.getMessage(), "회원을 찾을 수 없습니다.");
            verify(userRepository, times(1)).findByPhoneAndPhoneActiveYn(phone, true);
            verifyNoMoreInteractions(userRepository);
        }
    }
}
