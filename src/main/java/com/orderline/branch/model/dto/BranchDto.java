package com.orderline.branch.model.dto;

import com.orderline.branch.enums.NoticeMethodType;
import com.orderline.branch.enums.ScheduleAutoFinishTypeEnum;
import com.orderline.branch.model.entity.Branch;
import com.orderline.branch.model.entity.BranchUserRole;
import com.orderline.common.user.enums.UserRoleEnum;
import com.orderline.common.user.model.entity.User;
import io.swagger.annotations.ApiModelProperty;
import com.orderline.branch.enums.ReservationTypeEnum;
import com.orderline.branch.enums.SubjectTypeEnum;
import lombok.*;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BranchDto {

    static final String TIME_PATTERN ="^(?:[01]\\d|2[0-3]):(?:[0-5]\\d)~((?:[01]\\d|2[0-3]):(?:[0-5]\\d)|24:00)$";
    static final String TIME_PATTERN_MESSAGE = "00:00~24:00 사이로 입력해주세요";

    @Builder
    @Getter
    public static class ResponseBranchDto {

        @ApiModelProperty(value = "지점 id")
        private Long branchId;

        @ApiModelProperty(value = "지점명")
        private String branchName;

        @ApiModelProperty(value = "지점 대표 사진")
        private String imageUri;

        @ApiModelProperty(value = "분야")
        private SubjectTypeEnum subjectType;

        @ApiModelProperty(value = "상세 분야")
        private String subjectDetail;

        @ApiModelProperty(value = "지점의 예약 타입")
        private ReservationTypeEnum reservationType;

        @ApiModelProperty(value = "지점 보관 여부 (false: 사용, true: 보관)")
        private Boolean branchArchiveYn;

        @ApiModelProperty(value = "강사 보관 여부 (false: 사용, true: 보관)")
        private Boolean tutorArchiveYn;

        @ApiModelProperty(value = "지점 강사 수")
        private Integer tutorCount;

        @ApiModelProperty(value = "지점 회원 수")
        private Integer tuteeCount;

        @ApiModelProperty(value = "지점의 개인 메모")
        private String privateMemo;

        @ApiModelProperty(value = "지점의 공개 메모")
        private String publicMemo;

        @ApiModelProperty(value = "한주의 시작 요일")
        private String startWeekType;

        @ApiModelProperty(value = "기본 선택 시간")
        private Integer selectTime;

        @ApiModelProperty(value = "일정 자동 완료")
        private ScheduleAutoFinishTypeEnum scheduleAutoFinishType;

        @ApiModelProperty(value = "수강권 자동 완료")
        private Boolean ticketAutoFinishType;

        @ApiModelProperty(value = "출석시 서명")
        private Boolean signUpOnAttendanceYn;

        @ApiModelProperty(value = "회원 예약 허용")
        private Boolean reservationEnableYn;

        @ApiModelProperty(value = "예약 허용 일자")
        private Integer reservationAvailableDate;

        @ApiModelProperty(value = "예약 허용 일자 표시 여부")
        private Boolean reservationNumDisplayYn;

        @ApiModelProperty(value = "수용 가능 인원 수 표시 여부")
        private Boolean capacityNumDisplayYn;

        @ApiModelProperty(value = "예약 허용 시간대")
        private String reservationEnableTimes;

        @ApiModelProperty(value = "회원에게 일정 변경 알림")
        private Boolean alarmChangeScheduleToTuteeYn;

        @ApiModelProperty(value = "강사에게 일정 변경 알림")
        private Boolean alarmChangeScheduleToTutorYn;

        @ApiModelProperty(value = "관리자에게 일정 변경 알림")
        private Boolean alarmChangeScheduleToManagerYn;

        @ApiModelProperty(value = "일정 시작전 알림 시간")
        private Integer alarmNoticeScheduleStartTime;

        @ApiModelProperty(value = "일정 시작전 알림 방법")
        private NoticeMethodType alarmNoticeScheduleStartTimeMethodType;

        @ApiModelProperty(value = "수강권 종료전 알림 일자")
        private Integer alarmNoticeTicketEndDate;

        @ApiModelProperty(value = "수강권 종료전 알림 방법")
        private NoticeMethodType alarmNoticeTicketEndDateMethodType;

        @ApiModelProperty(value = "일요일 영업시간")
        private List<String> sunOperatingHours;

        @ApiModelProperty(value = "월요일 영업시간")
        private List<String> monOperatingHours;

        @ApiModelProperty(value = "화요일 영업시간")
        private List<String> tueOperatingHours;

        @ApiModelProperty(value = "수요일 영업시간")
        private List<String> wedOperatingHours;

        @ApiModelProperty(value = "목요일 영업시간")
        private List<String> thrOperatingHours;

        @ApiModelProperty(value = "금요일 영업시간")
        private List<String> friOperatingHours;

        @ApiModelProperty(value = "토요일 영업시간")
        private List<String> satOperatingHours;

        @ApiModelProperty(value = "지점 사용자 id")
        private Long branchUserRoleId;

        @ApiModelProperty(value = "지점 마지막 조회 여부")
        private Boolean lastViewYn;

        private UserRoleEnum roleType;

        public static ResponseBranchDto toDto(final Branch branch){
            return ResponseBranchDto.builder()
                    .branchId(branch.getId())
                    .branchName(branch.getBranchName())
                    .imageUri(branch.getImageUri())
                    .subjectType(branch.getSubjectType())
                    .subjectDetail(branch.getSubjectDetail())
                    .reservationType(branch.getReservationType())
                    .branchArchiveYn(branch.getArchiveYn())
                    .tutorCount(branch.getTutorCount())
                    .tuteeCount(branch.getTuteeCount())
                    .privateMemo(branch.getPrivateMemo())
                    .publicMemo(branch.getPublicMemo())
                    .startWeekType(branch.getStartWeekType())
                    .selectTime(branch.getSelectTime())
                    .scheduleAutoFinishType(branch.getScheduleAutoFinishType())
                    .ticketAutoFinishType(branch.getTicketAutoFinishType())
                    .signUpOnAttendanceYn(branch.getSignUpOnAttendanceYn())
                    .reservationEnableYn(branch.getReservationEnableYn())
                    .reservationAvailableDate(branch.getReservationAvailableDate())
                    .reservationNumDisplayYn(branch.getReservationNumDisplayYn())
                    .capacityNumDisplayYn(branch.getCapacityNumDisplayYn())
                    .reservationEnableTimes(branch.getReservationEnableTimes())
                    .alarmChangeScheduleToTuteeYn(branch.getAlarmChangeScheduleToTuteeYn())
                    .alarmChangeScheduleToTutorYn(branch.getAlarmChangeScheduleToTutorYn())
                    .alarmChangeScheduleToManagerYn(branch.getAlarmChangeScheduleToManagerYn())
                    .alarmNoticeScheduleStartTime(branch.getAlarmNoticeScheduleStartTime())
                    .alarmNoticeScheduleStartTimeMethodType(branch.getAlarmNoticeScheduleStartTimeMethodType())
                    .alarmNoticeTicketEndDate(branch.getAlarmNoticeTicketEndDate())
                    .alarmNoticeTicketEndDateMethodType(branch.getAlarmNoticeTicketEndDateMethodType())
                    .sunOperatingHours(branch.getSunOperatingHours() == null ? null : Arrays.asList(branch.getSunOperatingHours().split(",")))
                    .monOperatingHours(branch.getMonOperatingHours() == null ? null : Arrays.asList(branch.getMonOperatingHours().split(",")))
                    .tueOperatingHours(branch.getTueOperatingHours() == null ? null : Arrays.asList(branch.getTueOperatingHours().split(",")))
                    .wedOperatingHours(branch.getWedOperatingHours() == null ? null : Arrays.asList(branch.getWedOperatingHours().split(",")))
                    .thrOperatingHours(branch.getThrOperatingHours() == null ? null : Arrays.asList(branch.getThrOperatingHours().split(",")))
                    .friOperatingHours(branch.getFriOperatingHours() == null ? null : Arrays.asList(branch.getFriOperatingHours().split(",")))
                    .satOperatingHours(branch.getSatOperatingHours() == null ? null : Arrays.asList(branch.getSatOperatingHours().split(",")))
                    .build();
        }

        public static ResponseBranchDto toDto(final BranchUserRole branchUserRole){
            final Branch branch = branchUserRole.getBranch();
            return ResponseBranchDto.builder()
                    .branchId(branch.getId())
                    .branchName(branch.getBranchName())
                    .imageUri(branch.getImageUri())
                    .subjectType(branch.getSubjectType())
                    .subjectDetail(branch.getSubjectDetail())
                    .reservationType(branch.getReservationType())
                    .branchArchiveYn(branch.getArchiveYn())
                    .tutorArchiveYn(branchUserRole.getArchiveYn())
                    .tutorCount(branch.getTutorCount())
                    .tuteeCount(branch.getTuteeCount())
                    .privateMemo(branch.getPrivateMemo())
                    .publicMemo(branch.getPublicMemo())
                    .startWeekType(branch.getStartWeekType())
                    .selectTime(branch.getSelectTime())
                    .scheduleAutoFinishType(branch.getScheduleAutoFinishType())
                    .ticketAutoFinishType(branch.getTicketAutoFinishType())
                    .signUpOnAttendanceYn(branch.getSignUpOnAttendanceYn())
                    .reservationEnableYn(branch.getReservationEnableYn())
                    .reservationAvailableDate(branch.getReservationAvailableDate())
                    .reservationNumDisplayYn(branch.getReservationNumDisplayYn())
                    .capacityNumDisplayYn(branch.getCapacityNumDisplayYn())
                    .reservationEnableTimes(branch.getReservationEnableTimes())
                    .alarmChangeScheduleToTuteeYn(branch.getAlarmChangeScheduleToTuteeYn())
                    .alarmChangeScheduleToTutorYn(branch.getAlarmChangeScheduleToTutorYn())
                    .alarmChangeScheduleToManagerYn(branch.getAlarmChangeScheduleToManagerYn())
                    .alarmNoticeScheduleStartTime(branch.getAlarmNoticeScheduleStartTime())
                    .alarmNoticeScheduleStartTimeMethodType(branch.getAlarmNoticeScheduleStartTimeMethodType())
                    .alarmNoticeTicketEndDate(branch.getAlarmNoticeTicketEndDate())
                    .alarmNoticeTicketEndDateMethodType(branch.getAlarmNoticeTicketEndDateMethodType())
                    .sunOperatingHours(branch.getSunOperatingHours() == null ? null : Arrays.asList(branch.getSunOperatingHours().split(",")))
                    .monOperatingHours(branch.getMonOperatingHours() == null ? null : Arrays.asList(branch.getMonOperatingHours().split(",")))
                    .tueOperatingHours(branch.getTueOperatingHours() == null ? null : Arrays.asList(branch.getTueOperatingHours().split(",")))
                    .wedOperatingHours(branch.getWedOperatingHours() == null ? null : Arrays.asList(branch.getWedOperatingHours().split(",")))
                    .thrOperatingHours(branch.getThrOperatingHours() == null ? null : Arrays.asList(branch.getThrOperatingHours().split(",")))
                    .friOperatingHours(branch.getFriOperatingHours() == null ? null : Arrays.asList(branch.getFriOperatingHours().split(",")))
                    .satOperatingHours(branch.getSatOperatingHours() == null ? null : Arrays.asList(branch.getSatOperatingHours().split(",")))
                    .branchUserRoleId(branchUserRole.getId())
                    .lastViewYn(branchUserRole.getLastViewYn())
                    .roleType(branchUserRole.getRoleType())
                    .build();
        }

    }

    @Builder
    @Getter
    public static class ResponseBranchListDto {

        @ApiModelProperty(value = "지점 리스트")
        private List<BranchDto.ResponseBranchDto> results;
        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static BranchDto.ResponseBranchListDto build(Page<BranchDto.ResponseBranchDto> responseDtoPage, Integer currentPage, Integer maxResults){
            return ResponseBranchListDto.builder()
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
    public static class RequestBranchDto {

        @NotBlank(message = "지점명을 입력해주세요.")
        @ApiModelProperty(value = "지점명(필수)", example = "필라테스 A")
        private String branchName;

        @ApiModelProperty(value = "지점 대표 사진", example = "")
        private String imageUri;

        @NotNull(message = "분야를 입력해주세요.")
        @ApiModelProperty(value = "분야(필수)", example = "SPORTS")
        private SubjectTypeEnum subjectType;

        @NotBlank(message = "상세분야를 입력해주세요.")
        @ApiModelProperty(value = "상세 분야(필수)", example = "요가")
        private String subjectDetail;

        @NotNull(message = "지점의 예약 타입을 선택해주세요")
        @ApiModelProperty(value = "지점의 예약 타입")
        private ReservationTypeEnum reservationType;

        public Branch toEntity(final User owner){
            return Branch.builder()
                    .owner(owner)
                    .branchName(branchName)
                    .imageUri(imageUri)
                    .subjectType(subjectType)
                    .subjectDetail(subjectDetail)
                    .reservationType(reservationType)
                    .build();
        }
    }

    //setting 관련
    @Builder
    @Getter
    public static class ResponseBranchSettingDto {

        @ApiModelProperty(value = "일요일 영업시간")
        private List<String> sunOperatingHours;

        @ApiModelProperty(value = "월요일 영업시간")
        private List<String> monOperatingHours;

        @ApiModelProperty(value = "화요일 영업시간")
        private List<String> tueOperatingHours;

        @ApiModelProperty(value = "수요일 영업시간")
        private List<String> wedOperatingHours;

        @ApiModelProperty(value = "목요일 영업시간")
        private List<String> thrOperatingHours;

        @ApiModelProperty(value = "금요일 영업시간")
        private List<String> friOperatingHours;

        @ApiModelProperty(value = "토요일 영업시간")
        private List<String> satOperatingHours;

        @ApiModelProperty(value = "한 주의 시작 요일")
        private String startWeekType;

        @ApiModelProperty(value = "기본 선택 시간")
        private Integer selectTime;

        @ApiModelProperty(value = "일정 자동 완료")
        private ScheduleAutoFinishTypeEnum scheduleAutoFinishType;

        @ApiModelProperty(value = "수강권 자동 완료")
        private Boolean ticketAutoFinishType;

        @ApiModelProperty(value = "출석시 서명")
        private Boolean signUpOnAttendanceYn;

        @ApiModelProperty(value = "회원 예약 허용")
        private Boolean reservationEnableYn;

        @ApiModelProperty(value = "예약 허용 일자")
        private Integer reservationAvailableDate;

        @ApiModelProperty(value = "예약 허용 일자 표시 여부")
        private Boolean reservationNumDisplayYn;

        @ApiModelProperty(value = "수용 가능 인원 수 표시 여부")
        private Boolean capacityNumDisplayYn;

        @ApiModelProperty(value = "예약 허용 시간대")
        private String reservationEnableTimes;

        @ApiModelProperty(value = "회원에게 일정 변경 알림")
        private Boolean alarmChangeScheduleToTuteeYn;

        @ApiModelProperty(value = "강사에게 일정 변경 알림")
        private Boolean alarmChangeScheduleToTutorYn;

        @ApiModelProperty(value = "관리자에게 일정 변경 알림")
        private Boolean alarmChangeScheduleToManagerYn;

        @ApiModelProperty(value = "일정 시작전 알림 시간")
        private Integer alarmNoticeScheduleStartTime;

        @ApiModelProperty(value = "일정 시작전 알림 방법")
        private NoticeMethodType alarmNoticeScheduleStartTimeMethodType;

        @ApiModelProperty(value = "수강권 종료전 알림 일자")
        private Integer alarmNoticeTicketEndDate;

        @ApiModelProperty(value = "수강권 종료전 알림 방법")
        private NoticeMethodType alarmNoticeTicketEndDateMethodType;

        public static ResponseBranchSettingDto toDto(final Branch branch){
            return ResponseBranchSettingDto.builder()
                    .sunOperatingHours(branch.getSunOperatingHours() == null ? null : Arrays.asList(branch.getSunOperatingHours().split(",")))
                    .monOperatingHours(branch.getMonOperatingHours() == null ? null : Arrays.asList(branch.getMonOperatingHours().split(",")))
                    .tueOperatingHours(branch.getTueOperatingHours() == null ? null : Arrays.asList(branch.getTueOperatingHours().split(",")))
                    .wedOperatingHours(branch.getWedOperatingHours() == null ? null : Arrays.asList(branch.getWedOperatingHours().split(",")))
                    .thrOperatingHours(branch.getThrOperatingHours() == null ? null : Arrays.asList(branch.getThrOperatingHours().split(",")))
                    .friOperatingHours(branch.getFriOperatingHours() == null ? null : Arrays.asList(branch.getFriOperatingHours().split(",")))
                    .satOperatingHours(branch.getSatOperatingHours() == null ? null : Arrays.asList(branch.getSatOperatingHours().split(",")))
                    .startWeekType(branch.getStartWeekType())
                    .selectTime(branch.getSelectTime())
                    .scheduleAutoFinishType(branch.getScheduleAutoFinishType())
                    .ticketAutoFinishType(branch.getTicketAutoFinishType())
                    .signUpOnAttendanceYn(branch.getSignUpOnAttendanceYn())
                    .reservationEnableYn(branch.getReservationEnableYn())
                    .reservationAvailableDate(branch.getReservationAvailableDate())
                    .reservationNumDisplayYn(branch.getReservationNumDisplayYn())
                    .capacityNumDisplayYn(branch.getCapacityNumDisplayYn())
                    .reservationEnableTimes(branch.getReservationEnableTimes())
                    .alarmChangeScheduleToTuteeYn(branch.getAlarmChangeScheduleToTuteeYn())
                    .alarmChangeScheduleToTutorYn(branch.getAlarmChangeScheduleToTutorYn())
                    .alarmChangeScheduleToManagerYn(branch.getAlarmChangeScheduleToManagerYn())
                    .alarmNoticeScheduleStartTime(branch.getAlarmNoticeScheduleStartTime())
                    .alarmNoticeScheduleStartTimeMethodType(branch.getAlarmNoticeScheduleStartTimeMethodType())
                    .alarmNoticeTicketEndDate(branch.getAlarmNoticeTicketEndDate())
                    .alarmNoticeTicketEndDateMethodType(branch.getAlarmNoticeTicketEndDateMethodType())
                    .build();
        }
    }

    @Builder
    @Getter
    public static class RequestBranchSettingDto {

        @ApiModelProperty(value = "일요일 영업시간", example = "[\"00:00~24:00\"]")
        private List<@Pattern(regexp = TIME_PATTERN, message = "일요일 영업시간을 " + TIME_PATTERN_MESSAGE) String> sunOperatingHours;

        @ApiModelProperty(value = "월요일 영업시간", example = "[\"00:00~24:00\"]")
        private List<@Pattern(regexp = TIME_PATTERN, message = "월요일 영업시간을 " + TIME_PATTERN_MESSAGE) String> monOperatingHours;

        @ApiModelProperty(value = "화요일 영업시간", example = "[\"00:00~24:00\"]")
        private List<@Pattern(regexp = TIME_PATTERN, message = "화요일 영업시간을 " + TIME_PATTERN_MESSAGE) String> tueOperatingHours;

        @ApiModelProperty(value = "수요일 영업시간", example = "[\"00:00~24:00\"]")
        private List<@Pattern(regexp = TIME_PATTERN, message = "수요일 영업시간을 " + TIME_PATTERN_MESSAGE) String> wedOperatingHours;

        @ApiModelProperty(value = "목요일 영업시간", example = "[\"00:00~24:00\"]")
        private List<@Pattern(regexp = TIME_PATTERN, message = "목요일 영업시간을 " + TIME_PATTERN_MESSAGE) String> thrOperatingHours;

        @ApiModelProperty(value = "금요일 영업시간", example = "[\"00:00~24:00\"]")
        private List<@Pattern(regexp = TIME_PATTERN, message = "금요일 영업시간을 " + TIME_PATTERN_MESSAGE) String> friOperatingHours;

        @ApiModelProperty(value = "토요일 영업시간", example = "[\"00:00~24:00\"]")
        private List<@Pattern(regexp = TIME_PATTERN, message = "토요일 영업시간을 " + TIME_PATTERN_MESSAGE) String> satOperatingHours;

        @ApiModelProperty(value = "한 주의 시작 요일", example = "SUN")
        private String startWeekType;

        @ApiModelProperty(value = "기본 선택 시간", example = "30")
        private Integer selectTime;

        @ApiModelProperty(value = "일정 자동 완료", example = "OFF")
        private ScheduleAutoFinishTypeEnum scheduleAutoFinishType;

        @ApiModelProperty(value = "수강권 자동 완료", example = "false")
        private Boolean ticketAutoFinishType;

        @ApiModelProperty(value = "출석시 서명", example = "false")
        private Boolean signUpOnAttendanceYn;

        @ApiModelProperty(value = "회원 예약 허용", example = "false")
        private Boolean reservationEnableYn;

        @ApiModelProperty(value = "예약 허용 일자", example = "14")
        private Integer reservationAvailableDate;

        @ApiModelProperty(value = "예약자 수 표시", example = "false")
        private Boolean reservationNumDisplayYn;

        @ApiModelProperty(value = "정원 표시", example = "true")
        private Boolean capacityNumDisplayYn;

        @Pattern(regexp = TIME_PATTERN, message = "예약 허용 시간대를 " + TIME_PATTERN_MESSAGE)
        @ApiModelProperty(value = "예약 허용 시간대", example = "00:00~24:00")
        private String reservationEnableTimes;

        @ApiModelProperty(value = "회원에게 일정 변경 알림", example = "true")
        private Boolean alarmChangeScheduleToTuteeYn;

        @ApiModelProperty(value = "강사에게 일정 변경 알림", example = "true")
        private Boolean alarmChangeScheduleToTutorYn;

        @ApiModelProperty(value = "관리자에게 일정 변경 알림", example = "true")
        private Boolean alarmChangeScheduleToManagerYn;

        @ApiModelProperty(value = "일정 시작전 알림 시간", example = "0") //0은 설정안함?
        private Integer alarmNoticeScheduleStartTime;

        @ApiModelProperty(value = "일정 시작전 알림 방법", example = "PUSH")
        private NoticeMethodType alarmNoticeScheduleStartTimeMethodType;

        @ApiModelProperty(value = "수강권 종료전 알림 일자")
        private Integer alarmNoticeTicketEndDate;

        @ApiModelProperty(value = "수강권 종료전 알림 방법", example = "PUSH")
        private NoticeMethodType alarmNoticeTicketEndDateMethodType;

    }

}

