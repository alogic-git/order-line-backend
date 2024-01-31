package com.orderline.klass.model.dto;

import com.orderline.basic.annotation.Multiple;
import com.orderline.basic.model.dto.CommonDto;
import com.orderline.branch.model.entity.Branch;
import com.orderline.branch.model.entity.BranchUserRole;
import com.orderline.klass.model.entity.Klass;
import com.orderline.product.model.entity.ProductKlass;
import io.swagger.annotations.ApiModelProperty;
import com.orderline.klass.enums.KlassColorTypeEnum;
import com.orderline.klass.enums.KlassLevelTypeEnum;
import com.orderline.klass.enums.KlassStartTimeTypeEnum;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import javax.validation.constraints.*;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KlassDto {

    @Getter
    @Builder
    public static class ResponseKlassDto {

        @ApiModelProperty(value = "클래스 ID")
        private Long klassId;

        @ApiModelProperty(value = "지점 ID")
        private Long branchId;

        @ApiModelProperty(value = "클래스 이름")
        private String klassName;

        @ApiModelProperty(value = "메인 강사 ID")
        private Long mainTutorId;

        @ApiModelProperty(value = "메인 강사 닉네임")
        private String mainTutorNickName;

        @ApiModelProperty(value = "보조 강사 ID")
        private Long subTutorId;

        @ApiModelProperty(value = "보조 강사 닉네임")
        private String subTutorNickName;

        @ApiModelProperty(value = "최소 정원")
        private Integer minTuteeNum;

        @ApiModelProperty(value = "최대 정원")
        private Integer maxTuteeNum;

        @ApiModelProperty(value = "클래스 시간")
        private Integer klassTime;

        @ApiModelProperty(value = "회원 예약 가능 시작 시각")
        private KlassStartTimeTypeEnum startTime;

        @ApiModelProperty(value = "난이도")
        private KlassLevelTypeEnum level;

        @ApiModelProperty(value = "수업 색상 코드")
        private CommonDto.ColorCodeDto colorCodeDto;

        @ApiModelProperty(value = "회원 수")
        private Integer tuteeCount;

        @ApiModelProperty(value = "관리자 및 강사 전용 메모")
        private String privateMemo;

        @ApiModelProperty(value = "회원 공유 메모")
        private String publicMemo;

        @ApiModelProperty(value = "수강권 차감 횟수")
        private Integer reservationCount;

        @ApiModelProperty(value = "예약 허용 여부")
        private Boolean reservationEnableYn;

        @ApiModelProperty(value = "예약 가능 시간")
        private Integer reservationEnableTime;

        @ApiModelProperty(value = "취소 가능 시간")
        private Integer cancelEnableTime;

        @ApiModelProperty(value = "최소 정원 미충족시 자동 취소 시간")
        private Integer minTuteeLackCancelTime;

        @ApiModelProperty(value = "대기 예약 허용 정원")
        private Integer waitingTuteeNum;

        @ApiModelProperty(value = "대기 예약 자동 취소 시간")
        private Integer waitingReservationCancelTime;

        @ApiModelProperty(value = "같은 시간에 중복 예약 허용 여부")
        private Boolean duplicateReservationYn;

        @ApiModelProperty(value = "예약 시작 시각")
        private String reservationStartTime;

        @ApiModelProperty(value = "체크인 가능 시간(시작 전 x분)")
        private Integer checkInEnableBeforeTime;

        @ApiModelProperty(value = "체크인 가능 시간(시작 후 x분)")
        private Integer checkInEnableAfterTime;

        @ApiModelProperty(value = "체크아웃 가능 시간(종료 전 x분)")
        private Integer checkOutEnableBeforeTime;

        @ApiModelProperty(value = "체크아웃 가능 시간(종료 후 x분)")
        private Integer checkOutEnableAfterTime;

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

        @ApiModelProperty(value = "보관 여부")
        private Boolean archiveYn;

        public static ResponseKlassDto toDto(final Klass klass){
            return ResponseKlassDto.builder()
                    .klassId(klass.getId())
                    .branchId(klass.getBranch().getId())
                    .klassName(klass.getKlassName())
                    .mainTutorId(klass.getMainTutor().getId())
                    .mainTutorNickName(klass.getMainTutor().getNickname())
                    .subTutorId(klass.getSubTutor() == null ? null : klass.getSubTutor().getId())
                    .subTutorNickName(klass.getSubTutor() == null ? null : klass.getSubTutor().getNickname())
                    .minTuteeNum(klass.getMinTuteeNum())
                    .maxTuteeNum(klass.getMaxTuteeNum())
                    .klassTime(klass.getKlassTime())
                    .startTime(klass.getStartTime())
                    .level(klass.getLevel())
                    .colorCodeDto(CommonDto.ColorCodeDto.toDto(klass.getColorCdType()))
                    .tuteeCount(klass.getTuteeCount())
                    .privateMemo(klass.getPrivateMemo())
                    .publicMemo(klass.getPublicMemo())
                    .reservationCount(klass.getReservationCount())
                    .reservationEnableYn(klass.getReservationEnableYn())
                    .reservationEnableTime(klass.getReservationEnableTime())
                    .cancelEnableTime(klass.getCancelEnableTime())
                    .minTuteeLackCancelTime(klass.getMinTuteeLackCancelTime())
                    .waitingTuteeNum(klass.getWaitingTuteeNum())
                    .waitingReservationCancelTime(klass.getWaitingReservationCancelTime())
                    .duplicateReservationYn(klass.getDuplicateReservationYn())
                    .reservationStartTime(klass.getReservationStartTime())
                    .checkInEnableBeforeTime(klass.getCheckInEnableBeforeTime())
                    .checkInEnableAfterTime(klass.getCheckInEnableAfterTime())
                    .checkOutEnableBeforeTime(klass.getCheckOutEnableBeforeTime())
                    .checkOutEnableAfterTime(klass.getCheckOutEnableAfterTime())
                    .sunOperatingHours(klass.getSunOperatingHours() == null ? null : Arrays.asList(klass.getSunOperatingHours().split(",")))
                    .monOperatingHours(klass.getMonOperatingHours() == null ? null : Arrays.asList(klass.getMonOperatingHours().split(",")))
                    .tueOperatingHours(klass.getTueOperatingHours() == null ? null : Arrays.asList(klass.getTueOperatingHours().split(",")))
                    .wedOperatingHours(klass.getWedOperatingHours() == null ? null : Arrays.asList(klass.getWedOperatingHours().split(",")))
                    .thrOperatingHours(klass.getThrOperatingHours() == null ? null : Arrays.asList(klass.getThrOperatingHours().split(",")))
                    .friOperatingHours(klass.getFriOperatingHours() == null ? null : Arrays.asList(klass.getFriOperatingHours().split(",")))
                    .satOperatingHours(klass.getSatOperatingHours() == null ? null : Arrays.asList(klass.getSatOperatingHours().split(",")))
                    .archiveYn(klass.getArchiveYn())
                    .build();
        }

        public static ResponseKlassDto toDto(final ProductKlass productKlass){
            Klass klass = productKlass.getKlass();
            return ResponseKlassDto.builder()
                    .klassId(klass.getId())
                    .branchId(klass.getBranch().getId())
                    .klassName(klass.getKlassName())
                    .mainTutorId(klass.getMainTutor().getId())
                    .mainTutorNickName(klass.getMainTutor().getNickname())
                    .subTutorId(klass.getSubTutor() == null ? null : klass.getSubTutor().getId())
                    .subTutorNickName(klass.getSubTutor() == null ? null : klass.getSubTutor().getNickname())
                    .minTuteeNum(klass.getMinTuteeNum())
                    .maxTuteeNum(klass.getMaxTuteeNum())
                    .klassTime(klass.getKlassTime())
                    .startTime(klass.getStartTime())
                    .level(klass.getLevel())
                    .colorCodeDto(CommonDto.ColorCodeDto.toDto(klass.getColorCdType()))
                    .tuteeCount(klass.getTuteeCount())
                    .privateMemo(klass.getPrivateMemo())
                    .publicMemo(klass.getPublicMemo())
                    .reservationCount(klass.getReservationCount())
                    .reservationEnableYn(klass.getReservationEnableYn())
                    .reservationEnableTime(klass.getReservationEnableTime())
                    .cancelEnableTime(klass.getCancelEnableTime())
                    .minTuteeLackCancelTime(klass.getMinTuteeLackCancelTime())
                    .waitingTuteeNum(klass.getWaitingTuteeNum())
                    .waitingReservationCancelTime(klass.getWaitingReservationCancelTime())
                    .duplicateReservationYn(klass.getDuplicateReservationYn())
                    .reservationStartTime(klass.getReservationStartTime())
                    .checkInEnableBeforeTime(klass.getCheckInEnableBeforeTime())
                    .checkInEnableAfterTime(klass.getCheckInEnableAfterTime())
                    .checkOutEnableBeforeTime(klass.getCheckOutEnableBeforeTime())
                    .checkOutEnableAfterTime(klass.getCheckOutEnableAfterTime())
                    .sunOperatingHours(klass.getSunOperatingHours() == null ? null : Arrays.asList(klass.getSunOperatingHours().split(",")))
                    .monOperatingHours(klass.getMonOperatingHours() == null ? null : Arrays.asList(klass.getMonOperatingHours().split(",")))
                    .tueOperatingHours(klass.getTueOperatingHours() == null ? null : Arrays.asList(klass.getTueOperatingHours().split(",")))
                    .wedOperatingHours(klass.getWedOperatingHours() == null ? null : Arrays.asList(klass.getWedOperatingHours().split(",")))
                    .thrOperatingHours(klass.getThrOperatingHours() == null ? null : Arrays.asList(klass.getThrOperatingHours().split(",")))
                    .friOperatingHours(klass.getFriOperatingHours() == null ? null : Arrays.asList(klass.getFriOperatingHours().split(",")))
                    .satOperatingHours(klass.getSatOperatingHours() == null ? null : Arrays.asList(klass.getSatOperatingHours().split(",")))
                    .archiveYn(klass.getArchiveYn())
                    .build();
        }

    }


    @Builder
    @Getter
    public static class ResponseKlassListDto {

        @ApiModelProperty(value = "클래스 리스트")
        private List<ResponseKlassDto> results;
        @ApiModelProperty(value = "현재 페이지")
        private Integer currentPage;

        @ApiModelProperty(value = "페이지당 항목 수")
        private Integer maxResults;

        @ApiModelProperty(value = "전체 페이지")
        private Integer totalPages;

        @ApiModelProperty(value = "전체 항목 수")
        private Long totalElements;

        public static ResponseKlassListDto build(Page<ResponseKlassDto> responseDtoPage, Integer currentPage, Integer maxResults){
            return ResponseKlassListDto.builder()
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
    public static class RequestFreeKlassDto {

        @NotBlank(message = "클래스명을 입력해주세요")
        @ApiModelProperty(value = "클래스 이름(필수)", example = "고배배 요가")
        private String klassName;

        @NotNull(message = "담당 강사를 선택해주세요")
        @ApiModelProperty(value = "담당 강사 ID(필수)", example = "207")
        private Long mainTutorId;

        @ApiModelProperty(value = "보조 강사 ID")
        private Long subTutorId;

        @ApiModelProperty(value = "최소 정원", example = "1")
        @Min(value = 1, message = "최소 정원은 1명 이상을 입력해주세요")
        private Integer minTuteeNum;

        @NotNull(message = "최대 정원을 입력해주세요")
        @Min(value = 1, message = "최대 정원은 1명 이상을 입력해주세요")
        @ApiModelProperty(value = "최대 정원(필수)", example = "5")
        private Integer maxTuteeNum;

        @Multiple(value = 5, message = "기본 클래스 수업 시간은 5분 단위로 입력해주세요")
        @Positive(message = "기본 클래스 수업 시간은 5분 이상을 입력해주세요")
        @ApiModelProperty(value = "기본 클래스 수업 시간(필수)", example = "60")
        private Integer klassTime;

        @NotNull(message = "클래스 시작 시간을 입력해주세요")
        @ApiModelProperty(value = "회원 예약 시작 시각(필수)", example = "A0")
        private KlassStartTimeTypeEnum startTime;

        @ApiModelProperty(value = "난이도")
        private KlassLevelTypeEnum level;

        @ApiModelProperty(value = "색상 Type", example = "PLUM")
        private KlassColorTypeEnum colorCdType;

        @ApiModelProperty(value = "관리자 및 강사 전용 메모")
        @Size(max = 200, message = "관리자 및 강사 전용 메모는 200자 이내로 입력해주세요")
        private String privateMemo;

        @ApiModelProperty(value = "회원 공유 메모")
        @Size(max = 200, message = "회원 공유 메모는 200자 이내로 입력해주세요")
        private String publicMemo;

        @ApiModelProperty(value = "수강권 차감 횟수")
        private Integer reservationCount;

        @ApiModelProperty(value = "예약 허용 여부")
        private Boolean reservationEnableYn;

        @ApiModelProperty(value = "예약 가능 시간")
        private Integer reservationEnableTime;

        @ApiModelProperty(value = "취소 가능 시간")
        private Integer cancelEnableTime;

        @ApiModelProperty(value = "최소 정원 미충족시 자동 취소 시간")
        private Integer minTuteeLackCancelTime;

        @ApiModelProperty(value = "대기 예약 허용 정원")
        private Integer waitingTuteeNum;

        @ApiModelProperty(value = "대기 예약 자동 취소 시간")
        private Integer waitingReservationCancelTime;

        @ApiModelProperty(value = "같은 시간에 중복 예약 허용 여부")
        private Boolean duplicateReservationYn;

        @ApiModelProperty(value = "예약 시작 시각")
        private String reservationStartTime;

        @ApiModelProperty(value = "체크인 가능 시간(시작 전 x분)")
        private Integer checkInEnableBeforeTime;

        @ApiModelProperty(value = "체크인 가능 시간(시작 후 x분)")
        private Integer checkInEnableAfterTime;

        @ApiModelProperty(value = "체크아웃 가능 시간(종료 전 x분)")
        private Integer checkOutEnableBeforeTime;

        @ApiModelProperty(value = "체크아웃 가능 시간(종료 후 x분)")
        private Integer checkOutEnableAfterTime;

        @ApiModelProperty(value = "일요일 영업시간", example = "[\"00:00~24:00\"]")
        private List<String> sunOperatingHours;

        @ApiModelProperty(value = "월요일 영업시간", example = "[\"00:00~24:00\"]")
        private List<String> monOperatingHours;

        @ApiModelProperty(value = "화요일 영업시간", example = "[\"00:00~24:00\"]")
        private List<String> tueOperatingHours;

        @ApiModelProperty(value = "수요일 영업시간", example = "[\"00:00~24:00\"]")
        private List<String> wedOperatingHours;

        @ApiModelProperty(value = "목요일 영업시간", example = "[\"00:00~24:00\"]")
        private List<String> thrOperatingHours;

        @ApiModelProperty(value = "금요일 영업시간", example = "[\"00:00~24:00\"]")
        private List<String> friOperatingHours;

        @ApiModelProperty(value = "토요일 영업시간", example = "[\"00:00~24:00\"]")
        private List<String> satOperatingHours;

        public Klass toEntity(final Branch branch, final BranchUserRole mainTutor, final BranchUserRole subTutor){
            return Klass.builder()
                    .branch(branch)
                    .klassName(klassName)
                    .mainTutor(mainTutor)
                    .subTutor(subTutor)
                    .minTuteeNum(minTuteeNum)
                    .maxTuteeNum(maxTuteeNum)
                    .klassTime(klassTime)
                    .startTime(startTime)
                    .level(level)
                    .colorCdType(colorCdType)
                    .privateMemo(privateMemo)
                    .publicMemo(publicMemo)
                    .reservationCount(reservationCount)
                    .reservationEnableYn(reservationEnableYn)
                    .reservationEnableTime(reservationEnableTime)
                    .cancelEnableTime(cancelEnableTime)
                    .minTuteeLackCancelTime(minTuteeLackCancelTime)
                    .waitingTuteeNum(waitingTuteeNum)
                    .waitingReservationCancelTime(waitingReservationCancelTime)
                    .duplicateReservationYn(duplicateReservationYn)
                    .reservationStartTime(reservationStartTime)
                    .checkInEnableBeforeTime(checkInEnableBeforeTime)
                    .checkInEnableAfterTime(checkInEnableAfterTime)
                    .checkOutEnableBeforeTime(checkOutEnableBeforeTime)
                    .checkOutEnableAfterTime(checkOutEnableAfterTime)
                    .sunOperatingHours(String.join(",",sunOperatingHours))
                    .monOperatingHours(String.join(",",monOperatingHours))
                    .tueOperatingHours(String.join(",",tueOperatingHours))
                    .wedOperatingHours(String.join(",",wedOperatingHours))
                    .thrOperatingHours(String.join(",",thrOperatingHours))
                    .friOperatingHours(String.join(",",friOperatingHours))
                    .satOperatingHours(String.join(",",satOperatingHours))
                    .build();
        }
    }

    @Getter
    @Builder
    public  static class RequestFreeKlassSettingDto {

        @ApiModelProperty(value = "예약 허용 여부")
        private Boolean reservationEnableYn;

        @ApiModelProperty(value = "예약 가능 시간")
        private Integer reservationEnableTime;

        @ApiModelProperty(value = "취소 가능 시간")
        private Integer cancelEnableTime;

        @ApiModelProperty(value = "최소 정원 미충족시 자동 취소 시간")
        private Integer minTuteeLackCancelTime;

        @ApiModelProperty(value = "대기 예약 허용 정원")
        private Integer waitingTuteeNum;

        @ApiModelProperty(value = "대기 예약 자동 취소 시간")
        private Integer waitingReservationCancelTime;

        @ApiModelProperty(value = "같은 시간에 중복 예약 허용 여부")
        private Boolean duplicateReservationYn;

        @ApiModelProperty(value = "예약 시작 시각")
        private String reservationStartTime;

        @ApiModelProperty(value = "체크인 가능 시간(시작 전 x분)")
        private Integer checkInEnableBeforeTime;

        @ApiModelProperty(value = "체크인 가능 시간(시작 후 x분)")
        private Integer checkInEnableAfterTime;

        @ApiModelProperty(value = "체크아웃 가능 시간(종료 전 x분)")
        private Integer checkOutEnableBeforeTime;

        @ApiModelProperty(value = "체크아웃 가능 시간(종료 후 x분)")
        private Integer checkOutEnableAfterTime;

        public Klass toEntity(final Branch branch){
            return Klass.builder()
                    .branch(branch)
                    .reservationEnableYn(reservationEnableYn)
                    .reservationEnableTime(reservationEnableTime)
                    .cancelEnableTime(cancelEnableTime)
                    .minTuteeLackCancelTime(minTuteeLackCancelTime)
                    .waitingTuteeNum(waitingTuteeNum)
                    .waitingReservationCancelTime(waitingReservationCancelTime)
                    .duplicateReservationYn(duplicateReservationYn)
                    .reservationStartTime(reservationStartTime)
                    .checkInEnableBeforeTime(checkInEnableBeforeTime)
                    .checkInEnableAfterTime(checkInEnableAfterTime)
                    .checkOutEnableBeforeTime(checkOutEnableBeforeTime)
                    .checkOutEnableAfterTime(checkOutEnableAfterTime)
                    .build();
        }

    }


    @Getter
    @Builder
    public static class RequestLimitedKlassDto {

        @NotBlank(message = "클래스명을 입력해주세요")
        @ApiModelProperty(value = "클래스 이름(필수)")
        private String klassName;

        @NotNull(message = "담당 강사를 선택해주세요")
        @ApiModelProperty(value = "담당 강사 ID(필수)")
        private Long mainTutorId;

        @ApiModelProperty(value = "보조 강사 ID")
        private Long subTutorId;

        @ApiModelProperty(value = "최소 정원", example = "1")
        @Min(value = 1, message = "최소 정원은 1명 이상을 입력해주세요")
        private Integer minTuteeNum;

        @NotNull(message = "최대 정원을 입력해주세요")
        @Min(value = 1, message = "최대 정원은 1명 이상을 입력해주세요")
        @ApiModelProperty(value = "최대 정원(필수)", example = "5")
        private Integer maxTuteeNum;

        @Multiple(value = 5, message = "기본 클래스 수업 시간은 5분 단위로 입력해주세요")
        @Positive(message = "기본 클래스 수업 시간은 5분 이상을 입력해주세요")
        @ApiModelProperty(value = "기본 클래스 수업 시간(필수)", example = "60")
        private Integer klassTime;

        @ApiModelProperty(value = "난이도")
        private KlassLevelTypeEnum level;

        @ApiModelProperty(value = "색상 Type", example = "SUN_RED")
        private KlassColorTypeEnum colorCdType;

        @ApiModelProperty(value = "관리자 및 강사 전용 메모")
        @Size(max = 200, message = "관리자 및 강사 전용 메모는 200자 이내로 입력해주세요")
        private String privateMemo;

        @ApiModelProperty(value = "회원 공유 메모")
        @Size(max = 200, message = "회원 공유 메모는 200자 이내로 입력해주세요")
        private String publicMemo;

        @ApiModelProperty(value = "수강권 차감 횟수")
        private Integer reservationCount;

        @ApiModelProperty(value = "예약 허용 여부")
        private Boolean reservationEnableYn;

        @ApiModelProperty(value = "예약 가능 시간")
        private Integer reservationEnableTime;

        @ApiModelProperty(value = "취소 가능 시간")
        private Integer cancelEnableTime;

        @ApiModelProperty(value = "최소 정원 미충족시 자동 취소 시간")
        private Integer minTuteeLackCancelTime;

        @ApiModelProperty(value = "대기 예약 허용 정원")
        private Integer waitingTuteeNum;

        @ApiModelProperty(value = "대기 예약 자동 취소 시간")
        private Integer waitingReservationCancelTime;

        @ApiModelProperty(value = "같은 시간에 중복 예약 허용 여부")
        private Boolean duplicateReservationYn;

        @ApiModelProperty(value = "예약 시작 시각")
        private String reservationStartTime;

        @ApiModelProperty(value = "체크인 가능 시간(시작 전 x분)")
        private Integer checkInEnableBeforeTime;

        @ApiModelProperty(value = "체크인 가능 시간(시작 후 x분)")
        private Integer checkInEnableAfterTime;

        @ApiModelProperty(value = "체크아웃 가능 시간(종료 전 x분)")
        private Integer checkOutEnableBeforeTime;

        @ApiModelProperty(value = "체크아웃 가능 시간(종료 후 x분)")
        private Integer checkOutEnableAfterTime;

        @ApiModelProperty(value = "일요일 영업시간", example = "[\"00:00~24:00\"]")
        private List<String> sunOperatingHours;

        @ApiModelProperty(value = "월요일 영업시간", example = "[\"00:00~24:00\"]")
        private List<String> monOperatingHours;

        @ApiModelProperty(value = "화요일 영업시간", example = "[\"00:00~24:00\"]")
        private List<String> tueOperatingHours;

        @ApiModelProperty(value = "수요일 영업시간", example = "[\"00:00~24:00\"]")
        private List<String> wedOperatingHours;

        @ApiModelProperty(value = "목요일 영업시간", example = "[\"00:00~24:00\"]")
        private List<String> thrOperatingHours;

        @ApiModelProperty(value = "금요일 영업시간", example = "[\"00:00~24:00\"]")
        private List<String> friOperatingHours;

        @ApiModelProperty(value = "토요일 영업시간", example = "[\"00:00~24:00\"]")
        private List<String> satOperatingHours;

        public Klass toEntity(final Branch branch, final BranchUserRole mainTutor, final BranchUserRole subTutor){
            return Klass.builder()
                    .branch(branch)
                    .klassName(klassName)
                    .mainTutor(mainTutor)
                    .subTutor(subTutor)
                    .minTuteeNum(minTuteeNum)
                    .maxTuteeNum(maxTuteeNum)
                    .klassTime(klassTime)
                    .level(level)
                    .colorCdType(colorCdType)
                    .privateMemo(privateMemo)
                    .publicMemo(publicMemo)
                    .reservationCount(reservationCount)
                    .reservationEnableYn(reservationEnableYn)
                    .reservationEnableTime(reservationEnableTime)
                    .cancelEnableTime(cancelEnableTime)
                    .minTuteeLackCancelTime(minTuteeLackCancelTime)
                    .waitingTuteeNum(waitingTuteeNum)
                    .waitingReservationCancelTime(waitingReservationCancelTime)
                    .duplicateReservationYn(duplicateReservationYn)
                    .reservationStartTime(reservationStartTime)
                    .checkInEnableBeforeTime(checkInEnableBeforeTime)
                    .checkInEnableAfterTime(checkInEnableAfterTime)
                    .checkOutEnableBeforeTime(checkOutEnableBeforeTime)
                    .checkOutEnableAfterTime(checkOutEnableAfterTime)
                    .sunOperatingHours(String.join(",",sunOperatingHours))
                    .monOperatingHours(String.join(",",monOperatingHours))
                    .tueOperatingHours(String.join(",",tueOperatingHours))
                    .wedOperatingHours(String.join(",",wedOperatingHours))
                    .thrOperatingHours(String.join(",",thrOperatingHours))
                    .friOperatingHours(String.join(",",friOperatingHours))
                    .satOperatingHours(String.join(",",satOperatingHours))
                    .build();
        }
    }

    @Getter
    @Builder
    public static class RequestLimitedKlassSettingDto {

        @ApiModelProperty(value = "예약 허용 여부")
        private Boolean reservationEnableYn;

        @ApiModelProperty(value = "예약 가능 시간")
        private Integer reservationEnableTime;

        @ApiModelProperty(value = "취소 가능 시간")
        private Integer cancelEnableTime;

        @ApiModelProperty(value = "최소 정원 미충족시 자동 취소 시간")
        private Integer minTuteeLackCancelTime;

        @ApiModelProperty(value = "대기 예약 허용 정원")
        private Integer waitingTuteeNum;

        @ApiModelProperty(value = "대기 예약 자동 취소 시간")
        private Integer waitingReservationCancelTime;

        @ApiModelProperty(value = "같은 시간에 중복 예약 허용 여부")
        private Boolean duplicateReservationYn;

        @ApiModelProperty(value = "예약 시작 시각")
        private String reservationStartTime;

        @ApiModelProperty(value = "체크인 가능 시간(시작 전 x분)")
        private Integer checkInEnableBeforeTime;

        @ApiModelProperty(value = "체크인 가능 시간(시작 후 x분)")
        private Integer checkInEnableAfterTime;

        @ApiModelProperty(value = "체크아웃 가능 시간(종료 전 x분)")
        private Integer checkOutEnableBeforeTime;

        @ApiModelProperty(value = "체크아웃 가능 시간(종료 후 x분)")
        private Integer checkOutEnableAfterTime;

        public Klass toEntity(final Branch branch) {
            return Klass.builder()
                    .branch(branch)
                    .reservationEnableYn(reservationEnableYn)
                    .reservationEnableTime(reservationEnableTime)
                    .cancelEnableTime(cancelEnableTime)
                    .minTuteeLackCancelTime(minTuteeLackCancelTime)
                    .waitingTuteeNum(waitingTuteeNum)
                    .waitingReservationCancelTime(waitingReservationCancelTime)
                    .duplicateReservationYn(duplicateReservationYn)
                    .reservationStartTime(reservationStartTime)
                    .checkInEnableBeforeTime(checkInEnableBeforeTime)
                    .checkInEnableAfterTime(checkInEnableAfterTime)
                    .checkOutEnableBeforeTime(checkOutEnableBeforeTime)
                    .checkOutEnableAfterTime(checkOutEnableAfterTime)
                    .build();
        }
    }
}

