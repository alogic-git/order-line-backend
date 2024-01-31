package com.ptglue.klass.model.domain;

import com.ptglue.klass.model.entity.Klass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.time.ZonedDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class KlassDomainTest {

    private Klass klassMock;
    private KlassDomain klassDomain;

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of(ZonedDateTime.parse("2023-12-05T10:00:00Z"), ZonedDateTime.parse("2023-12-05T11:00:00Z"), new String[]{"09:00~18:00"}, true),
                Arguments.of(ZonedDateTime.parse("2023-12-05T17:00:00Z"), ZonedDateTime.parse("2023-12-05T19:00:00Z"), new String[]{"09:00~18:00"}, false),
                Arguments.of(ZonedDateTime.parse("2023-12-05T20:00:00Z"), ZonedDateTime.parse("2023-12-05T21:00:00Z"), new String[]{"09:00~18:00"}, false),
                Arguments.of(ZonedDateTime.parse("2023-12-05T23:00:00Z"), ZonedDateTime.parse("2023-12-06T01:00:00Z"), new String[]{"09:00~18:00"}, false),
                Arguments.of(ZonedDateTime.parse("2023-12-05T23:00:00Z"), ZonedDateTime.parse("2023-12-06T00:00:00Z"), new String[]{"09:00~24:00"}, true),
                Arguments.of(ZonedDateTime.parse("2023-12-05T23:00:00Z"), ZonedDateTime.parse("2023-12-06T01:00:00Z"), new String[]{"09:00~24:00"}, false),
                Arguments.of(ZonedDateTime.parse("2023-12-05T10:00:00Z"), ZonedDateTime.parse("2023-12-05T11:00:00Z"), new String[]{"09:00~18:00", "19:00~24:00"}, true),
                Arguments.of(ZonedDateTime.parse("2023-12-05T17:00:00Z"), ZonedDateTime.parse("2023-12-05T19:00:00Z"), new String[]{"09:00~18:00", "19:00~24:00"}, false),
                Arguments.of(ZonedDateTime.parse("2023-12-05T20:00:00Z"), ZonedDateTime.parse("2023-12-05T21:00:00Z"), new String[]{"09:00~18:00", "19:00~24:00"}, true),
                Arguments.of(ZonedDateTime.parse("2023-12-05T23:00:00Z"), ZonedDateTime.parse("2023-12-06T00:00:00Z"), new String[]{"09:00~18:00", "19:00~24:00"}, true),
                Arguments.of(ZonedDateTime.parse("2023-12-05T23:00:00Z"), ZonedDateTime.parse("2023-12-06T01:00:00Z"), new String[]{"09:00~18:00", "19:00~24:00"}, false)
        );
    }

    private static  Stream<Arguments> provideMergeTimeRangesTestCases() {
        return Stream.of(
                Arguments.of("08:00~10:00,11:00~13:00", "08:00~10:00,11:00~13:00"),
                Arguments.of("09:00~11:00,11:00~13:00", "09:00~13:00"),
                Arguments.of("10:00~12:00,11:00~13:00", "10:00~13:00"),
                Arguments.of("14:00~18:00,15:00~17:00", "14:00~18:00"),
                Arguments.of("04:00~12:00,11:00~19:50,19:00~24:00", "04:00~24:00")
        );
    }

    @BeforeEach
    public void setUp() {
        klassMock = Mockito.mock(Klass.class);
        klassDomain = new KlassDomain(klassMock);
    }

    @Nested
    @DisplayName("getSelectedDateKlassOperationHours TEST")
    class GetSelectedDateKlassOperationHoursTest {
        @Test
        @DisplayName("ok TEST getSelectedDateKlassOperationHours")
        void testGetSelectedDateKlassOperationHours() {
            // given
            ZonedDateTime monday = ZonedDateTime.parse("2023-12-04T10:15:30+01:00[Asia/Seoul]"); // 대체할 날짜
            ZonedDateTime tuesday = ZonedDateTime.parse("2023-12-05T10:15:30+01:00[Asia/Seoul]"); // 대체할 날짜
            ZonedDateTime wednesday = ZonedDateTime.parse("2023-12-06T10:15:30+01:00[Asia/Seoul]"); // 대체할 날짜
            ZonedDateTime thursday = ZonedDateTime.parse("2023-12-07T10:15:30+01:00[Asia/Seoul]"); // 대체할 날짜
            ZonedDateTime friday = ZonedDateTime.parse("2023-12-08T10:15:30+01:00[Asia/Seoul]"); // 대체할 날짜
            ZonedDateTime saturday = ZonedDateTime.parse("2023-12-09T10:15:30+01:00[Asia/Seoul]"); // 대체할 날짜
            ZonedDateTime sunday = ZonedDateTime.parse("2023-12-10T10:15:30+01:00[Asia/Seoul]"); // 대체할 날짜

            // when
            when(klassMock.getMonOperatingHours()).thenReturn("07:00~12:00,13:05~18:00,19:00~24:00");
            when(klassMock.getTueOperatingHours()).thenReturn("01:00~12:00,19:20~24:00");
            when(klassMock.getWedOperatingHours()).thenReturn("00:00~24:00");
            when(klassMock.getThrOperatingHours()).thenReturn("09:00~12:00,13:00~18:30,19:00~17:30");
            when(klassMock.getFriOperatingHours()).thenReturn("00:00~12:00,13:00~18:40,19:00~22:00");
            when(klassMock.getSatOperatingHours()).thenReturn("04:00~12:00,12:00~19:50,19:00~24:00");
            when(klassMock.getSunOperatingHours()).thenReturn("01:00~12:00,13:00~20:10,21:00~23:00");

            String[] operationHoursMonday = klassDomain.getSelectedDateKlassOperationHours(monday);
            String[] operationHoursTuesday = klassDomain.getSelectedDateKlassOperationHours(tuesday);
            String[] operationHoursWednesday = klassDomain.getSelectedDateKlassOperationHours(wednesday);
            String[] operationHoursThursday = klassDomain.getSelectedDateKlassOperationHours(thursday);
            String[] operationHoursFriday = klassDomain.getSelectedDateKlassOperationHours(friday);
            String[] operationHoursSaturday = klassDomain.getSelectedDateKlassOperationHours(saturday);
            String[] operationHoursSunday = klassDomain.getSelectedDateKlassOperationHours(sunday);

            // then
            assertNotNull(operationHoursMonday);
            assertEquals("07:00~12:00", operationHoursMonday[0]);
            assertEquals("13:05~18:00", operationHoursMonday[1]);
            assertEquals("19:00~24:00", operationHoursMonday[2]);

            assertNotNull(operationHoursTuesday);
            assertEquals("01:00~12:00", operationHoursTuesday[0]);
            assertEquals("19:20~24:00", operationHoursTuesday[1]);

            assertNotNull(operationHoursWednesday);
            assertEquals("00:00~24:00", operationHoursWednesday[0]);

            assertNotNull(operationHoursThursday);
            assertEquals("09:00~12:00", operationHoursThursday[0]);
            assertEquals("13:00~18:30", operationHoursThursday[1]);
            assertEquals("19:00~17:30", operationHoursThursday[2]);

            assertNotNull(operationHoursFriday);
            assertEquals("00:00~12:00", operationHoursFriday[0]);
            assertEquals("13:00~18:40", operationHoursFriday[1]);
            assertEquals("19:00~22:00", operationHoursFriday[2]);

            assertNotNull(operationHoursSaturday);
            assertEquals("04:00~24:00", operationHoursSaturday[0]);

            assertNotNull(operationHoursSunday);
            assertEquals("01:00~12:00", operationHoursSunday[0]);
            assertEquals("13:00~20:10", operationHoursSunday[1]);
            assertEquals("21:00~23:00", operationHoursSunday[2]);
        }

        @Test
        @DisplayName("failed TEST getSelectedDateKlassOperationHours")
        void testGetSelectedDateKlassOperationHoursReturnFailed() {
            // given
            ZonedDateTime monday = ZonedDateTime.parse("2023-12-01T10:15:30+01:00[Asia/Seoul]"); // 대체할 날짜

            // when
            when(klassMock.getMonOperatingHours()).thenReturn("07:00~12:00,13:05~18:00,19:00~24:00");
            when(klassMock.getFriOperatingHours()).thenReturn("00:00~12:00,13:00~18:40,19:00~22:00,23:00~24:00");
            String[] operationHoursMonday = klassDomain.getSelectedDateKlassOperationHours(monday);

            // then
            assertNotNull(operationHoursMonday);
            assertNotEquals(3, operationHoursMonday.length);
            assertNotEquals("07:00~12:00", operationHoursMonday[0]);
            assertNotEquals("13:00~18:00", operationHoursMonday[1]);
            assertNotEquals("19:00~24:00", operationHoursMonday[2]);
        }

        @Test
        @DisplayName("null TEST getSelectedDateKlassOperationHours")
        void testGetSelectedDateKlassOperationHoursWithNullFormat() {
            //given
            ZonedDateTime someDay = ZonedDateTime.now();

            //when
            when(klassMock.getMonOperatingHours()).thenReturn(null);
            when(klassMock.getTueOperatingHours()).thenReturn(null);
            when(klassMock.getWedOperatingHours()).thenReturn(null);
            when(klassMock.getThrOperatingHours()).thenReturn(null);
            when(klassMock.getFriOperatingHours()).thenReturn(null);
            when(klassMock.getSatOperatingHours()).thenReturn(null);
            when(klassMock.getSunOperatingHours()).thenReturn(null);
            Exception exception = assertThrows(IllegalArgumentException.class, () -> klassDomain.getSelectedDateKlassOperationHours(someDay));

            //then
            assertNotNull(exception);
            assertEquals("운영시간이 설정되어 있지 않습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("empty TEST getSelectedDateKlassOperationHours")
        void testGetSelectedDateKlassOperationHoursWithEmptyString() {
            //given
            ZonedDateTime someDay = ZonedDateTime.now();

            //when
            when(klassMock.getMonOperatingHours()).thenReturn("");
            when(klassMock.getTueOperatingHours()).thenReturn("");
            when(klassMock.getWedOperatingHours()).thenReturn("");
            when(klassMock.getThrOperatingHours()).thenReturn("");
            when(klassMock.getFriOperatingHours()).thenReturn("");
            when(klassMock.getSatOperatingHours()).thenReturn("");
            when(klassMock.getSunOperatingHours()).thenReturn("");
            Exception exception = assertThrows(IllegalArgumentException.class, () -> klassDomain.getSelectedDateKlassOperationHours(someDay));

            //then
            assertNotNull(exception);
            assertEquals("운영시간이 설정되어 있지 않습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("invalid TEST getSelectedDateKlassOperationHours")
        void testGetSelectedDateKlassOperationHoursWithInvalidString() {
            //given
            ZonedDateTime someDay = ZonedDateTime.now();

            //when
            when(klassMock.getMonOperatingHours()).thenReturn("Invalid value");
            when(klassMock.getTueOperatingHours()).thenReturn("Invalid value");
            when(klassMock.getWedOperatingHours()).thenReturn("Invalid value");
            when(klassMock.getThrOperatingHours()).thenReturn("Invalid value");
            when(klassMock.getFriOperatingHours()).thenReturn("Invalid value");
            when(klassMock.getSatOperatingHours()).thenReturn("Invalid value");
            when(klassMock.getSunOperatingHours()).thenReturn("Invalid value");
            Exception exception = assertThrows(IllegalArgumentException.class, () -> klassDomain.getSelectedDateKlassOperationHours(someDay));

            //then
            assertNotNull(exception);
            assertEquals("운영시간이 잘못 설정되어 않습니다.", exception.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    @DisplayName("isInOperationHours TEST")
    void testIsInOperationHours(ZonedDateTime startDt, ZonedDateTime endDt, String[] operationHours, boolean expected) {
        //given

        //when
        boolean result = klassDomain.isInOperationHours(startDt, endDt, operationHours);

        //then
        assertEquals(expected, result);
    }


        @ParameterizedTest
        @MethodSource("provideMergeTimeRangesTestCases")
        @DisplayName("TEST getAdjustmentStartTime")
        void testMergeTimeRangesWithNonOverlappingRanges(String input, String expected) {
            //given

            //when
            String result = klassDomain.mergeTimeRanges(input);

            //then
            assertEquals(expected, result);
        }

}
