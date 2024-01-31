package com.orderline.branch.model.dto;

import com.orderline.basic.model.dto.CommonDto;
import com.orderline.branch.enums.ConnectionTypeEnum;
import com.orderline.branch.enums.FunctionTypeEnum;
import com.orderline.branch.enums.PermissionTypeEnum;
import com.orderline.branch.model.entity.Branch;
import com.orderline.branch.model.entity.BranchUserPermission;
import com.orderline.branch.model.entity.BranchUserRole;
import com.orderline.common.user.enums.UserRoleEnum;
import com.orderline.common.user.model.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BranchUserDto {

    @Builder
    @Getter
    public static class ResponseBranchUserDto {

        @ApiModelProperty(value = "지점강사 id")
        private Long branchUserRoleId;

        @ApiModelProperty(value = "강사 db user id")
        private Long userId;

        @ApiModelProperty(value = "강사 앱 유저 아이디")
        private String username;

        @ApiModelProperty(value = "강사명")
        private String tutorName;

        @ApiModelProperty(value = "역할")
        private UserRoleEnum roleType;

        @ApiModelProperty(value = "지점에서 지정한 강사 별칭")
        private String nickname;

        @ApiModelProperty(value = "강사 폰번호")
        private String phone;

        @ApiModelProperty(value = "강사 생년월일")
        private LocalDate birthdate; //db datetime, localdatetime?zone?

        @ApiModelProperty(value = "강사 성별")
        private String gender;

        @ApiModelProperty(value = "강사 프로필 사진")
        private String profileUri;

        @ApiModelProperty(value = "연결 여부(CONNECTED/WAIT/DISCONNECTED)")
        private CommonDto.EnumStateWithAttr connectionType;

        @ApiModelProperty(value = "보관 여부 (false: 사용, true: 보관)")
        private Boolean archiveYn;


        public static ResponseBranchUserDto toDto(final BranchUserRole branchUserRole) {
            User user = branchUserRole.getUser();
            return ResponseBranchUserDto.builder()
                    .branchUserRoleId(branchUserRole.getId())
                    .userId(user.getId())
                    .username(user.getUsername())
                    .tutorName(user.getName())
                    .roleType(branchUserRole.getRoleType())
                    .nickname(branchUserRole.getNickname())
                    .phone(user.getPhone())
                    .birthdate(user.getBirthDate())
                    .gender(user.getGender())
                    .profileUri(user.getProfileUri())
                    .connectionType(
                        CommonDto.EnumStateWithAttr.builder()
                            .stateCode(branchUserRole.getConnectionType().getId())
                            .stateName(branchUserRole.getConnectionType().getText())
                            .attr1(branchUserRole.getConnectionType().getAttr1())
                            .attr2(branchUserRole.getConnectionType().getAttr2())
                            .attr3(branchUserRole.getConnectionType().getAttr3())
                            .attr4(branchUserRole.getConnectionType().getAttr4())
                            .build()
                    )
                    .archiveYn(branchUserRole.getArchiveYn())
                    .build();
        }


    }

    @Builder
    @Getter
    public static class ResponseBranchUserListDto {

        @ApiModelProperty(value = "지점 강사 리스트")
        private List<BranchUserDto.ResponseBranchUserDto> results;
        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static BranchUserDto.ResponseBranchUserListDto build(Page<BranchUserDto.ResponseBranchUserDto> responseDtoPage, Integer currentPage, Integer maxResults){
            return BranchUserDto.ResponseBranchUserListDto.builder()
                    .results(responseDtoPage.getContent())
                    .maxResults(maxResults)
                    .currentPage(currentPage)
                    .totalPages(responseDtoPage.getTotalPages())
                    .totalElements(responseDtoPage.getTotalElements())
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestUpdateNickname{

        @ApiModelProperty(value = "지점에서 지정한 강사 별칭", example = "귀여운 고배배")
        private String nickname;
    }

    @Builder
    @Getter
    public static class RequestBranchUserDto {

        @NotNull(message = "등록할 강사/관리자의 유저아이디는 필수 입력값입니다.")
        @ApiModelProperty(value = "등록할 강사/관리자의 유저아이디", example = "1")
        private Long userId;

        @ApiModelProperty(value = "지점에서 지정한 강사/관리자 별칭", example = "귀여운 고배배")
        private String nickname;

        @NotNull(message = "등록한 역할을 입력해주세요. TUTOR: 강사, MANAGER: 관리자")
        @ApiModelProperty(value = "TUTOR: 강사, MANAGER: 관리자", example = "TUTOR")
        private UserRoleEnum roleType;

        @NotNull(message = "권한 설정은 필수 입니다.")
        @ApiModelProperty(value = "권한 설정 리스트")
        private List<RequestBranchUserPermissionDto> permissionDtoList;

        public BranchUserRole toEntity(final Branch branch, final User user){
            return BranchUserRole.builder()
                    .branch(branch)
                    .user(user)
                    .roleType(roleType)
                    .nickname(this.nickname)
                    .connectionType(ConnectionTypeEnum.WAIT)
                    .build();
        }

        public List<BranchUserPermission> toEntity(final BranchUserRole branchUserRole){
            List<BranchUserPermission> branchUserPermissionList = new ArrayList<>();
            for (RequestBranchUserPermissionDto permissionDto : this.permissionDtoList) {
                branchUserPermissionList.add(
                    BranchUserPermission.builder()
                        .branch(branchUserRole.getBranch())
                        .user(branchUserRole.getUser())
                        .branchUserRole(branchUserRole)
                        .functionType(permissionDto.functionType)
                        .permissionType(permissionDto.permissionType)
                        .build());
            }
            return branchUserPermissionList;
        }
    }

    @Builder
    @Getter
    public static class RequestBranchUserPermissionDto {

        @ApiModelProperty(value = "권한 종류", example = "KLASS")
        private FunctionTypeEnum functionType;

        @ApiModelProperty(value = "허용할 권한", example = "EDIT")
        private PermissionTypeEnum permissionType;

        public BranchUserPermission toEntity(final BranchUserRole branchUserRole){
            return BranchUserPermission.builder()
                    .branch(branchUserRole.getBranch())
                    .user(branchUserRole.getUser())
                    .branchUserRole(branchUserRole)
                    .functionType(this.functionType)
                    .permissionType(this.permissionType)
                    .build();
        }
    }

    @Builder
    @Getter
    public static class ResponseBranchUserPermissionDto {

        @ApiModelProperty(value = "권한 종류", example = "KLASS")
        private FunctionTypeEnum functionType;

        @ApiModelProperty(value = "허용할 권한", example = "EDIT")
        private PermissionTypeEnum permissionType;

        public static ResponseBranchUserPermissionDto toDto(final BranchUserPermission branchUserPermission){
            return ResponseBranchUserPermissionDto.builder()
                    .functionType(branchUserPermission.getFunctionType())
                    .permissionType(branchUserPermission.getPermissionType())
                    .build();
        }

    }

    @Getter
    @Builder
    public static class ResponseBranchTutorDto {

        @ApiModelProperty(value = "지점강사 id")
        private Long branchUserRoleId;

        @ApiModelProperty(value = "지점에서 지정한 강사 별칭")
        private String nickname;

        @ApiModelProperty(value = "강사 프로필 사진")
        private String profileUri;

        @ApiModelProperty(value = "보관 여부 (false: 사용, true: 보관)")
        private Boolean archiveYn;

        public static ResponseBranchTutorDto toDto(final BranchUserRole branchUserRole) {
            return ResponseBranchTutorDto.builder()
                    .branchUserRoleId(branchUserRole.getId())
                    .nickname(branchUserRole.getNickname())
                    .profileUri(branchUserRole.getUser().getProfileUri())
                    .archiveYn(branchUserRole.getArchiveYn())
                    .build();
        }
    }

    @Builder
    @Getter
    public static class ResponseBranchTutorListDto {

        @ApiModelProperty(value = "지점 강사 리스트")
        private List<BranchUserDto.ResponseBranchTutorDto> results;
        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static BranchUserDto.ResponseBranchTutorListDto build(Page<BranchUserDto.ResponseBranchTutorDto> responseDtoPage, Integer currentPage, Integer maxResults){
            return BranchUserDto.ResponseBranchTutorListDto.builder()
                    .results(responseDtoPage.getContent())
                    .maxResults(maxResults)
                    .currentPage(currentPage)
                    .totalPages(responseDtoPage.getTotalPages())
                    .totalElements(responseDtoPage.getTotalElements())
                    .build();
        }
    }
}
