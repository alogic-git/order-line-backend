package com.orderline.klass.model.domain;

import com.orderline.klass.enums.KlassStartTimeTypeEnum;
import com.orderline.klass.model.entity.Klass;
import com.orderline.schedule.model.dto.ScheduleDto;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class FreeChoiceKlassDomain extends KlassDomain{

    private final Klass klass;

    public FreeChoiceKlassDomain(Klass klassInfo){
        super(klassInfo);
        this.klass = klassInfo;
    }

    // 만약 10:15분 이고 A30인 경우 09:30 으로 변경
    public ZonedDateTime getAdjustmentStartTime(ZonedDateTime startDt){
        ZonedDateTime adjustedStartDt = startDt.withMinute(startDt.getMinute() - startDt.getMinute() % klass.getStartTime().getAtMinutes());

        if(klass.getStartTime().equals(KlassStartTimeTypeEnum.A30) && adjustedStartDt.getMinute() != 30) {
            adjustedStartDt = adjustedStartDt.minusMinutes(30);
        }
        return adjustedStartDt;
    }

    public ZonedDateTime getPreviousScheduleStartDt(ZonedDateTime startDt){
        ZonedDateTime adjustedStartDt = startDt.minusMinutes(1);
        return this.getAdjustmentStartTime(adjustedStartDt);
    }

    public ZonedDateTime getNextScheduleStartDt(ZonedDateTime startDt){
        ZonedDateTime nextStartDt = startDt.plusMinutes(klass.getStartTime().getAdditionalMinutes());
        return this.getAdjustmentStartTime(nextStartDt);
    }

    public ZonedDateTime getCurrentScheduleEndDt(ZonedDateTime startDt){
        return startDt.plusMinutes(klass.getKlassTime());
    }

    // 테스팅 가능한 코드로 조금 더 개선 필요
    public List<ScheduleDto.ResponseScheduleDto> getKlassFreeChoiceSchedule(ZonedDateTime startDt, ZonedDateTime endDt) {
        List<ScheduleDto.ResponseScheduleDto> klassEnableScheduleList = new ArrayList<>();
        if(Boolean.FALSE.equals(klass.getReservationEnableYn())) return klassEnableScheduleList;

        ZonedDateTime referenceStartDt = startDt.plusMinutes(klass.getReservationEnableTime());
        ZonedDateTime referenceEndDt = endDt.minusMinutes(klass.getKlassTime());

        ZonedDateTime klassStartDt = this.getPreviousScheduleStartDt(referenceStartDt);
        ZonedDateTime klassEndDt;

        // 시작 시각 부터 최종 종료일 까지의 스케쥴 생성
        while (!this.getNextScheduleStartDt(klassStartDt).isAfter(referenceEndDt)) {

            klassStartDt = this.getNextScheduleStartDt(klassStartDt);
            klassEndDt = this.getCurrentScheduleEndDt(klassStartDt);
            String[] operationHours = this.getSelectedDateKlassOperationHours(klassStartDt);
            if(this.isInOperationHours(klassStartDt, klassEndDt, operationHours)) {
                klassEnableScheduleList.add(ScheduleDto.ResponseScheduleDto.toDto(klass, klassStartDt, klassEndDt));
            }
        }
        return klassEnableScheduleList;
    }
}
