package com.ptglue.common.repository.user.repository;

import com.ptglue.common.user.model.dto.UserDto;
import com.ptglue.common.user.model.entity.User;
import com.ptglue.common.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({"local", "dev"})
class UserRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    public User makeTutee(){
        return User.builder()
                .username("tuteeId")
                .password("password")
                .phone("01234567890")
                .phoneActiveYn(true)
                .name("testTutee")
                .build();
    }

    public User makeTempTutee(){
        return User.builder()
                .username("임시123")
                .password("password")
                .phone("01234567890")
                .phoneActiveYn(false)
                .name("임시123")
                .build();
    }

    public User makeUser(String username, String name){
        return User.builder()
                .username(username)
                .password("password")
                .name(name)
                .phone("01234567890")
                .phoneActiveYn(true)
                .build();
    }

    @Test
    @DisplayName("findByUsername test")
    void findByUsernameTest(){
        //given
        User expectedUser = makeTutee();
        entityManager.persist(expectedUser);
        entityManager.flush();
        String username = expectedUser.getUsername();
        //when
        Optional<User> userOptional = userRepository.findByUsername(username);
        //then
        assertTrue(userOptional.isPresent());
    }

    @Test
    @DisplayName("countByName test")
    void countByNameTest(){
        //given
        User user1 = makeUser("id1", "name1");
        User user2 = makeUser("id2", "name2");
        User user3 = makeUser("id3", "name1");
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.flush();
        String testName1 = user1.getName();
        String testName2 = user2.getName();
        //when
        Integer actualCount1 = userRepository.countByName(testName1);
        Integer actualCount2 = userRepository.countByName(testName2);
        //
        assertThat(actualCount1).isEqualTo(2);
        assertThat(actualCount2).isEqualTo(1);
    }

    @Test
    @DisplayName("findByPhone test")
    void findFirstByPhoneOrderByModDateTimeDesc(){
        //given
        User expectedUser = makeTutee();
        entityManager.persist(expectedUser);
        entityManager.flush();
        String phone = expectedUser.getPhone();
        //when
        User actualUser = userRepository.findFirstByPhoneOrderByModDateTimeDesc(phone);
        //then
        assertThat(actualUser).isEqualTo(expectedUser);
        assertThat(actualUser.getPhone()).isEqualTo(phone);
    }

    @Test
    @DisplayName("findByUsernameAndPhone test")
    void findByUsernameAndPhone(){
        //given
        User expectedUser = makeTutee();
        entityManager.persist(expectedUser);
        entityManager.flush();
        String username = expectedUser.getUsername();
        String phone = expectedUser.getPhone();
        //when
        User actualUser = userRepository.findByUsernameAndPhone(username, phone);
        //then
        assertThat(actualUser).isEqualTo(expectedUser);
        assertThat(actualUser.getUsername()).isEqualTo(username);
        assertThat(actualUser.getPhone()).isEqualTo(phone);
    }

    @Test
    @DisplayName("findAllByPhone test")
    void findAllByPhone(){
        //given
        User expectedUser = makeTutee();
        User expectedUser2 = makeTempTutee();
        entityManager.persist(expectedUser);
        entityManager.persist(expectedUser2);
        entityManager.flush();
        String phone = expectedUser.getPhone();
        //when
        List<User> actualUserList = userRepository.findAllByPhone(phone);
        //then
        assertTrue(actualUserList.contains(expectedUser));
        assertTrue(actualUserList.contains(expectedUser2));
    }

    @Test
    @DisplayName("findByPhoneAndPhoneActiveYn test")
    void findByPhoneAndPhoneActiveYn(){
        //given
        User expectedUser = makeTutee();
        User expectedUser2 = makeTempTutee();
        entityManager.persist(expectedUser);
        entityManager.persist(expectedUser2);
        entityManager.flush();
        String phone = expectedUser.getPhone();
        //when
        Optional<User> actualUserOptional = userRepository.findByPhoneAndPhoneActiveYn(phone, true);
        User actualUser = null;
        if(actualUserOptional.isPresent()) actualUser = actualUserOptional.get();
        //then
        assertTrue(actualUserOptional.isPresent());
        assertThat(actualUser).isEqualTo(expectedUser);
    }

    @Test
    @DisplayName("findById test")
    void findByIdTest(){
        //given
        User expectedUser= makeUser("id1", "name1");
        entityManager.persist(expectedUser);
        entityManager.flush();
        Long userId = expectedUser.getId();
        //when
        Optional<User> userOptional = userRepository.findById(userId);
        User actualUser = null;
        if(userOptional.isPresent()) actualUser = userOptional.get();
        //then
        assertThat(actualUser).isEqualTo(expectedUser);
    }

    @Test
    @DisplayName("create test")
    void createTest(){
        //given
        User expectedUser= makeUser("id1", "name1");
        entityManager.persist(expectedUser);
        entityManager.flush();
        //when
        User actualUser = userRepository.save(expectedUser);
        //then
        assertThat(actualUser).isEqualTo(expectedUser);
    }

    @Test
    @DisplayName("delete test")
    void deleteTest(){
        //given
        User expectedUser= makeUser("id1", "name1");
        entityManager.persist(expectedUser);
        entityManager.flush();
        //when
        expectedUser.deleteUser();
        userRepository.save(expectedUser);
        //then
        assertFalse(userRepository.existsById(expectedUser.getId()));
    }
}
