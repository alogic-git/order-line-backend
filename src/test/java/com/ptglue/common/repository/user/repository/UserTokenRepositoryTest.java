package com.ptglue.common.repository.user.repository;

import com.ptglue.common.user.model.entity.User;
import com.ptglue.common.user.model.entity.UserToken;
import com.ptglue.common.user.repository.UserTokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserTokenRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserTokenRepository userTokenRepository;

    public User makeTutee(){
        return User.builder()
                .username("tuteeId")
                .password("password")
                .phone("01012345678")
                .phoneActiveYn(true)
                .name("testTutee")
                .build();
    }

    public UserToken makeUserRefreshToken(User user){
        return UserToken.builder()
                .user(user)
                .refreshToken("asdf")
                .build();
    }

    @Test
    @DisplayName("findFirstByTokenOrderByIdDesc test")
    void findFirstByTokenOrderByIdDescTest(){
        //given
        User user = makeTutee();
        UserToken expectedUserToken = makeUserRefreshToken(user);
        entityManager.persist(user);
        entityManager.persist(expectedUserToken);
        entityManager.flush();
        String token = expectedUserToken.getRefreshToken();
        //when
        UserToken actualUserToken = userTokenRepository.findFirstByRefreshTokenOrderByIdDesc(token);
        //then
        assertThat(actualUserToken).isEqualTo(expectedUserToken);
        assertThat(actualUserToken.getRefreshToken()).isEqualTo(token);
    }
    @Test
    @DisplayName("findByUser_Id test")
    void findByUser_IdTest(){
        //given
        User user = makeTutee();
        UserToken token1 = makeUserRefreshToken(user);
        UserToken token2 = makeUserRefreshToken(user);
        entityManager.persist(user);
        entityManager.persist(token1);
        entityManager.persist(token2);
        entityManager.flush();
        List<UserToken> expectedUserTokens = new ArrayList<>();
        expectedUserTokens.add(token1);
        expectedUserTokens.add(token2);
        Long userId = user.getId();
        //when
        List<UserToken> actualUserTokens = userTokenRepository.findByUser_Id(userId);
        //then
        assertThat(actualUserTokens).isEqualTo(expectedUserTokens);
        assertThat(actualUserTokens.get(0).getUser().getId()).isEqualTo(userId);
        assertThat(actualUserTokens.get(1).getUser().getId()).isEqualTo(userId);
    }
    @Test
    @DisplayName("create test")
    void createTest(){
        //given
        User user = makeTutee();
        UserToken expectedToken = makeUserRefreshToken(user);
        entityManager.persist(user);
        entityManager.persist(expectedToken);
        entityManager.flush();
        //when
        UserToken actualToken = userTokenRepository.save(expectedToken);
        //then
        assertThat(actualToken).isEqualTo(expectedToken);
    }
    @Test
    @DisplayName("delete test")
    void deleteTest(){
        //given
        User user = makeTutee();
        UserToken expectedToken = makeUserRefreshToken(user);
        entityManager.persist(user);
        entityManager.persist(expectedToken);
        entityManager.flush();
        //when
        expectedToken.deleteToken();
        userTokenRepository.save(expectedToken);
        //then
        assertFalse(userTokenRepository.existsById(expectedToken.getId()));
    }
}
