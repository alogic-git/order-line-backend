package com.orderline.branch.repository;

import com.orderline.branch.model.dto.BranchDto;
import com.orderline.branch.model.entity.Branch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({"local", "dev"})
class BranchRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private BranchRepository branchRepository;

    public Branch makeBranch(){
        return Branch.builder()
                .build();
    }

    @Test
    @DisplayName("findById test")
    void findById(){
        //given
        Branch expectedBranch = makeBranch();
        entityManager.persist(expectedBranch);
        entityManager.flush();
        //when
        Optional<Branch> branchOptional = branchRepository.findById(expectedBranch.getId());
        //then
        assertTrue(branchOptional.isPresent());
        assertEquals(expectedBranch, branchOptional.get());
    }

    @Test
    @DisplayName("create test")
    void create(){
        //given
        Branch expectedBranch = makeBranch();
        entityManager.persist(expectedBranch);
        entityManager.flush();
        //when
        Branch actualBranch = branchRepository.save(expectedBranch);
        //then
        assertEquals(expectedBranch, actualBranch);
    }

    @Test
    @DisplayName("update test")
    void update(){
        //given
        Branch expectedBranch = makeBranch();
        entityManager.persist(expectedBranch);
        entityManager.flush();
        //when
        BranchDto.RequestBranchDto requestBranchDto = BranchDto.RequestBranchDto.builder()
                .branchName("test")
                .build();
        expectedBranch.updateBranch(requestBranchDto);
        branchRepository.save(expectedBranch);
        //then
        assertEquals(expectedBranch.getBranchName(), requestBranchDto.getBranchName());
    }

    @Test
    @DisplayName("archive test")
    void archive(){
        //given
        Branch expectedBranch = makeBranch();
        entityManager.persist(expectedBranch);
        entityManager.flush();
        //when
        expectedBranch.archiveBranch();
        branchRepository.save(expectedBranch);
        //then
        assertTrue(expectedBranch.getArchiveYn());
    }

    @Test
    @DisplayName("delete test")
    void delete(){
        //given
        Branch expectedBranch = makeBranch();
        entityManager.persist(expectedBranch);
        entityManager.flush();
        //when
        expectedBranch.deleteBranch();
        branchRepository.save(expectedBranch);
        //then
        assertFalse(branchRepository.existsById(expectedBranch.getId()));
    }

}
