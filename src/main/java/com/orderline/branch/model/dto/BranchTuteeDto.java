package com.orderline.branch.model.dto;

import com.orderline.branch.enums.ConnectionTypeEnum;
import com.orderline.branch.model.entity.*;
import io.swagger.annotations.ApiModelProperty;
import com.orderline.basic.model.dto.CommonDto;
import com.orderline.basic.utils.TimeFunction;
import com.orderline.branch.enums.FunctionTypeEnum;
import com.orderline.branch.enums.PermissionTypeEnum;
import com.orderline.common.user.enums.UserRoleEnum;
import com.orderline.common.user.model.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BranchTuteeDto {

    @Builder
    @Getter
    public static class ResponseBranchTuteeDto {

        @ApiModelProperty(value = "지점 id")
        private Long branchId;

        @ApiModelProperty(value = "지점 회원 id")
        private Long branchUserRoleId;

        @ApiModelProperty(value = "회원 db user id")
        private Long userId;

        @ApiModelProperty(value = "회원 앱 유저 아이디")
        private String username;

        @ApiModelProperty(value = "회원명")
        private String tuteeName;

        @ApiModelProperty(value = "역할")
        private UserRoleEnum roleType;

        @ApiModelProperty(value = "잔여횟수")
        private Integer remainReservationCount;

        @ApiModelProperty(value = "잔여기간")
        private Integer remainPeriod;

        @ApiModelProperty(value = "회원 폰번호")
        private String phone;

        @ApiModelProperty(value = "회원 생년월일")
        private LocalDate birthdate; //db datetime, localdatetime?zone?

        @ApiModelProperty(value = "회원 성별")
        private String gender;

        @ApiModelProperty(value = "회원 프로필 사진")
        private String profileUri;

        @ApiModelProperty(value = "연결 여부(CONNECTED/WAIT/DISCONNECTED)")
        private CommonDto.EnumStateWithAttr connectionType;

        @ApiModelProperty(value = "보관 여부 (false: 사용, true: 보관)")
        private Boolean archiveYn;

        public static BranchTuteeDto.ResponseBranchTuteeDto toDto(final BranchUserOngoingList branchUserOngoingList) {
            User user = branchUserOngoingList.getUser();
            return ResponseBranchTuteeDto.builder()
                    .branchId(branchUserOngoingList.getBranch().getId())
                    .userId(user.getId())
                    .branchUserRoleId(branchUserOngoingList.getBranchUserRoleId())
                    .username(user.getUsername())
                    .tuteeName(user.getName())
                    .roleType(branchUserOngoingList.getRoleType())
                    .remainReservationCount(branchUserOngoingList.getRemainReservationCount() > 999999 ? 999999 : branchUserOngoingList.getRemainReservationCount())
                    .remainPeriod(TimeFunction.getRemainPeriod(branchUserOngoingList.getEndDate()))
                    .phone(user.getPhone())
                    .birthdate(user.getBirthDate())
                    .gender(user.getGender())
                    .profileUri(user.getProfileUri())
                    .connectionType(
                            CommonDto.EnumStateWithAttr.builder()
                                    .stateCode(branchUserOngoingList.getConnectionType().getId())
                                    .stateName(branchUserOngoingList.getConnectionType().getText())
                                    .attr1(branchUserOngoingList.getConnectionType().getAttr1())
                                    .attr2(branchUserOngoingList.getConnectionType().getAttr2())
                                    .attr3(branchUserOngoingList.getConnectionType().getAttr3())
                                    .attr4(branchUserOngoingList.getConnectionType().getAttr4())
                                    .build()
                    )
                    .build();
        }

        public static BranchTuteeDto.ResponseBranchTuteeDto toDto(final BranchUserEndList branchUserEndList) {
            User user = branchUserEndList.getUser();
            return ResponseBranchTuteeDto.builder()
                    .branchId(branchUserEndList.getBranch().getId())
                    .userId(user.getId())
                    .branchUserRoleId(branchUserEndList.getBranchUserRoleId())
                    .username(user.getUsername())
                    .tuteeName(user.getName())
                    .roleType(branchUserEndList.getRoleType())
                    .remainReservationCount(branchUserEndList.getRemainReservationCount() > 999999 ? 999999 : branchUserEndList.getRemainReservationCount())
                    .remainPeriod(TimeFunction.getRemainPeriod(branchUserEndList.getEndDate()))
                    .phone(user.getPhone())
                    .birthdate(user.getBirthDate())
                    .gender(user.getGender())
                    .profileUri(user.getProfileUri())
                    .connectionType(
                            CommonDto.EnumStateWithAttr.builder()
                                    .stateCode(branchUserEndList.getConnectionType().getId())
                                    .stateName(branchUserEndList.getConnectionType().getText())
                                    .attr1(branchUserEndList.getConnectionType().getAttr1())
                                    .attr2(branchUserEndList.getConnectionType().getAttr2())
                                    .attr3(branchUserEndList.getConnectionType().getAttr3())
                                    .attr4(branchUserEndList.getConnectionType().getAttr4())
                                    .build()
                    )
                    .build();
        }

        public static BranchTuteeDto.ResponseBranchTuteeDto toDto(final BranchUserRole branchUserRole) {
            User user = branchUserRole.getUser();

            return BranchTuteeDto.ResponseBranchTuteeDto.builder()
                    .branchUserRoleId(branchUserRole.getId())
                    .userId(user.getId())
                    .username(user.getUsername())
                    .tuteeName(user.getName())
                    .roleType(branchUserRole.getRoleType())
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

      public static BranchTuteeDto.ResponseBranchTuteeDto toDto(final BranchUserOngoingListGroupedKlass branchUserOngoingListGroupedKlass) {
            User user = branchUserOngoingListGroupedKlass.getUser();
            return BranchTuteeDto.ResponseBranchTuteeDto.builder()
                    .branchId(branchUserOngoingListGroupedKlass.getBranch().getId())
                    .userId(user.getId())
                    .branchUserRoleId(branchUserOngoingListGroupedKlass.getBranchUserRole().getId())
                    .username(user.getUsername())
                    .tuteeName(user.getName())
                    .roleType(branchUserOngoingListGroupedKlass.getRoleType())
                    .remainReservationCount(branchUserOngoingListGroupedKlass.getRemainReservationCount() > 999999 ? 999999 : branchUserOngoingListGroupedKlass.getRemainReservationCount())
                    .remainPeriod(TimeFunction.getRemainPeriod(branchUserOngoingListGroupedKlass.getEndDate()))
                    .phone(user.getPhone())
                    .birthdate(user.getBirthDate())
                    .profileUri(user.getProfileUri())
                    .connectionType(
                          CommonDto.EnumStateWithAttr.builder()
                              .stateCode(branchUserOngoingListGroupedKlass.getConnectionType().getId())
                              .stateName(branchUserOngoingListGroupedKlass.getConnectionType().getText())
                              .attr1(branchUserOngoingListGroupedKlass.getConnectionType().getAttr1())
                              .attr2(branchUserOngoingListGroupedKlass.getConnectionType().getAttr2())
                              .attr3(branchUserOngoingListGroupedKlass.getConnectionType().getAttr3())
                              .attr4(branchUserOngoingListGroupedKlass.getConnectionType().getAttr4())
                              .build()
                    ).build();
        }

        public static BranchTuteeDto.ResponseBranchTuteeDto toDto(final BranchUserOngoingListGroupedProduct branchUserOngoingListGroupedProduct){
            User user = branchUserOngoingListGroupedProduct.getUser();
            return ResponseBranchTuteeDto.builder()
                    .branchId(branchUserOngoingListGroupedProduct.getBranch().getId())
                    .userId(user.getId())
                    .branchUserRoleId(branchUserOngoingListGroupedProduct.getBranchUserRole().getId())
                    .username(user.getUsername())
                    .tuteeName(user.getName())
                    .roleType(branchUserOngoingListGroupedProduct.getRoleType())
                    .remainReservationCount(branchUserOngoingListGroupedProduct.getRemainReservationCount() > 999999 ? 999999 : branchUserOngoingListGroupedProduct.getRemainReservationCount())
                    .remainPeriod(TimeFunction.getRemainPeriod(branchUserOngoingListGroupedProduct.getEndDate()))
                    .phone(user.getPhone())
                    .birthdate(user.getBirthDate())
                    .profileUri(user.getProfileUri())
                    .connectionType(
                        CommonDto.EnumStateWithAttr.builder()
                            .stateCode(branchUserOngoingListGroupedProduct.getConnectionType().getId())
                            .stateName(branchUserOngoingListGroupedProduct.getConnectionType().getText())
                            .attr1(branchUserOngoingListGroupedProduct.getConnectionType().getAttr1())
                            .attr2(branchUserOngoingListGroupedProduct.getConnectionType().getAttr2())
                            .attr3(branchUserOngoingListGroupedProduct.getConnectionType().getAttr3())
                            .attr4(branchUserOngoingListGroupedProduct.getConnectionType().getAttr4())
                            .build()
                    ).build();
        }

    }

    @Builder
    @Getter
    public static class ResponseBranchTuteeListDto {

        @ApiModelProperty(value = "지점 회원 리스트")
        private List<BranchTuteeDto.ResponseBranchTuteeDto> results;
        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static BranchTuteeDto.ResponseBranchTuteeListDto build(Page<BranchTuteeDto.ResponseBranchTuteeDto> responseDtoPage, Integer currentPage, Integer maxResults) {
            return BranchTuteeDto.ResponseBranchTuteeListDto.builder()
                    .results(responseDtoPage.getContent())
                    .maxResults(maxResults)
                    .currentPage(currentPage)
                    .totalPages(responseDtoPage.getTotalPages())
                    .totalElements(responseDtoPage.getTotalElements())
                    .build();
        }
    }

    @Builder
    @Getter
    public static class RequestBranchTuteeDto {

        @NotNull(message = "등록할 강사/관리자의 유저아이디는 필수 입력값입니다.")
        @ApiModelProperty(value = "등록할 강사/관리자의 유저아이디", example = "1")
        private Long userId;

        @NotNull(message = "등록할 수강권 선택해주세요")
        @ApiModelProperty(value = "수강권 id", example = "1")
        private Long productId;
        public BranchUserRole toEntity(final Branch branch, final User user){
            return BranchUserRole.builder()
                    .branch(branch)
                    .user(user)
                    .roleType(UserRoleEnum.TUTEE)
                    .connectionType(ConnectionTypeEnum.WAIT)
                    .build();
        }

        public List<BranchUserPermission> toEntity(final BranchUserRole branchUserRole){
            List<BranchUserPermission> branchUserPermissionList = new ArrayList<>();
            for (FunctionTypeEnum functionType : FunctionTypeEnum.values()) {
                if (functionType == FunctionTypeEnum.SCHEDULE || functionType == FunctionTypeEnum.TICKET) { //일정 추가, 티켓 일시정지,
                    branchUserPermissionList.add(BranchUserPermission.builder()
                            .branch(branchUserRole.getBranch())
                            .user(branchUserRole.getUser())
                            .branchUserRole(branchUserRole)
                            .functionType(functionType)
                            .permissionType(PermissionTypeEnum.EDIT)
                            .build());
                } else {
                    branchUserPermissionList.add(BranchUserPermission.builder()
                            .branch(branchUserRole.getBranch())
                            .user(branchUserRole.getUser())
                            .branchUserRole(branchUserRole)
                            .functionType(functionType)
                            .permissionType(PermissionTypeEnum.VIEW)
                            .build());
                }
            }
            return branchUserPermissionList;
        }
    }

}
