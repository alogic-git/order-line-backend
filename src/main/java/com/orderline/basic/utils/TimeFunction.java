package com.orderline.basic.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class TimeFunction {
  
    public static Long toUnixTime(ZonedDateTime time){
        return time.toInstant().toEpochMilli();
    }

    public static ZonedDateTime toZonedDateTime(Long unixTime){
        return ZonedDateTime.ofInstant(java.time.Instant.ofEpochMilli(unixTime), java.time.ZoneId.systemDefault());
    }

    public static ZonedDateTime toZonedDateTime(LocalDate date, String time){
        String[] times = time.split(":");
        int hour = Integer.parseInt(times[0]);
        int minute = Integer.parseInt(times[1]);
        LocalDateTime localDateTime =  date.atStartOfDay().plusHours(hour).plusMinutes(minute);
        return localDateTime.atZone(java.time.ZoneId.systemDefault());
    }

    public static Long toUnixTime(LocalDate date, String time){
        String[] times = time.split(":");
        int hour = Integer.parseInt(times[0]);
        int minute = Integer.parseInt(times[1]);
        LocalDateTime localDateTime =  date.atStartOfDay().plusHours(hour).plusMinutes(minute);
        return localDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static ZonedDateTime getNow() {
        return ZonedDateTime.now();
    }

    public static LocalDate getToday() {
        return LocalDate.now();
    }

    public static LocalDate getUnlimitedDate(){
        return LocalDate.of(9999, 12, 31);
    }

    public static Integer getRemainPeriod (LocalDate endDate) {
        return  endDate == null ? 0 : endDate.isEqual(TimeFunction.getUnlimitedDate()) ? 999999 : (int) ChronoUnit.DAYS.between(TimeFunction.getToday(), endDate);
    }

}

