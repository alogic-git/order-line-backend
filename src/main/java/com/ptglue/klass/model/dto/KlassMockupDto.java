package com.ptglue.klass.model.dto;

import com.ptglue.basic.model.dto.CommonDto;
import com.ptglue.klass.enums.KlassColorTypeEnum;
import com.ptglue.klass.enums.KlassLevelTypeEnum;
import com.ptglue.klass.enums.KlassStartTimeTypeEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KlassMockupDto {
    public static KlassDto.ResponseKlassDto getKlassMockup1 () {
            return KlassDto.ResponseKlassDto.builder()
                    .klassId(1L)
                    .branchId(101L)
                    .klassName("Yoga Class")
                    .mainTutorId(201L)
                    .mainTutorNickName("YogaMaster")
                    .subTutorId(202L)
                    .subTutorNickName("AssistantYogi")
                    .minTuteeNum(5)
                    .maxTuteeNum(20)
                    .klassTime(60)
                    .startTime(KlassStartTimeTypeEnum.A0)
                    .level(KlassLevelTypeEnum.LOW)
                    .colorCodeDto(CommonDto.ColorCodeDto.toDto(KlassColorTypeEnum.LIME))
                    .tuteeCount(15)
                    .privateMemo("Private Memo for Admin and Tutors")
                    .publicMemo("Public Memo for Members")
                    .reservationCount(10)
                    .reservationEnableYn(true)
                    .reservationEnableTime(30)
                    .cancelEnableTime(60)
                    .minTuteeLackCancelTime(10)
                    .waitingTuteeNum(5)
                    .waitingReservationCancelTime(15)
                    .duplicateReservationYn(false)
                    .reservationStartTime("09:00")
                    .checkInEnableBeforeTime(10)
                    .checkInEnableAfterTime(5)
                    .checkOutEnableBeforeTime(15)
                    .checkOutEnableAfterTime(10)
                    .sunOperatingHours(Arrays.asList("09:00-13:00", "14:00-18:00"))
                    .monOperatingHours(Arrays.asList("09:00-13:00", "14:00-18:00"))
                    .tueOperatingHours(Arrays.asList("09:00-13:00", "14:00-18:00"))
                    .wedOperatingHours(Arrays.asList("09:00-13:00", "14:00-18:00"))
                    .thrOperatingHours(Collections.singletonList("09:00-19:00"))
                    .friOperatingHours(Collections.singletonList("09:00-19:00"))
                    .satOperatingHours(Collections.singletonList("08:00-17:00"))
                    .archiveYn(false)
                    .build();
        }

    public static KlassDto.ResponseKlassDto getKlassMockup2 () {
            return KlassDto.ResponseKlassDto.builder()
                    .klassId(1L)
                    .branchId(101L)
                    .klassName("Yoga Class")
                    .mainTutorId(201L)
                    .mainTutorNickName("YogaMaster")
                    .subTutorId(202L)
                    .subTutorNickName("AssistantYogi")
                    .minTuteeNum(5)
                    .maxTuteeNum(20)
                    .klassTime(60)
                    .startTime(KlassStartTimeTypeEnum.A0)
                    .level(KlassLevelTypeEnum.LOW)
                    .colorCodeDto(CommonDto.ColorCodeDto.toDto(KlassColorTypeEnum.SUNSET))
                    .tuteeCount(15)
                    .privateMemo("Private Memo for Admin and Tutors")
                    .publicMemo("Public Memo for Members")
                    .reservationCount(10)
                    .reservationEnableYn(true)
                    .reservationEnableTime(30)
                    .cancelEnableTime(60)
                    .minTuteeLackCancelTime(10)
                    .waitingTuteeNum(5)
                    .waitingReservationCancelTime(15)
                    .duplicateReservationYn(false)
                    .reservationStartTime("09:00")
                    .checkInEnableBeforeTime(10)
                    .checkInEnableAfterTime(5)
                    .checkOutEnableBeforeTime(15)
                    .checkOutEnableAfterTime(10)
                    .sunOperatingHours(Arrays.asList("09:00-13:00", "14:00-18:00"))
                    .monOperatingHours(Arrays.asList("09:00-13:00", "14:00-18:00"))
                    .tueOperatingHours(Arrays.asList("09:00-13:00", "14:00-18:00"))
                    .wedOperatingHours(Arrays.asList("09:00-13:00", "14:00-18:00"))
                    .thrOperatingHours(Collections.singletonList("09:00-19:00"))
                    .friOperatingHours(Collections.singletonList("09:00-19:00"))
                    .satOperatingHours(Collections.singletonList("08:00-17:00"))
                    .archiveYn(false)
                    .build();
        }
    }

