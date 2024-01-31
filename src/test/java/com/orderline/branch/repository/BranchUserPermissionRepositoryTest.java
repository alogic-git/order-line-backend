package com.orderline.branch.repository;

import com.orderline.branch.enums.FunctionTypeEnum;
import com.orderline.branch.enums.PermissionTypeEnum;
import com.orderline.branch.model.entity.Branch;
import com.orderline.branch.model.entity.BranchUserPermission;
import com.orderline.branch.model.entity.BranchUserRole;
import com.orderline.common.user.model.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({"local", "dev"})
class BranchUserPermissionRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private BranchUserPermissionRepository branchUserPermissionRepository;

    public User makeUser(){
        return User.builder()
                .username("username")
                .phone("01012345678")
                .build();
    }

    public Branch makeBranch(){
        return Branch.builder()
                .build();
    }

    public BranchUserRole makeBranchUserRole(){
        return BranchUserRole.builder()
                .build();
    }
    public BranchUserRole makeBranchUserRole(User user, Branch branch){
        return BranchUserRole.builder()
                .user(user)
                .branch(branch)
                .build();
    }
    public BranchUserPermission makeBranchUserPermission(BranchUserRole branchUserRole){
        return BranchUserPermission.builder()
                .branchUserRole(branchUserRole)
                .build();
    }
    public BranchUserPermission makeBranchUserPermission2(BranchUserRole branchUserRole){
        return BranchUserPermission.builder()
                .branchUserRole(branchUserRole)
                .build();
    }

    private void persistObjects(Object... objects) {
        for (Object object : objects) {
            entityManager.persist(object);
        }
        entityManager.flush();
    }

    @Test
    @DisplayName("findByBranchUserRoleId")
    void findByBranchUserRoleId(){
        //given
        BranchUserRole branchUserRole = makeBranchUserRole();
        BranchUserPermission expectedBranchUserPermission = makeBranchUserPermission(branchUserRole);
        BranchUserPermission expectedBranchUserPermission2 = makeBranchUserPermission2(branchUserRole);
        persistObjects(branchUserRole, expectedBranchUserPermission, expectedBranchUserPermission2);
        Long branchUserRoleId = expectedBranchUserPermission.getBranchUserRole().getId();
        List<BranchUserPermission> branchUserPermissionList = Arrays.asList(expectedBranchUserPermission, expectedBranchUserPermission2);
        //when
        List<BranchUserPermission> actualBranchUserPermissionList = branchUserPermissionRepository.findByBranchUserRoleId(branchUserRoleId);
        //then
        assertEquals(branchUserPermissionList, actualBranchUserPermissionList);
    }

    @Test
    @DisplayName("findByBranchUserRole_UserIdAndBranchUserRole_BranchId")
    void findByBranchUserRole_UserIdAndBranchUserRole_BranchId(){
        //given
        User user = makeUser();
        Branch branch = makeBranch();
        BranchUserRole branchUserRole = makeBranchUserRole(user, branch);
        BranchUserPermission expectedBranchUserPermission = makeBranchUserPermission(branchUserRole);
        BranchUserPermission expectedBranchUserPermission2 = makeBranchUserPermission2(branchUserRole);
        persistObjects(user, branch, branchUserRole, expectedBranchUserPermission, expectedBranchUserPermission2);
        Long userId = expectedBranchUserPermission.getBranchUserRole().getUser().getId();
        Long branchId = expectedBranchUserPermission.getBranchUserRole().getBranch().getId();
        List<BranchUserPermission> branchUserPermissionList = Arrays.asList(expectedBranchUserPermission, expectedBranchUserPermission2);
        //when
        List<BranchUserPermission> actualBranchUserPermissionList = branchUserPermissionRepository.findByBranchUserRole_UserIdAndBranchUserRole_BranchId(userId, branchId);
        //then
        assertEquals(branchUserPermissionList, actualBranchUserPermissionList);
    }

    @Test
    @DisplayName("saveAll")
    void saveAll(){
        //given
        BranchUserRole branchUserRole = makeBranchUserRole();
        persistObjects(branchUserRole);
        List<BranchUserPermission> expectedBranchPermissionList = new ArrayList<>();
        for (FunctionTypeEnum authEnum : FunctionTypeEnum.values()) {
            expectedBranchPermissionList.add(BranchUserPermission.builder()
                    .branchUserRole(branchUserRole)
                    .functionType(authEnum)
                    .permissionType(PermissionTypeEnum.EDIT)
                    .build());
        }
        //when
        List<BranchUserPermission> actualBranchUserPermissionList = branchUserPermissionRepository.saveAll(expectedBranchPermissionList);
        //then
        assertEquals(expectedBranchPermissionList, actualBranchUserPermissionList);
    }
}
