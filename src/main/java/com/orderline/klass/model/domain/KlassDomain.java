package com.orderline.klass.model.domain;

import com.orderline.klass.model.entity.Klass;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;

public class KlassDomain {

    private final Klass klass;

    public KlassDomain(Klass klassInfo){
        this.klass = klassInfo;
    }

    public String mergeTimeRanges(String timeRanges) {
        String[] ranges = timeRanges.split(",");
        if(ranges.length == 1) return timeRanges;
        ArrayList<String[]> times = new ArrayList<>();

        for (String range : ranges) {
            times.add(range.split("~"));
        }

        times.sort(Comparator.comparing(o -> o[0]));

        String currentStart = times.get(0)[0];
        String currentEnd = times.get(0)[1];

        StringBuilder merged = new StringBuilder();

        for (int i = 1; i < times.size(); i++) {
            String start = times.get(i)[0];
            String end = times.get(i)[1];

            if (start.compareTo(currentEnd) <= 0) {
                if (end.compareTo(currentEnd) > 0) {
                    currentEnd = end;
                }
            } else {
                merged.append(currentStart).append("~").append(currentEnd).append(",");
                currentStart = start;
                currentEnd = end;
            }
        }

        merged.append(currentStart).append("~").append(currentEnd);
        return merged.toString();
    }
    public String[] getSelectedDateKlassOperationHours(ZonedDateTime selectDt) {

        String operationHours;
        switch (selectDt.getDayOfWeek()) {
            case MONDAY:
                operationHours = klass.getMonOperatingHours(); break;
            case TUESDAY:
                operationHours = klass.getTueOperatingHours(); break;
            case WEDNESDAY:
                operationHours = klass.getWedOperatingHours(); break;
            case THURSDAY:
                operationHours = klass.getThrOperatingHours(); break;
            case FRIDAY:
                operationHours = klass.getFriOperatingHours(); break;
            case SATURDAY:
                operationHours = klass.getSatOperatingHours(); break;
            default:
                operationHours = klass.getSunOperatingHours(); break;
        }
        // null, 빈문자 처리
        if(operationHours == null || operationHours.trim().isEmpty()){
            throw new IllegalArgumentException("운영시간이 설정되어 있지 않습니다.");
        }
        operationHours = mergeTimeRanges(operationHours);
        // 형식 검증
        String[] operationHoursArray = operationHours.split(",");
        for (String hours : operationHoursArray) {
            if (!hours.matches("\\d{2}:\\d{2}~\\d{2}:\\d{2}")) {
                throw new IllegalArgumentException("운영시간이 잘못 설정되어 않습니다.");
            }
        }
        // operatingHours Example : ["07:00~12:00","13:00~18:00","19:00~24:00"]
        return operationHoursArray;
    }


    public boolean isInOperationHours(ZonedDateTime startDt, ZonedDateTime endDt, String[] operationHours){

        ZonedDateTime adjustedEndDt = endDt;
        if(adjustedEndDt.getHour() == 0 && adjustedEndDt.getMinute() == 0) adjustedEndDt = adjustedEndDt.minusMinutes(1);
        if(startDt.getDayOfMonth() != adjustedEndDt.getDayOfMonth()) return false;

        // operationHours Example : ["09:00~18:00"]
        for (String operatingHour : operationHours) {
            operatingHour = operatingHour.replace("24:00", "23:59");
            String[] operatingHourSplitData = operatingHour.split("~");
            int operatingStartHour = Integer.parseInt(operatingHourSplitData[0].split(":")[0]);
            int operatingStartMinute = Integer.parseInt(operatingHourSplitData[0].split(":")[1]);
            int operatingEndHour = Integer.parseInt(operatingHourSplitData[1].split(":")[0]);
            int operatingEndMinute = Integer.parseInt(operatingHourSplitData[1].split(":")[1]);

            ZonedDateTime operatingStartDt = startDt.withHour(operatingStartHour).withMinute(operatingStartMinute);
            ZonedDateTime operatingEndDt = startDt.withHour(operatingEndHour).withMinute(operatingEndMinute);

            if(!startDt.isBefore(operatingStartDt) && !startDt.isAfter(operatingEndDt)
                    && !adjustedEndDt.isBefore(operatingStartDt) && !adjustedEndDt.isAfter(operatingEndDt))
                return true;
        }
        return false;
    }


}
