package com.ptglue.branch.repository;


import com.ptglue.basic.enums.order.TutorOrderEnum;
import com.ptglue.branch.enums.ConnectionTypeEnum;
import com.ptglue.branch.model.entity.Branch;
import com.ptglue.branch.model.entity.BranchUserRole;
import com.ptglue.common.user.enums.UserRoleEnum;
import com.ptglue.common.user.model.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({"local", "dev"})
class BranchUserRoleRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private BranchUserRoleRepository branchUserRoleRepository;

    public User makeUser(){
        return User.builder()
                .username("user1")
                .password("password")
                .phone("11111111111")
                .phoneActiveYn(true)
                .name("test user 1")
                .build();
    }

    public Branch makeBranch(){
        return Branch.builder()
                .branchName("test branch")
                .archiveYn(false)
                .build();
    }

    public BranchUserRole makeBranchManager(User user, Branch branch){
        return BranchUserRole.builder()
                .user(user)
                .branch(branch)
                .roleType(UserRoleEnum.MANAGER)
                .connectionType(ConnectionTypeEnum.CONNECTED)
                .lastViewYn(true)
                .archiveYn(false)
                .build();
    }

    public BranchUserRole makeBranchTutor(User user, Branch branch){
        return BranchUserRole.builder()
                .user(user)
                .branch(branch)
                .roleType(UserRoleEnum.TUTOR)
                .connectionType(ConnectionTypeEnum.CONNECTED)
                .lastViewYn(false)
                .archiveYn(false)
                .build();
    }

    private void persistObjects(Object... objects) {
        for (Object object : objects) {
            entityManager.persist(object);
        }
        entityManager.flush();
    }

    @Test
    @DisplayName("findByUserIdAndLastViewYnAndArchiveYn test")
    void findByUserIdAndLastViewYnAndArchiveYn(){
        //given
        User user = makeUser();
        Branch branch = makeBranch();
        BranchUserRole expectedBranchUserRole = makeBranchManager(user, branch);
        persistObjects(user, branch, expectedBranchUserRole);
        Long userId = expectedBranchUserRole.getUser().getId();
        //when
        Optional<BranchUserRole> actualBranchUserRoleOptional = branchUserRoleRepository.findByUserIdAndLastViewYnAndArchiveYn(userId, true, false);
        BranchUserRole actualBranchUserRole = null;
        if(actualBranchUserRoleOptional.isPresent()){
            actualBranchUserRole = actualBranchUserRoleOptional.get();
        }
        //then
        assertTrue(actualBranchUserRoleOptional.isPresent());
        assertEquals(expectedBranchUserRole, actualBranchUserRole);
    }

    @Test
    @DisplayName("findByUserId test")
    void findByUserId(){
        //given
        User user = makeUser();
        Branch branch = makeBranch();
        BranchUserRole expectedBranchManager = makeBranchManager(user, branch);
        BranchUserRole expectedBranchTutor = makeBranchTutor(user, branch);
        persistObjects(user, branch, expectedBranchManager, expectedBranchTutor);
        List<BranchUserRole> expectedBranchUserRoleList = Arrays.asList(expectedBranchManager, expectedBranchTutor);
        Long userId = expectedBranchManager.getUser().getId();
        //when
        List<BranchUserRole> actualBranchUserRoleList = branchUserRoleRepository.findByUserId(userId);
        //then
        assertEquals(expectedBranchUserRoleList, actualBranchUserRoleList);
    }

    @Test
    @DisplayName("findByUserIdAndArchiveYnAndBranch_DeleteYnAndBranch_ArchiveYn")
    void findByUserIdAndArchiveYnAndBranch_DeleteYnAndBranch_ArchiveYn(){
        //given
        User user = makeUser();
        Branch branch = makeBranch();
        BranchUserRole expectedBranchManager = makeBranchManager(user, branch);
        BranchUserRole expectedBranchTutor = makeBranchTutor(user, branch);
        List<BranchUserRole> expectedBranchUserRoleList = Arrays.asList(expectedBranchManager, expectedBranchTutor);
        persistObjects(user, branch, expectedBranchManager, expectedBranchTutor);
        Long userId = expectedBranchManager.getUser().getId();
        //when
        List<BranchUserRole> actualBranchUserRoleList = branchUserRoleRepository.findByUserIdAndArchiveYnAndBranch_DeleteYnAndBranch_ArchiveYn(userId, false, false, false);
        //then
        assertEquals(expectedBranchUserRoleList, actualBranchUserRoleList);
    }

    @Test
    @DisplayName("findByBranchIdAndUser_Phone test")
    void findByBranchIdAndUser_Phone(){
        //given
        User user = makeUser();
        Branch branch = makeBranch();
        BranchUserRole expectedBranchManager = makeBranchManager(user, branch);
        BranchUserRole expectedBranchTutor = makeBranchTutor(user, branch);
        persistObjects(user, branch, expectedBranchManager, expectedBranchTutor);
        List<BranchUserRole> expectedBranchUserRoleList = Arrays.asList(expectedBranchManager, expectedBranchTutor);
        Long branchId = expectedBranchManager.getBranch().getId();
        String phone = expectedBranchManager.getUser().getPhone();
        //when
        List<BranchUserRole> actualBranchUserRoleList = branchUserRoleRepository.findByBranchIdAndUser_Phone(branchId, phone);
        //then
        assertEquals(expectedBranchUserRoleList, actualBranchUserRoleList);
    }

    @Test
    @DisplayName("findByBranchIdAndUserIdAndRoleType test")
    void findByBranchIdAndUserIdAndRoleType(){
        //given
        User user = makeUser();
        Branch branch = makeBranch();
        BranchUserRole expectedBranchUserRole = makeBranchManager(user, branch);
        persistObjects(user, branch, expectedBranchUserRole);
        Long branchId = expectedBranchUserRole.getBranch().getId();
        Long userId = expectedBranchUserRole.getUser().getId();
        UserRoleEnum roleType = expectedBranchUserRole.getRoleType();
        //when
        Optional<BranchUserRole> actualBranchUserRoleOptional = branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(branchId, userId, roleType);
        BranchUserRole actualBranchUserRole = null;
        if(actualBranchUserRoleOptional.isPresent()){
            actualBranchUserRole = actualBranchUserRoleOptional.get();
        }
        //then
        assertTrue(actualBranchUserRoleOptional.isPresent());
        assertEquals(expectedBranchUserRole, actualBranchUserRole);
    }

    @Test
    @DisplayName("findByBranchId test")
    void findByBranchId(){
        //given
        User user = makeUser();
        Branch branch = makeBranch();
        BranchUserRole expectedBranchManager = makeBranchManager(user, branch);
        BranchUserRole expectedBranchTutor = makeBranchTutor(user, branch);
        persistObjects(user, branch, expectedBranchManager, expectedBranchTutor);
        List<BranchUserRole> expectedBranchUserRoleList = Arrays.asList(expectedBranchManager, expectedBranchTutor);
        Long branchId = expectedBranchManager.getBranch().getId();
        //when
        List<BranchUserRole> actualBranchUserRoleList = branchUserRoleRepository.findByBranchId(branchId);
        //then
        assertEquals(expectedBranchUserRoleList, actualBranchUserRoleList);
    }

    @Test
    @DisplayName("findByUserIdAndBranchId test")
    void findByUserIdAndBranchId(){
        //given
        User user = makeUser();
        Branch branch = makeBranch();
        BranchUserRole expectedBranchUserRole = makeBranchManager(user, branch);
        persistObjects(user, branch, expectedBranchUserRole);
        Long userId = expectedBranchUserRole.getUser().getId();
        Long branchId = expectedBranchUserRole.getBranch().getId();
        //when
        Optional<BranchUserRole> actualBranchUserRoleOptional = branchUserRoleRepository.findByUserIdAndBranchId(userId, branchId);
        BranchUserRole actualBranchUserRole = null;
        if(actualBranchUserRoleOptional.isPresent()){
            actualBranchUserRole = actualBranchUserRoleOptional.get();
        }
        //then
        assertTrue(actualBranchUserRoleOptional.isPresent());
        assertEquals(expectedBranchUserRole, actualBranchUserRole);
    }

    @Test
    @DisplayName("findTutorNativeQuery test")
    void findTutorNativeQuery(){
        //given
        User user = makeUser();
        Branch branch = makeBranch();
        BranchUserRole expectedBranchManger = makeBranchManager(user, branch);
        BranchUserRole expectedBranchTutor = makeBranchTutor(user, branch);
        persistObjects(user, branch, expectedBranchManger, expectedBranchTutor);
        Long branchId = expectedBranchManger.getBranch().getId();
        String searchWord = '%'+ expectedBranchManger.getUser().getName().substring(0, 1) + '%';
        Sort sort = Sort.by(Sort.Direction.ASC, TutorOrderEnum.TUTOR_NAME_ASC.getColumn());
        Pageable pageable = PageRequest.of(0, 10, sort);
        //when
        Page<BranchUserRole> actualBranchUserRolePage = branchUserRoleRepository.findTutorNativeQuery(branchId, searchWord, pageable);
        //then
        assertFalse(actualBranchUserRolePage.getContent().contains(expectedBranchManger));
        assertTrue(actualBranchUserRolePage.getContent().contains(expectedBranchTutor));
    }

    @Test
    @DisplayName("findBranchListNativeQuery test")
    void findBranchListNativeQuery(){
        //given
        User user = makeUser();
        Branch branch = makeBranch();
        BranchUserRole expectedBranchUserRole = makeBranchTutor(user, branch);
        persistObjects(user, branch, expectedBranchUserRole);
        Long userId = expectedBranchUserRole.getUser().getId();
        String userRole = expectedBranchUserRole.getRoleType().getId();
        Pageable pageable = PageRequest.of(0, 10);
        //when
        Page<BranchUserRole> actualBranchUserRolePage = branchUserRoleRepository.findBranchListNativeQuery(userId, pageable);
        //then
        assertTrue(actualBranchUserRolePage.getContent().contains(expectedBranchUserRole));
    }

    @Test
    @DisplayName("findByBranchIdAndRoleTypeAndArchiveYn test")
    void findByBranchIdAndRoleTypeAndArchiveYn(){
        //given
        User user = makeUser();
        Branch branch = makeBranch();
        BranchUserRole expectedBranchManager = makeBranchManager(user, branch);
        BranchUserRole expectedBranchManager2 = makeBranchManager(user, branch);
        BranchUserRole expectedBranchTutor = makeBranchTutor(user, branch);
        persistObjects(user, branch, expectedBranchManager, expectedBranchManager2, expectedBranchTutor);
        Pageable pageable = PageRequest.of(0, 10);
        Long branchId = expectedBranchManager.getBranch().getId();
        UserRoleEnum roleType = expectedBranchManager.getRoleType();
        //when
        Page<BranchUserRole> actualBranchUserRolePage = branchUserRoleRepository.findByBranchIdAndRoleTypeAndArchiveYn(branchId, roleType, false, pageable);
        //then
        assertTrue(actualBranchUserRolePage.getContent().contains(expectedBranchManager));
        assertTrue(actualBranchUserRolePage.getContent().contains(expectedBranchManager2));
        assertFalse(actualBranchUserRolePage.getContent().contains(expectedBranchTutor));
    }

    @Test
    @DisplayName("create test")
    void createTest(){
        //given
        User user = makeUser();
        Branch branch = makeBranch();
        BranchUserRole expectedBranchUserRole = makeBranchTutor(user, branch);
        persistObjects(user, branch);
        //when
        BranchUserRole actualBranchUserRole = branchUserRoleRepository.save(expectedBranchUserRole);
        //then
        assertThat(actualBranchUserRole).isEqualTo(expectedBranchUserRole);
    }

    @Test
    @DisplayName("update test")
    void updateTest(){
        //given
        User user = makeUser();
        Branch branch = makeBranch();
        BranchUserRole expectedBranchUserRole = makeBranchTutor(user, branch);
        persistObjects(user, branch, expectedBranchUserRole);
        //when
        expectedBranchUserRole.updateArchive(true);
        branchUserRoleRepository.save(expectedBranchUserRole);
        //then
        Optional<BranchUserRole> actualBranchUserRoleOptional = branchUserRoleRepository.findById(expectedBranchUserRole.getId());
        BranchUserRole actualBranchUserRole = null;
        if(actualBranchUserRoleOptional.isPresent()) actualBranchUserRole = actualBranchUserRoleOptional.get();

        assertTrue(actualBranchUserRoleOptional.isPresent());
        assertTrue(actualBranchUserRole.getArchiveYn());
    }

    @Test
    @DisplayName("saveAll test")
    void saveAll(){
        //given
        User user = makeUser();
        Branch branch = makeBranch();
        BranchUserRole expectedBranchManager = makeBranchManager(user, branch);
        BranchUserRole expectedBranchManager2 = makeBranchManager(user, branch);
        BranchUserRole expectedBranchTutor = makeBranchTutor(user, branch);
        persistObjects(user, branch);
        List<BranchUserRole> expectedBranchUserRoleList = Arrays.asList(expectedBranchManager, expectedBranchManager2, expectedBranchTutor);
        //when
        List<BranchUserRole> actualBranchUserRoleList = branchUserRoleRepository.saveAll(expectedBranchUserRoleList);
        //then
        assertEquals(expectedBranchUserRoleList, actualBranchUserRoleList);
    }

    @Test
    @DisplayName("findById test")
    void findById(){
        //given
        User user = makeUser();
        Branch branch = makeBranch();
        BranchUserRole expectedBranchManager = makeBranchManager(user, branch);
        BranchUserRole expectedBranchManager2 = makeBranchManager(user, branch);
        BranchUserRole expectedBranchTutor = makeBranchTutor(user, branch);
        persistObjects(user, branch, expectedBranchManager, expectedBranchManager2, expectedBranchTutor);
        Long branchUserRoleId = expectedBranchManager.getId();
        //when
        Optional<BranchUserRole> actualBranchUserRoleOptional = branchUserRoleRepository.findById(branchUserRoleId);
        //then
        assertTrue(actualBranchUserRoleOptional.isPresent());
        assertEquals(expectedBranchManager, actualBranchUserRoleOptional.get());
        assertNotEquals(expectedBranchManager2, actualBranchUserRoleOptional.get());
    }
}