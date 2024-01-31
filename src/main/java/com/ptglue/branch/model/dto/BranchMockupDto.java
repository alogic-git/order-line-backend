package com.ptglue.branch.model.dto;

import com.ptglue.branch.enums.NoticeMethodType;
import com.ptglue.branch.enums.ScheduleAutoFinishTypeEnum;
import com.ptglue.basic.model.dto.CommonDto;
import com.ptglue.branch.enums.ConnectionTypeEnum;
import com.ptglue.branch.enums.FunctionTypeEnum;
import com.ptglue.branch.enums.PermissionTypeEnum;
import com.ptglue.branch.enums.SubjectTypeEnum;
import com.ptglue.common.user.enums.UserRoleEnum;

import java.util.Arrays;

public class BranchMockupDto {

    public static BranchDto.ResponseBranchSettingDto getBranchSettingMockup(){
        return BranchDto.ResponseBranchSettingDto.builder()
                .sunOperatingHours(Arrays.asList("09:00~12:00", "13:00~18:00"))
                .monOperatingHours(Arrays.asList("09:00~11:00", "12:00~17:00"))
                .tueOperatingHours(Arrays.asList("09:00~12:00", "13:00~18:00"))
                .wedOperatingHours(Arrays.asList("09:00~12:00", "13:00~18:00"))
                .thrOperatingHours(Arrays.asList("09:00~11:00", "12:00~17:00"))
                .friOperatingHours(Arrays.asList("09:00~11:00", "12:00~17:00"))
                .satOperatingHours(Arrays.asList("09:00~12:00", "13:00~18:00"))
                .startWeekType("SUN")
                .selectTime(30)
                .scheduleAutoFinishType(ScheduleAutoFinishTypeEnum.OFF)
                .ticketAutoFinishType(false)
                .signUpOnAttendanceYn(false)
                .reservationEnableYn(true)
                .reservationAvailableDate(14)
                .reservationNumDisplayYn(true)
                .capacityNumDisplayYn(true)
                .reservationEnableTimes("09:00~24:00")
                .alarmChangeScheduleToTuteeYn(true)
                .alarmChangeScheduleToTutorYn(true)
                .alarmChangeScheduleToManagerYn(true)
                .alarmNoticeScheduleStartTime(30)
                .alarmNoticeScheduleStartTimeMethodType(NoticeMethodType.PUSH)
                .alarmNoticeTicketEndDate(30)
                .alarmNoticeTicketEndDateMethodType(NoticeMethodType.PUSH)
                .build();
    }


    public static BranchDto.ResponseBranchListDto getBranchListMockup(){
        return BranchDto.ResponseBranchListDto.builder()
                .results(Arrays.asList(getBranchMockup(), getBranchMockup2()))
                .maxResults(10)
                .currentPage(1)
                .totalPages(1)
                .totalElements(1L)
                .build();
    }

    public static BranchDto.ResponseBranchDto getBranchMockup(){
        return BranchDto.ResponseBranchDto.builder()
                .branchId(1L)
                .branchName("필라테스A")
                .imageUri("")
                .subjectType(SubjectTypeEnum.SPORTS)
                .subjectDetail("요가")
                .branchArchiveYn(false)
                .tutorCount(5)
                .tuteeCount(30)
                .build();
    }
    public static BranchDto.ResponseBranchDto getBranchMockup2(){
        return BranchDto.ResponseBranchDto.builder()
                .branchId(1L)
                .branchName("스윙와일")
                .imageUri("")
                .subjectType(SubjectTypeEnum.SPORTS)
                .subjectDetail("골프")
                .branchArchiveYn(false)
                .tutorArchiveYn(false)
                .tutorCount(3)
                .tuteeCount(15)
                .build();
    }

    public static BranchUserDto.ResponseBranchUserDto getBranchUserMockup1(){
        return BranchUserDto.ResponseBranchUserDto.builder()
                .branchUserRoleId(1L)
                .userId(1L)
                .username("gobaebae")
                .tutorName("고배배")
                .nickname("귀여운 고배배")
                .profileUri("")
                .connectionType(
                        CommonDto.EnumStateWithAttr.builder()
                                .stateCode(ConnectionTypeEnum.CONNECTED.getId())
                                .stateName(ConnectionTypeEnum.CONNECTED.getText())
                                .attr1(ConnectionTypeEnum.CONNECTED.getAttr1())
                                .attr2(ConnectionTypeEnum.CONNECTED.getAttr2())
                                .build()
                )
                .archiveYn(false)
                .build();
    }
    public static BranchUserDto.ResponseBranchUserDto getBranchUserMockup2(){
        return BranchUserDto.ResponseBranchUserDto.builder()
                .branchUserRoleId(2L)
                .userId(2L)
                .username("nodaji")
                .tutorName("노다지")
                .nickname("다지")
                .profileUri("")
                .connectionType(
                        CommonDto.EnumStateWithAttr.builder()
                                .stateCode(ConnectionTypeEnum.WAIT.getId())
                                .stateName(ConnectionTypeEnum.WAIT.getText())
                                .attr1(ConnectionTypeEnum.WAIT.getAttr1())
                                .attr2(ConnectionTypeEnum.WAIT.getAttr2())
                                .build()

                )
                .archiveYn(false)
                .build();
    }

    public static BranchUserDto.ResponseBranchUserListDto getBranchUserListMockup(){
        return BranchUserDto.ResponseBranchUserListDto.builder()
                .results(Arrays.asList(getBranchUserMockup1(), getBranchUserMockup2()))
                .maxResults(10)
                .currentPage(1)
                .totalPages(1)
                .totalElements(2L)
                .build();
    }

    public static BranchUserDto.ResponseBranchUserPermissionDto getBranchUserPermissionMockup1(){
        return BranchUserDto.ResponseBranchUserPermissionDto.builder()
                .functionType(FunctionTypeEnum.KLASS)
                .permissionType(PermissionTypeEnum.EDIT)
                .build();
    }

    public static BranchUserDto.ResponseBranchUserPermissionDto getBranchUserPermissionMockup2(){
        return BranchUserDto.ResponseBranchUserPermissionDto.builder()
                .functionType(FunctionTypeEnum.SCHEDULE)
                .permissionType(PermissionTypeEnum.VIEW)
                .build();
    }

    public static BranchTuteeDto.ResponseBranchTuteeListDto getBranchTuteeListMockup(){
        return BranchTuteeDto.ResponseBranchTuteeListDto.builder()
                .results(Arrays.asList(getBranchTuteeMockup(), getBranchTuteeMockup()))
                .maxResults(10)
                .currentPage(1)
                .totalPages(1)
                .totalElements(1L)
                .build();
    }

    public static BranchTuteeDto.ResponseBranchTuteeDto getBranchTuteeMockup(){
        return BranchTuteeDto.ResponseBranchTuteeDto.builder()
                .branchId(1L)
                .branchUserRoleId(1L)
                .userId(1L)
                .username("nodaji")
                .tuteeName("노다지")
                .roleType(UserRoleEnum.TUTEE)
                .remainReservationCount(10)
                .remainPeriod(30)
                .build();

}
}
