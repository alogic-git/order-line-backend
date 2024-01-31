package com.ptglue.common.repository.user.repository;

import com.ptglue.common.user.model.entity.ActivationCode;
import com.ptglue.common.user.repository.ActivationCodeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({"local", "dev"})
class ActivationCodeRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ActivationCodeRepository activationCodeRepository;

    public ActivationCode makeActivationCode(){
        return ActivationCode.builder()
                .phone("01012345678")
                .activationCode("123456")
                .activationYn(false)
                .build();
    }
    @Test
    @DisplayName("findFirstByPhoneOrderByModDateTimeDesc test")
    void findFirstByPhoneOrderByModDateTimeDesc(){
        //given
        ActivationCode expectedActivationCode = makeActivationCode();
        entityManager.persist(expectedActivationCode);
        entityManager.flush();
        String phone = expectedActivationCode.getPhone();
        //when
        ActivationCode actualActivationCode = activationCodeRepository.findFirstByPhoneOrderByModDateTimeDesc(phone);
        //then
        assertThat(actualActivationCode).isEqualTo(expectedActivationCode);

    }

    @Test
    @DisplayName("findFirstByPhoneOrderByRegDateTimeDesc test")
    void findFirstByPhoneOrderByRegDateTimeDesc(){
        //given
        ActivationCode expectedActivationCode = makeActivationCode();
        entityManager.persist(expectedActivationCode);
        entityManager.flush();
        String phone = expectedActivationCode.getPhone();
        //when
        ActivationCode actualActivationCode = activationCodeRepository.findFirstByPhoneOrderByRegDateTimeDesc(phone);
        //then
        assertThat(actualActivationCode).isEqualTo(expectedActivationCode);

    }

    @Test
    @DisplayName("create test")
    void create(){
        //given
        ActivationCode expectedActivationCode = makeActivationCode();
        entityManager.persist(expectedActivationCode);
        entityManager.flush();
        //when
        ActivationCode actualActivationCode = activationCodeRepository.save(expectedActivationCode);
        //then
        assertThat(actualActivationCode).isEqualTo(expectedActivationCode);
    }

    @Test
    @DisplayName("delete test")
    void delete(){
        //given
        ActivationCode expectedActivationCode = makeActivationCode();
        entityManager.persist(expectedActivationCode);
        entityManager.flush();
        Long userId = expectedActivationCode.getId();
        //when
        expectedActivationCode.deleteActivationCode();
        activationCodeRepository.save(expectedActivationCode);
        //then
        assertFalse(activationCodeRepository.existsById(userId));
    }

    @Test
    @DisplayName("update test")
    void update(){
        //given
        ActivationCode expectedActivationCode = makeActivationCode();
        entityManager.persist(expectedActivationCode);
        entityManager.flush();
        //when
        expectedActivationCode.updateActivationStatus();
        activationCodeRepository.save(expectedActivationCode);
        //then
        Optional<ActivationCode> activationCodeOptional = activationCodeRepository.findById(expectedActivationCode.getId());
        ActivationCode actualActivationCode = null;
        if(activationCodeOptional.isPresent()){
            actualActivationCode = activationCodeOptional.get();
        }

        assertTrue(activationCodeOptional.isPresent());
        assertTrue(actualActivationCode.getActivationYn());
    }
}
