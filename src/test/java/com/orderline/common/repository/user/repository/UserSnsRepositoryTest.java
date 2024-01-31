package com.orderline.common.repository.user.repository;

import com.orderline.common.user.enums.SnsTypeEnum;
import com.orderline.common.user.model.entity.UserSns;
import com.orderline.common.user.model.entity.User;
import com.orderline.common.user.repository.UserSnsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserSnsRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserSnsRepository userSnsRepository;

    public User makeTutee(){
        return User.builder()
                .username("tuteeId")
                .password("password")
                .phone("01012345678")
                .phoneActiveYn(true)
                .name("testTutee")
                .build();
    }

    public UserSns makeSnsInfo(User user){
        return UserSns.builder()
                .user(user)
                .snsId("1234567")
                .snsType(SnsTypeEnum.KAKAO)
                .build();
    }

    @Test
    @DisplayName("findByUser_Id test")
    void findByUser_IdTest(){
        //given
        User user = makeTutee();
        UserSns expectedUserSns = makeSnsInfo(user);
        entityManager.persist(user);
        entityManager.persist(expectedUserSns);
        entityManager.flush();
        Long userId = user.getId();
        //when
        UserSns actualUserSns = userSnsRepository.findByUser_Id(userId);
        //then
        assertThat(actualUserSns).isEqualTo(expectedUserSns);
        assertThat(actualUserSns.getUser().getId()).isEqualTo(userId);
    }
    @Test
    @DisplayName("findBySnsTypeAndSnsId test")
    void findBySnsTypeAndSnsIdTest(){
        //given
        User user = makeTutee();
        UserSns expectedUserSns = makeSnsInfo(user);
        entityManager.persist(user);
        entityManager.persist(expectedUserSns);
        entityManager.flush();
        SnsTypeEnum snsType = expectedUserSns.getSnsType();
        String snsId = expectedUserSns.getSnsId();
        //when
        Optional<UserSns> optionalUserSns = userSnsRepository.findBySnsTypeAndSnsId(snsType, snsId);
        UserSns actualUserSns = makeSnsInfo(user);
        if(optionalUserSns.isPresent()) actualUserSns = optionalUserSns.get();

        //then
        assertThat(actualUserSns).isEqualTo(expectedUserSns);
        assertThat(actualUserSns.getSnsType()).isEqualTo(snsType);
        assertThat(actualUserSns.getSnsId()).isEqualTo(snsId);
    }
    @Test
    @DisplayName("create test")
    void createTest(){
        //given
        User user = makeTutee();
        UserSns expectedUserSns = makeSnsInfo(user);
        //when
        UserSns actualUserSns = userSnsRepository.save(expectedUserSns);
        //then
        assertThat(actualUserSns).isEqualTo(expectedUserSns);
    }

}
