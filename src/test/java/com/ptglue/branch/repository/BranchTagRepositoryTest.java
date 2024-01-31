package com.ptglue.branch.repository;

import com.ptglue.branch.model.entity.Branch;
import com.ptglue.branch.model.entity.BranchTag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({"local", "dev"})
class BranchTagRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private BranchTagRepository branchTagRepository;

    public BranchTag makeBranchTag(){
        return BranchTag.builder()
                .build();
    }

    public BranchTag makeBranchTag1(){
        return BranchTag.builder()
                .branch(Branch.builder().id(1L).build())
                .tag("tag1")
                .build();
    }

    public BranchTag makeBranchTag2(){
        return BranchTag.builder()
                .branch(Branch.builder().id(1L).build())
                .tag("tag2")
                .build();
    }

    @Test
    @DisplayName("create test")
    void create(){
        //given
        BranchTag expectedBranchTag = makeBranchTag();
        entityManager.persist(expectedBranchTag);
        entityManager.flush();
        //when
        BranchTag actualBranchTag = branchTagRepository.save(expectedBranchTag);
        //then
        assertEquals(expectedBranchTag, actualBranchTag);
    }

    @Test
    @DisplayName("findByBranchIdAndTagIn test")
    void findByBranchIdAndTagIn(){
        //given
        BranchTag expectedBranchTag1 = makeBranchTag1();
        BranchTag expectedBranchTag2 = makeBranchTag2();
        entityManager.persist(expectedBranchTag1);
        entityManager.persist(expectedBranchTag2);
        entityManager.flush();
        List<BranchTag> expectedBranchTagList = Arrays.asList(expectedBranchTag1, expectedBranchTag2);
        Long branchId = expectedBranchTag1.getBranch().getId();
        List<String> tagList = Arrays.asList(expectedBranchTag1.getTag(), expectedBranchTag2.getTag());
        //when
        List<BranchTag> actualBranchTagList = branchTagRepository.findByBranchIdAndTagIn(branchId, tagList);
        //then
        assertEquals(expectedBranchTagList, actualBranchTagList);
    }

    @Test
    @DisplayName("findByIdIn test")
    void findByIdIn(){
        //given
        BranchTag expectedBranchTag1 = makeBranchTag1();
        BranchTag expectedBranchTag2 = makeBranchTag2();
        entityManager.persist(expectedBranchTag1);
        entityManager.persist(expectedBranchTag2);
        entityManager.flush();
        List<BranchTag> expectedBranchTagList = Arrays.asList(expectedBranchTag1, expectedBranchTag2);
        List<Long> branchTagIdList = Arrays.asList(expectedBranchTag1.getId(), expectedBranchTag2.getId());
        //when
        List<BranchTag> actualBranchTagList = branchTagRepository.findByIdIn(branchTagIdList);
        //then
        assertEquals(expectedBranchTagList, actualBranchTagList);
    }
}
