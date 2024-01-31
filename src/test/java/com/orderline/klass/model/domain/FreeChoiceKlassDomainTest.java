package com.orderline.klass.model.domain;


import com.orderline.branch.model.entity.Branch;
import com.orderline.branch.model.entity.BranchUserRole;
import com.orderline.klass.enums.KlassColorTypeEnum;
import com.orderline.klass.enums.KlassLevelTypeEnum;
import com.orderline.klass.enums.KlassStartTimeTypeEnum;
import com.orderline.klass.model.entity.Klass;
import com.orderline.schedule.model.dto.ScheduleDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class FreeChoiceKlassDomainTest {

    private Klass klassMock;
    private FreeChoiceKlassDomain freeChoiceKlassDomain;
    private ZonedDateTime startDtCaseA, startDtCaseB, startDtCaseC, startDtCaseD, startDtCaseE, startDtCaseF;

    private static Stream<Arguments> provideScheduleScenarios() {
        // 숫자 count 값 정확히 확인 해서 입력 필요
        return Stream.of(
                Arguments.of(
                        ZonedDateTime.of(2024, 1, 10, 10, 0, 0, 0, ZoneId.systemDefault()),
                        ZonedDateTime.of(2024, 1, 16, 23, 0, 0, 0, ZoneId.systemDefault()),
                        96, 50, KlassStartTimeTypeEnum.A0),
                Arguments.of(
                        ZonedDateTime.of(2024, 1, 10, 9, 29, 0, 0, ZoneId.systemDefault()),
                        ZonedDateTime.of(2024, 1, 16, 23, 59, 0, 0, ZoneId.systemDefault()),
                        97, 50, KlassStartTimeTypeEnum.A0),
                Arguments.of(
                        ZonedDateTime.of(2024, 1, 10, 10, 0, 0, 0, ZoneId.systemDefault()),
                        ZonedDateTime.of(2024, 1, 16, 23, 0, 0, 0, ZoneId.systemDefault()),
                        98, 30, KlassStartTimeTypeEnum.A30),
                Arguments.of(
                        ZonedDateTime.of(2024, 1, 10, 9, 29, 0, 0, ZoneId.systemDefault()),
                        ZonedDateTime.of(2024, 1, 16, 23, 59, 0, 0, ZoneId.systemDefault()),
                        87, 50, KlassStartTimeTypeEnum.A30),
                Arguments.of(
                        ZonedDateTime.of(2024, 1, 10, 10, 0, 0, 0, ZoneId.systemDefault()),
                        ZonedDateTime.of(2024, 1, 16, 23, 0, 0, 0, ZoneId.systemDefault()),
                        567, 30, KlassStartTimeTypeEnum.E10),
                Arguments.of(
                        ZonedDateTime.of(2024, 1, 10, 0, 4, 0, 0, ZoneId.systemDefault()),
                        ZonedDateTime.of(2024, 1, 16, 23, 0, 0, 0, ZoneId.systemDefault()),
                        626, 30, KlassStartTimeTypeEnum.E10),
                Arguments.of(
                        ZonedDateTime.of(2024, 1, 10, 10, 0, 0, 0, ZoneId.systemDefault()),
                        ZonedDateTime.of(2024, 1, 16, 23, 0, 0, 0, ZoneId.systemDefault()),
                        383, 20, KlassStartTimeTypeEnum.E15),
                Arguments.of(
                        ZonedDateTime.of(2024, 1, 10, 8, 2, 33, 0, ZoneId.systemDefault()),
                        ZonedDateTime.of(2024, 1, 16, 14, 11, 0, 0, ZoneId.systemDefault()),
                        390, 20, KlassStartTimeTypeEnum.E15),
                Arguments.of(
                        ZonedDateTime.of(2024, 1, 10, 10, 0, 0, 0, ZoneId.systemDefault()),
                        ZonedDateTime.of(2024, 1, 16, 23, 0, 0, 0, ZoneId.systemDefault()),
                        295, 20, KlassStartTimeTypeEnum.E20),
                Arguments.of(
                        ZonedDateTime.of(2024, 1, 10, 0, 39, 0, 0, ZoneId.systemDefault()),
                        ZonedDateTime.of(2024, 1, 16, 23, 20, 0, 0, ZoneId.systemDefault()),
                        312, 30, KlassStartTimeTypeEnum.E20),
                Arguments.of(
                        ZonedDateTime.of(2024, 1, 10, 10, 29, 0, 0, ZoneId.systemDefault()),
                        ZonedDateTime.of(2024, 1, 16, 20, 19, 0, 0, ZoneId.systemDefault()),
                        184, 40, KlassStartTimeTypeEnum.E30)
        );
    }

    @BeforeEach
    public void setUp() {
        klassMock = Mockito.mock(Klass.class);
        freeChoiceKlassDomain = new FreeChoiceKlassDomain(klassMock);
        startDtCaseA = ZonedDateTime.of(2023, 3, 10, 15, 7, 0, 0, ZoneId.systemDefault());
        startDtCaseB = ZonedDateTime.of(2023, 3, 10, 15, 17, 0, 0, ZoneId.systemDefault());
        startDtCaseC = ZonedDateTime.of(2023, 3, 10, 15, 27, 0, 0, ZoneId.systemDefault());
        startDtCaseD = ZonedDateTime.of(2023, 3, 10, 15, 37, 0, 0, ZoneId.systemDefault());
        startDtCaseE = ZonedDateTime.of(2023, 3, 10, 15, 47, 0, 0, ZoneId.systemDefault());
        startDtCaseF = ZonedDateTime.of(2023, 3, 10, 16, 0, 0, 0, ZoneId.systemDefault());
    }

    @ParameterizedTest
    @ValueSource(strings = {"A0", "A30", "E10", "E15", "E20", "E30"})
    @DisplayName("ok TEST getAdjustmentStartTime")
    void testGetAdjustmentStartTimeForXxx(String atMinutes) {
        // given
        int expectedHoursResultA, expectedHoursResultB, expectedHoursResultC, expectedHoursResultD, expectedHoursResultE, expectedHoursResultF;
        int expectedMinutesResultA, expectedMinutesResultB, expectedMinutesResultC, expectedMinutesResultD, expectedMinutesResultE, expectedMinutesResultF;
        switch(atMinutes) {
            case "A0":
                expectedHoursResultA = 15; expectedMinutesResultA = 0;
                expectedHoursResultB = 15; expectedMinutesResultB = 0;
                expectedHoursResultC = 15; expectedMinutesResultC = 0;
                expectedHoursResultD = 15; expectedMinutesResultD = 0;
                expectedHoursResultE = 15; expectedMinutesResultE = 0;
                expectedHoursResultF = 16; expectedMinutesResultF = 0;
                break;
            case "A30":
                expectedHoursResultA = 14; expectedMinutesResultA = 30;
                expectedHoursResultB = 14; expectedMinutesResultB = 30;
                expectedHoursResultC = 14; expectedMinutesResultC = 30;
                expectedHoursResultD = 15; expectedMinutesResultD = 30;
                expectedHoursResultE = 15; expectedMinutesResultE = 30;
                expectedHoursResultF = 15; expectedMinutesResultF = 30;
                break;
            case "E10":
                expectedHoursResultA = 15; expectedMinutesResultA = 0;
                expectedHoursResultB = 15; expectedMinutesResultB = 10;
                expectedHoursResultC = 15; expectedMinutesResultC = 20;
                expectedHoursResultD = 15; expectedMinutesResultD = 30;
                expectedHoursResultE = 15; expectedMinutesResultE = 40;
                expectedHoursResultF = 16; expectedMinutesResultF = 0;
                break;
            case "E15":
                expectedHoursResultA = 15; expectedMinutesResultA = 0;
                expectedHoursResultB = 15; expectedMinutesResultB = 15;
                expectedHoursResultC = 15; expectedMinutesResultC = 15;
                expectedHoursResultD = 15; expectedMinutesResultD = 30;
                expectedHoursResultE = 15; expectedMinutesResultE = 45;
                expectedHoursResultF = 16; expectedMinutesResultF = 0;
                break;
            case "E20":
                expectedHoursResultA = 15; expectedMinutesResultA = 0;
                expectedHoursResultB = 15; expectedMinutesResultB = 0;
                expectedHoursResultC = 15; expectedMinutesResultC = 20;
                expectedHoursResultD = 15; expectedMinutesResultD = 20;
                expectedHoursResultE = 15; expectedMinutesResultE = 40;
                expectedHoursResultF = 16; expectedMinutesResultF = 0;
                break;
            case "E30":
                expectedHoursResultA = 15; expectedMinutesResultA = 0;
                expectedHoursResultB = 15; expectedMinutesResultB = 0;
                expectedHoursResultC = 15; expectedMinutesResultC = 0;
                expectedHoursResultD = 15; expectedMinutesResultD = 30;
                expectedHoursResultE = 15; expectedMinutesResultE = 30;
                expectedHoursResultF = 16; expectedMinutesResultF = 0;
                break;
            default: throw new IllegalArgumentException("additionalMinutes 값이 잘못되었습니다.");
        }

        // when
        when(klassMock.getStartTime()).thenReturn(KlassStartTimeTypeEnum.valueOf(atMinutes));

        ZonedDateTime adjustedTimeCaseA = freeChoiceKlassDomain.getAdjustmentStartTime(startDtCaseA);
        ZonedDateTime adjustedTimeCaseB = freeChoiceKlassDomain.getAdjustmentStartTime(startDtCaseB);
        ZonedDateTime adjustedTimeCaseC = freeChoiceKlassDomain.getAdjustmentStartTime(startDtCaseC);
        ZonedDateTime adjustedTimeCaseD = freeChoiceKlassDomain.getAdjustmentStartTime(startDtCaseD);
        ZonedDateTime adjustedTimeCaseE = freeChoiceKlassDomain.getAdjustmentStartTime(startDtCaseE);
        ZonedDateTime adjustedTimeCaseF = freeChoiceKlassDomain.getAdjustmentStartTime(startDtCaseF);

        // then
        assertNotNull(adjustedTimeCaseA);
        assertEquals(expectedHoursResultA, adjustedTimeCaseA.getHour());
        assertEquals(expectedMinutesResultA, adjustedTimeCaseA.getMinute());

        assertNotNull(adjustedTimeCaseB);
        assertEquals(expectedHoursResultB, adjustedTimeCaseB.getHour());
        assertEquals(expectedMinutesResultB, adjustedTimeCaseB.getMinute());

        assertNotNull(adjustedTimeCaseC);
        assertEquals(expectedHoursResultC, adjustedTimeCaseC.getHour());
        assertEquals(expectedMinutesResultC, adjustedTimeCaseC.getMinute());

        assertNotNull(adjustedTimeCaseD);
        assertEquals(expectedHoursResultD, adjustedTimeCaseD.getHour());
        assertEquals(expectedMinutesResultD, adjustedTimeCaseD.getMinute());

        assertNotNull(adjustedTimeCaseE);
        assertEquals(expectedHoursResultE, adjustedTimeCaseE.getHour());
        assertEquals(expectedMinutesResultE, adjustedTimeCaseE.getMinute());

        assertNotNull(adjustedTimeCaseF);
        assertEquals(expectedHoursResultF, adjustedTimeCaseF.getHour());
        assertEquals(expectedMinutesResultF, adjustedTimeCaseF.getMinute());
    }



    @ParameterizedTest
    @ValueSource(strings = {"A0", "A30", "E10", "E15", "E20", "E30"})
    @DisplayName("ok TEST getPreviousScheduleStartDt")
    void testGetPreviousScheduleStartDt(String atMinutes) {
        // given
        int expectedHoursResultA, expectedHoursResultB, expectedHoursResultC, expectedHoursResultD, expectedHoursResultE, expectedHoursResultF;
        int expectedMinutesResultA, expectedMinutesResultB, expectedMinutesResultC, expectedMinutesResultD, expectedMinutesResultE, expectedMinutesResultF;

        // when
        when(klassMock.getStartTime()).thenReturn(KlassStartTimeTypeEnum.valueOf(atMinutes));
        ZonedDateTime previousStartDtCaseA = freeChoiceKlassDomain.getPreviousScheduleStartDt(startDtCaseA);
        ZonedDateTime previousStartDtCaseB = freeChoiceKlassDomain.getPreviousScheduleStartDt(startDtCaseB);
        ZonedDateTime previousStartDtCaseC = freeChoiceKlassDomain.getPreviousScheduleStartDt(startDtCaseC);
        ZonedDateTime previousStartDtCaseD = freeChoiceKlassDomain.getPreviousScheduleStartDt(startDtCaseD);
        ZonedDateTime previousStartDtCaseE = freeChoiceKlassDomain.getPreviousScheduleStartDt(startDtCaseE);
        ZonedDateTime previousStartDtCaseF = freeChoiceKlassDomain.getPreviousScheduleStartDt(startDtCaseF);

        // then
        switch(atMinutes) {
            case "A0":

                expectedHoursResultA = 15; expectedMinutesResultA = 0;
                expectedHoursResultB = 15; expectedMinutesResultB = 0;
                expectedHoursResultC = 15; expectedMinutesResultC = 0;
                expectedHoursResultD = 15; expectedMinutesResultD = 0;
                expectedHoursResultE = 15; expectedMinutesResultE = 0;
                expectedHoursResultF = 15; expectedMinutesResultF = 0;
                break;
            case "A30":
                expectedHoursResultA = 14; expectedMinutesResultA = 30;
                expectedHoursResultB = 14; expectedMinutesResultB = 30;
                expectedHoursResultC = 14; expectedMinutesResultC = 30;
                expectedHoursResultD = 15; expectedMinutesResultD = 30;
                expectedHoursResultE = 15; expectedMinutesResultE = 30;
                expectedHoursResultF = 15; expectedMinutesResultF = 30;
                break;
            case "E10":
                expectedHoursResultA = 15; expectedMinutesResultA = 0;
                expectedHoursResultB = 15; expectedMinutesResultB = 10;
                expectedHoursResultC = 15; expectedMinutesResultC = 20;
                expectedHoursResultD = 15; expectedMinutesResultD = 30;
                expectedHoursResultE = 15; expectedMinutesResultE = 40;
                expectedHoursResultF = 15; expectedMinutesResultF = 50;
                break;
            case "E15":
                expectedHoursResultA = 15; expectedMinutesResultA = 0;
                expectedHoursResultB = 15; expectedMinutesResultB = 15;
                expectedHoursResultC = 15; expectedMinutesResultC = 15;
                expectedHoursResultD = 15; expectedMinutesResultD = 30;
                expectedHoursResultE = 15; expectedMinutesResultE = 45;
                expectedHoursResultF = 15; expectedMinutesResultF = 45;
                break;
            case "E20":
                expectedHoursResultA = 15; expectedMinutesResultA = 0;
                expectedHoursResultB = 15; expectedMinutesResultB = 0;
                expectedHoursResultC = 15; expectedMinutesResultC = 20;
                expectedHoursResultD = 15; expectedMinutesResultD = 20;
                expectedHoursResultE = 15; expectedMinutesResultE = 40;
                expectedHoursResultF = 15; expectedMinutesResultF = 40;
                break;
            case "E30":
                expectedHoursResultA = 15; expectedMinutesResultA = 0;
                expectedHoursResultB = 15; expectedMinutesResultB = 0;
                expectedHoursResultC = 15; expectedMinutesResultC = 0;
                expectedHoursResultD = 15; expectedMinutesResultD = 30;
                expectedHoursResultE = 15; expectedMinutesResultE = 30;
                expectedHoursResultF = 15; expectedMinutesResultF = 30;
                break;
            default: throw new IllegalArgumentException("additionalMinutes 값이 잘못되었습니다.");
        }
        assertNotNull(previousStartDtCaseA);
        assertEquals(ZonedDateTime.of(2023,3,10,expectedHoursResultA,expectedMinutesResultA,0,0, ZoneId.systemDefault()), previousStartDtCaseA);
        assertNotNull(previousStartDtCaseB);
        assertEquals(ZonedDateTime.of(2023,3,10,expectedHoursResultB,expectedMinutesResultB,0,0, ZoneId.systemDefault()), previousStartDtCaseB);
        assertNotNull(previousStartDtCaseC);
        assertEquals(ZonedDateTime.of(2023,3,10,expectedHoursResultC,expectedMinutesResultC,0,0, ZoneId.systemDefault()), previousStartDtCaseC);
        assertNotNull(previousStartDtCaseD);
        assertEquals(ZonedDateTime.of(2023,3,10,expectedHoursResultD,expectedMinutesResultD,0,0, ZoneId.systemDefault()), previousStartDtCaseD);
        assertNotNull(previousStartDtCaseE);
        assertEquals(ZonedDateTime.of(2023,3,10,expectedHoursResultE,expectedMinutesResultE,0,0, ZoneId.systemDefault()), previousStartDtCaseE);
        assertNotNull(previousStartDtCaseF);
        assertEquals(ZonedDateTime.of(2023,3,10,expectedHoursResultF,expectedMinutesResultF,0,0, ZoneId.systemDefault()), previousStartDtCaseF);
    }

    @ParameterizedTest
    @ValueSource(strings = {"A0", "A30", "E10", "E15", "E20", "E30"})
    @DisplayName("ok TEST getNextScheduleStartDt")

    void testGetNextScheduleStartDt(String atMinutes) {
        //given
        int expectedHoursResultA, expectedHoursResultB, expectedHoursResultC, expectedHoursResultD, expectedHoursResultE, expectedHoursResultF;
        int expectedMinutesResultA, expectedMinutesResultB, expectedMinutesResultC, expectedMinutesResultD, expectedMinutesResultE, expectedMinutesResultF;

        //when
        when(klassMock.getStartTime()).thenReturn(KlassStartTimeTypeEnum.valueOf(atMinutes));
        ZonedDateTime nextStartDtCaseA = freeChoiceKlassDomain.getNextScheduleStartDt(startDtCaseA);
        ZonedDateTime nextStartDtCaseB = freeChoiceKlassDomain.getNextScheduleStartDt(startDtCaseB);
        ZonedDateTime nextStartDtCaseC = freeChoiceKlassDomain.getNextScheduleStartDt(startDtCaseC);
        ZonedDateTime nextStartDtCaseD = freeChoiceKlassDomain.getNextScheduleStartDt(startDtCaseD);
        ZonedDateTime nextStartDtCaseE = freeChoiceKlassDomain.getNextScheduleStartDt(startDtCaseE);
        ZonedDateTime nextStartDtCaseF = freeChoiceKlassDomain.getNextScheduleStartDt(startDtCaseF);

        //then

        switch(atMinutes) {
            case "A0":
                expectedHoursResultA = 16; expectedMinutesResultA = 0;
                expectedHoursResultB = 16; expectedMinutesResultB = 0;
                expectedHoursResultC = 16; expectedMinutesResultC = 0;
                expectedHoursResultD = 16; expectedMinutesResultD = 0;
                expectedHoursResultE = 16; expectedMinutesResultE = 0;
                expectedHoursResultF = 17; expectedMinutesResultF = 0;
                break;
            case "A30":
                expectedHoursResultA = 15; expectedMinutesResultA = 30;
                expectedHoursResultB = 15; expectedMinutesResultB = 30;
                expectedHoursResultC = 15; expectedMinutesResultC = 30;
                expectedHoursResultD = 16; expectedMinutesResultD = 30;
                expectedHoursResultE = 16; expectedMinutesResultE = 30;
                expectedHoursResultF = 16; expectedMinutesResultF = 30;
                break;
            case "E10":

                expectedHoursResultA = 15; expectedMinutesResultA = 10;
                expectedHoursResultB = 15; expectedMinutesResultB = 20;
                expectedHoursResultC = 15; expectedMinutesResultC = 30;
                expectedHoursResultD = 15; expectedMinutesResultD = 40;
                expectedHoursResultE = 15; expectedMinutesResultE = 50;
                expectedHoursResultF = 16; expectedMinutesResultF = 10;
                break;
            case "E15":
                expectedHoursResultA = 15; expectedMinutesResultA = 15;
                expectedHoursResultB = 15; expectedMinutesResultB = 30;
                expectedHoursResultC = 15; expectedMinutesResultC = 30;
                expectedHoursResultD = 15; expectedMinutesResultD = 45;
                expectedHoursResultE = 16; expectedMinutesResultE = 0;
                expectedHoursResultF = 16; expectedMinutesResultF = 15;
                break;
            case "E20":

                expectedHoursResultA = 15; expectedMinutesResultA = 20;
                expectedHoursResultB = 15; expectedMinutesResultB = 20;
                expectedHoursResultC = 15; expectedMinutesResultC = 40;
                expectedHoursResultD = 15; expectedMinutesResultD = 40;
                expectedHoursResultE = 16; expectedMinutesResultE = 0;
                expectedHoursResultF = 16; expectedMinutesResultF = 20;
                break;
            case "E30":
                expectedHoursResultA = 15; expectedMinutesResultA = 30;
                expectedHoursResultB = 15; expectedMinutesResultB = 30;
                expectedHoursResultC = 15; expectedMinutesResultC = 30;
                expectedHoursResultD = 16; expectedMinutesResultD = 0;
                expectedHoursResultE = 16; expectedMinutesResultE = 0;
                expectedHoursResultF = 16; expectedMinutesResultF = 30;
                break;
            default: throw new IllegalArgumentException("additionalMinutes 값이 잘못되었습니다.");
        }
        assertNotNull(nextStartDtCaseA);
        assertEquals(ZonedDateTime.of(2023,3,10,expectedHoursResultA,expectedMinutesResultA,0,0, ZoneId.systemDefault()), nextStartDtCaseA);

        assertNotNull(nextStartDtCaseB);
        assertEquals(ZonedDateTime.of(2023,3,10,expectedHoursResultB,expectedMinutesResultB,0,0, ZoneId.systemDefault()), nextStartDtCaseB);

        assertNotNull(nextStartDtCaseC);
        assertEquals(ZonedDateTime.of(2023,3,10,expectedHoursResultC,expectedMinutesResultC,0,0, ZoneId.systemDefault()), nextStartDtCaseC);

        assertNotNull(nextStartDtCaseD);
        assertEquals(ZonedDateTime.of(2023,3,10,expectedHoursResultD,expectedMinutesResultD,0,0, ZoneId.systemDefault()), nextStartDtCaseD);

        assertNotNull(nextStartDtCaseE);
        assertEquals(ZonedDateTime.of(2023,3,10,expectedHoursResultE,expectedMinutesResultE,0,0, ZoneId.systemDefault()), nextStartDtCaseE);

        assertNotNull(nextStartDtCaseF);
        assertEquals(ZonedDateTime.of(2023,3,10,expectedHoursResultF,expectedMinutesResultF,0,0, ZoneId.systemDefault()), nextStartDtCaseF);
    }

    @Test
    @DisplayName("ok TEST getCurrentScheduleEndDt")
    void testGetCurrentScheduleEndDt(){
        //given
        int expectedHoursResultA, expectedHoursResultB, expectedHoursResultC, expectedHoursResultD, expectedHoursResultE, expectedHoursResultF;
        int expectedMinutesResultA, expectedMinutesResultB, expectedMinutesResultC, expectedMinutesResultD, expectedMinutesResultE, expectedMinutesResultF;

        //when
        when(klassMock.getKlassTime()).thenReturn(45);
        ZonedDateTime nextStartDtCaseA = freeChoiceKlassDomain.getCurrentScheduleEndDt(startDtCaseA);
        ZonedDateTime nextStartDtCaseB = freeChoiceKlassDomain.getCurrentScheduleEndDt(startDtCaseB);
        ZonedDateTime nextStartDtCaseC = freeChoiceKlassDomain.getCurrentScheduleEndDt(startDtCaseC);
        ZonedDateTime nextStartDtCaseD = freeChoiceKlassDomain.getCurrentScheduleEndDt(startDtCaseD);
        ZonedDateTime nextStartDtCaseE = freeChoiceKlassDomain.getCurrentScheduleEndDt(startDtCaseE);
        ZonedDateTime nextStartDtCaseF = freeChoiceKlassDomain.getCurrentScheduleEndDt(startDtCaseF);

        //then
        expectedHoursResultA = 15; expectedMinutesResultA = 52;
        expectedHoursResultB = 16; expectedMinutesResultB = 2;
        expectedHoursResultC = 16; expectedMinutesResultC = 12;
        expectedHoursResultD = 16; expectedMinutesResultD = 22;
        expectedHoursResultE = 16; expectedMinutesResultE = 32;
        expectedHoursResultF = 16; expectedMinutesResultF = 45;

        assertNotNull(nextStartDtCaseA);
        assertEquals(ZonedDateTime.of(2023,3,10,expectedHoursResultA,expectedMinutesResultA,0,0, ZoneId.systemDefault()), nextStartDtCaseA);

        assertNotNull(nextStartDtCaseB);
        assertEquals(ZonedDateTime.of(2023,3,10,expectedHoursResultB,expectedMinutesResultB,0,0, ZoneId.systemDefault()), nextStartDtCaseB);

        assertNotNull(nextStartDtCaseC);
        assertEquals(ZonedDateTime.of(2023,3,10,expectedHoursResultC,expectedMinutesResultC,0,0, ZoneId.systemDefault()), nextStartDtCaseC);

        assertNotNull(nextStartDtCaseD);
        assertEquals(ZonedDateTime.of(2023,3,10,expectedHoursResultD,expectedMinutesResultD,0,0, ZoneId.systemDefault()), nextStartDtCaseD);

        assertNotNull(nextStartDtCaseE);
        assertEquals(ZonedDateTime.of(2023,3,10,expectedHoursResultE,expectedMinutesResultE,0,0, ZoneId.systemDefault()), nextStartDtCaseE);

        assertNotNull(nextStartDtCaseF);
        assertEquals(ZonedDateTime.of(2023,3,10,expectedHoursResultF,expectedMinutesResultF,0,0, ZoneId.systemDefault()), nextStartDtCaseF);
    }

    @ParameterizedTest
    @MethodSource("provideScheduleScenarios")
    @DisplayName("ok TEST getKlassFreeChoiceSchedule")
    void testGetKlassFreeChoiceSchedule(ZonedDateTime startDt, ZonedDateTime endDt, int expectedScheduleCount, int klassTime, KlassStartTimeTypeEnum startTimeType) {
        // given

        //when
        when(klassMock.getId()).thenReturn(1L);
        when(klassMock.getBranch()).thenReturn(Branch.builder().id(1342L).branchName("테스트지점").build());
        when(klassMock.getMainTutor()).thenReturn(BranchUserRole.builder().id(1L).build());
        when(klassMock.getKlassName()).thenReturn("테스트 클래스");
        when(klassMock.getMinTuteeNum()).thenReturn(3);
        when(klassMock.getMaxTuteeNum()).thenReturn(10);
        when(klassMock.getLevel()).thenReturn(KlassLevelTypeEnum.HIGH);
        when(klassMock.getColorCdType()).thenReturn(KlassColorTypeEnum.BLUEBERRY);
        when(klassMock.getReservationEnableTime()).thenReturn(30);
        when(klassMock.getCancelEnableTime()).thenReturn(30);
        when(klassMock.getMinTuteeLackCancelTime()).thenReturn(30);

        when(klassMock.getReservationEnableYn()).thenReturn(true);
        when(klassMock.getKlassTime()).thenReturn(klassTime);
        when(klassMock.getStartTime()).thenReturn(startTimeType);
        when(klassMock.getMonOperatingHours()).thenReturn("07:00~12:00,13:05~18:00,19:00~24:00");
        when(klassMock.getTueOperatingHours()).thenReturn("00:00~00:00");
        when(klassMock.getWedOperatingHours()).thenReturn("00:00~24:00");
        when(klassMock.getThrOperatingHours()).thenReturn("09:00~12:00,13:00~18:30,19:00~20:30");
        when(klassMock.getFriOperatingHours()).thenReturn("00:00~12:00,13:00~18:40,19:00~22:00");
        when(klassMock.getSatOperatingHours()).thenReturn("04:00~12:00,12:00~19:50,19:50~24:00");
        when(klassMock.getSunOperatingHours()).thenReturn("01:00~12:00,13:00~20:10,21:00~23:00");
        List<ScheduleDto.ResponseScheduleDto> schedules = freeChoiceKlassDomain.getKlassFreeChoiceSchedule(startDt, endDt);
        System.out.println("start: " + startDt + "  end: " + endDt);
        System.out.println("startDays:" + startDt.getDayOfWeek() + "  endDays: " + endDt.getDayOfWeek());
        System.out.println("start Time Type : " + startTimeType + "  klassTime: " + klassTime);
        for(ScheduleDto.ResponseScheduleDto schedule: schedules) {
            System.out.println("=====================================");
            System.out.println(ZonedDateTime.ofInstant(java.time.Instant.ofEpochMilli(schedule.getStartDt()), java.time.ZoneId.systemDefault()));
            System.out.println(ZonedDateTime.ofInstant(java.time.Instant.ofEpochMilli(schedule.getEndDt()), java.time.ZoneId.systemDefault()));
            System.out.println("=====================================");
        }

        // then
        assertNotNull(schedules);
        assertEquals(expectedScheduleCount, schedules.size());

    }

    @Test
    @DisplayName("ReservationEnableYnFalse TEST testGetKlassFreeChoiceSchedule")
    void testGetKlassFreeChoiceScheduleReservationEnableYnFalse() {
        // given

        //when
        when(klassMock.getId()).thenReturn(1L);
        when(klassMock.getBranch()).thenReturn(Branch.builder().id(1342L).branchName("테스트지점").build());
        when(klassMock.getMainTutor()).thenReturn(BranchUserRole.builder().id(1L).build());
        when(klassMock.getKlassName()).thenReturn("테스트 클래스");
        when(klassMock.getMinTuteeNum()).thenReturn(3);
        when(klassMock.getMaxTuteeNum()).thenReturn(10);
        when(klassMock.getLevel()).thenReturn(KlassLevelTypeEnum.HIGH);
        when(klassMock.getColorCdType()).thenReturn(KlassColorTypeEnum.BLUEBERRY);
        when(klassMock.getReservationEnableTime()).thenReturn(30);
        when(klassMock.getCancelEnableTime()).thenReturn(30);
        when(klassMock.getMinTuteeLackCancelTime()).thenReturn(30);

        when(klassMock.getReservationEnableYn()).thenReturn(false);
        when(klassMock.getKlassTime()).thenReturn(30);
        when(klassMock.getStartTime()).thenReturn(KlassStartTimeTypeEnum.E15);
        when(klassMock.getMonOperatingHours()).thenReturn("07:00~12:00,13:05~18:00,19:00~24:00");
        when(klassMock.getTueOperatingHours()).thenReturn("00:00~00:00");
        when(klassMock.getWedOperatingHours()).thenReturn("00:00~24:00");
        when(klassMock.getThrOperatingHours()).thenReturn("09:00~12:00,13:00~18:30,19:00~17:30");
        when(klassMock.getFriOperatingHours()).thenReturn("00:00~12:00,13:00~18:40,19:00~22:00");
        when(klassMock.getSatOperatingHours()).thenReturn("04:00~12:00,12:00~19:50,19:50~24:00");
        when(klassMock.getSunOperatingHours()).thenReturn("01:00~12:00,13:00~20:10,21:00~23:00");
        List<ScheduleDto.ResponseScheduleDto> schedules = freeChoiceKlassDomain.getKlassFreeChoiceSchedule(
                ZonedDateTime.of(2024, 1, 10, 10, 0, 0, 0, ZoneId.systemDefault()),
                ZonedDateTime.of(2024, 1, 12, 23, 0, 0, 0, ZoneId.systemDefault()));

        // then
        assertNotNull(schedules);
        assertEquals(0, schedules.size());
    }

}
