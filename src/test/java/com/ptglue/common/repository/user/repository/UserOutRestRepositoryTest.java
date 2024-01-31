package com.ptglue.common.repository.user.repository;

import com.ptglue.common.user.model.entity.UserOutRest;
import com.ptglue.common.user.repository.UserOutRestRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({"local", "dev"})
class UserOutRestRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserOutRestRepository userOutRestRepository;

    public UserOutRest makeUserOutRest(){
        return UserOutRest.builder()
                .username("tuteeId")
                .phone("01234567890")
                .build();
    }

    @Test
    @DisplayName("findByUserId test")
    void findByUserId(){
        //given
        UserOutRest expectedUserOutRest = makeUserOutRest();
        entityManager.persist(expectedUserOutRest);
        entityManager.flush();
        Long userId = expectedUserOutRest.getUserId();
        //when
        Optional<UserOutRest> actualUserOutRestOptional = userOutRestRepository.findByUserId(userId);
        UserOutRest actualUserOutRest = null;
        if(actualUserOutRestOptional.isPresent()){
            actualUserOutRest = actualUserOutRestOptional.get();
        }
        //then
        assertTrue(actualUserOutRestOptional.isPresent());
        assertThat(actualUserOutRest).isEqualTo(expectedUserOutRest);
        assertThat(actualUserOutRest.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("findFirstByPhoneOrderByModDateTimeDesc test")
    void findFirstByPhoneOrderByModDateTimeDesc(){
        //given
        UserOutRest expectedUserOutRest = makeUserOutRest();
        entityManager.persist(expectedUserOutRest);
        entityManager.flush();
        String phone = expectedUserOutRest.getPhone();
        //when
        UserOutRest actualUserOutRest = userOutRestRepository.findFirstByPhoneOrderByModDateTimeDesc(phone);
        //then
        assertThat(actualUserOutRest).isEqualTo(expectedUserOutRest);
        assertThat(actualUserOutRest.getPhone()).isEqualTo(phone);
    }

    @Test
    @DisplayName("findByUsernameAndPhone test")
    void findByUsernameAndPhone(){
        //given
        UserOutRest expectedUserOutRest = makeUserOutRest();
        entityManager.persist(expectedUserOutRest);
        entityManager.flush();
        String username = expectedUserOutRest.getUsername();
        String phone = expectedUserOutRest.getPhone();
        //when
        UserOutRest actualUserOutRest = userOutRestRepository.findByUsernameAndPhone(username, phone);
        //then
        assertThat(actualUserOutRest).isEqualTo(expectedUserOutRest);
        assertThat(actualUserOutRest.getUsername()).isEqualTo(username);
        assertThat(actualUserOutRest.getPhone()).isEqualTo(phone);
    }

    @Test
    @DisplayName("findAllByPhone test")
    void findAllByPhone(){
        //given
        UserOutRest expectedUserOutRest = makeUserOutRest();
        entityManager.persist(expectedUserOutRest);
        entityManager.flush();
        String phone = expectedUserOutRest.getPhone();
        //when
        List<UserOutRest> actualUserOutRestList = userOutRestRepository.findAllByPhone(phone);
        //then
        assertTrue(actualUserOutRestList.contains(expectedUserOutRest));
        assertThat(actualUserOutRestList.get(0).getPhone()).isEqualTo(phone);
    }

}
