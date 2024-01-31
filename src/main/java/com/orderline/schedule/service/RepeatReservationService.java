package com.orderline.schedule.service;

import com.orderline.basic.exception.NotFoundException;
import com.orderline.basic.utils.TimeFunction;
import com.orderline.branch.enums.StateTypeEnum;
import com.orderline.schedule.enums.RepeatScheduleStatusTypeEnum;
import com.orderline.schedule.model.dto.RepeatReservationDto;
import com.orderline.schedule.model.dto.RepeatScheduleDto;
import com.orderline.schedule.model.entity.RepeatReservation;
import com.orderline.schedule.model.entity.RepeatSchedule;
import com.orderline.schedule.model.entity.Reservation;
import com.orderline.schedule.model.entity.Schedule;
import com.orderline.schedule.repository.RepeatReservationRepository;
import com.orderline.schedule.repository.ReservationRepository;
import com.orderline.schedule.repository.ScheduleRepository;
import com.orderline.ticket.model.entity.Ticket;
import com.orderline.ticket.repository.TicketRepository;
import com.orderline.ticket.service.TicketService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class RepeatReservationService {

    @Resource(name = "repeatReservationRepository")
    RepeatReservationRepository repeatReservationRepository;

    @Resource(name = "reservationRepository")
    ReservationRepository reservationRepository;

    @Resource(name = "scheduleRepository")
    ScheduleRepository scheduleRepository;

    @Resource(name = "ticketRepository")
    TicketRepository ticketRepository;

    @Resource(name = "ticketService")
    private TicketService ticketService;

    @Resource(name = "scheduleService")
    private ScheduleService scheduleService;


    public Page<RepeatReservationDto.ResponseRepeatReservationDto> getRepeatReservationList(Long branchId, Long userId, StateTypeEnum statusType, Pageable pageable) {
        Page<RepeatReservation> repeatReservationPage;
        if (statusType == StateTypeEnum.ONGOING) {
            repeatReservationPage = repeatReservationRepository.findByBranchIdAndTuteeIdAndStatusType(branchId, userId, RepeatScheduleStatusTypeEnum.ONGOING, pageable);
        } else {
            repeatReservationPage = repeatReservationRepository.findByBranchIdAndTuteeId(branchId, userId, pageable);
        }
        return repeatReservationPage.map(RepeatReservationDto.ResponseRepeatReservationDto::toDto);
    }

    public RepeatReservationDto.ResponseRepeatReservationCheckDto check(Long reservationId){
        Optional<Reservation> reservationOptional = reservationRepository.findById(reservationId);
        if (!reservationOptional.isPresent()){
            throw new NotFoundException("예약이 존재하지 않습니다.");
        }
        Reservation reservation = reservationOptional.get();

        Ticket ticket = reservation.getTicket();
        RepeatSchedule repeatSchedule = reservation.getSchedule().getRepeatSchedule();

        Integer expectedUsage = getExpectedUsage(repeatSchedule);

        if (expectedUsage >= ticket.getAvailableReservationCount()){
            return RepeatReservationDto.ResponseRepeatReservationCheckDto.toDto("DISABLE", "해당 수강권은 예약 가능한 횟수를 초과했습니다.");
        }

        if (ticket.getEndDate().isAfter(repeatSchedule.getEndDate())){
            return RepeatReservationDto.ResponseRepeatReservationCheckDto.toDto("CONFIRM", "반복되는 일정의 기간 보다 수강권의 유효 기간이 짧습니다.");
        }

        return RepeatReservationDto.ResponseRepeatReservationCheckDto.toDto("ENABLE", "생성 가능 합니다.");
    }

    @Transactional
    public RepeatReservationDto.ResponseRepeatReservationDto create(Long reservationId){
        Optional<Reservation> reservationOptional = reservationRepository.findById(reservationId);
        if (!reservationOptional.isPresent()){
            throw new NotFoundException("예약이 존재하지 않습니다.");
        }
        Reservation reservation = reservationOptional.get();
        Schedule schedule = reservation.getSchedule();

        RepeatSchedule repeatSchedule = schedule.getRepeatSchedule();

        RepeatReservation repeatReservation = RepeatScheduleDto.RequestRepeatScheduleDto.toRepeatReservationEntity(repeatSchedule, reservation.getTutee());
        repeatReservation = repeatReservationRepository.save(repeatReservation);

        generateReservations(schedule, reservation, repeatReservation);

        return RepeatReservationDto.ResponseRepeatReservationDto.toDto(repeatReservation);
    }

    @Transactional
    public RepeatReservationDto.ResponseRepeatReservationDto update(Long repeatReservationId, RepeatReservationDto.RequestRepeatReservationDto requestRepeatReservationDto){
        Optional<RepeatReservation> repeatReservationOptional = repeatReservationRepository.findById(repeatReservationId);
        if (!repeatReservationOptional.isPresent()){
            throw  new NotFoundException("반복 예약이 존재하지 않습니다.");
        }
        RepeatReservation repeatReservation = repeatReservationOptional.get();

        repeatReservation = requestRepeatReservationDto.toEntity(repeatReservation);
        repeatReservationRepository.save(repeatReservation);

        Optional<Reservation> reservationOptional = reservationRepository.findOneByRepeatReservationId(repeatReservationId);
        if (!reservationOptional.isPresent()){
            throw new NotFoundException("반복예약으로 생성된 예약이 존재하지 않습니다.");
        }

        Reservation reservation = reservationOptional.get();
        generateReservations(reservation.getSchedule(), reservation, repeatReservation);

        return RepeatReservationDto.ResponseRepeatReservationDto.toDto(repeatReservation);
    }

    @Transactional
    public void delete(Long repeatReservationId){
        Optional<RepeatReservation> repeatReservationOptional =  repeatReservationRepository.findById(repeatReservationId);
        if (!repeatReservationOptional.isPresent()){
            throw new NotFoundException("반복일정이 존재하지 않습니다");
        }
        RepeatReservation repeatReservation = repeatReservationOptional.get();

        repeatReservation.deleteRepeatReservation();
        repeatReservationRepository.save(repeatReservation);

        List<Reservation> reservationList = reservationRepository.findByRepeatReservationId(repeatReservationId);
        for (Reservation reservation : reservationList) {
            cancelReservation(reservation);
        }
    }

    public Map<DayOfWeek, List<String>> createDayOfWeekStarEndTimeMap(RepeatReservation repeatReservation){
        EnumMap<DayOfWeek, List<String>> dayOfWeekStarEndTimeMap = new EnumMap<>(DayOfWeek.class);
        dayOfWeekStarEndTimeMap.put(DayOfWeek.SUNDAY, repeatReservation.getSunStartEndTime() == null ? null : Arrays.asList(repeatReservation.getSunStartEndTime().split(",")));
        dayOfWeekStarEndTimeMap.put(DayOfWeek.MONDAY, repeatReservation.getMonStartEndTime() == null ? null : Arrays.asList(repeatReservation.getMonStartEndTime().split(",")));
        dayOfWeekStarEndTimeMap.put(DayOfWeek.TUESDAY, repeatReservation.getTueStartEndTime() == null ? null : Arrays.asList(repeatReservation.getTueStartEndTime().split(",")));
        dayOfWeekStarEndTimeMap.put(DayOfWeek.WEDNESDAY, repeatReservation.getWedStartEndTime() == null ? null : Arrays.asList(repeatReservation.getWedStartEndTime().split(",")));
        dayOfWeekStarEndTimeMap.put(DayOfWeek.THURSDAY, repeatReservation.getThrStartEndTime() == null ? null : Arrays.asList(repeatReservation.getThrStartEndTime().split(",")));
        dayOfWeekStarEndTimeMap.put(DayOfWeek.FRIDAY, repeatReservation.getFriStartEndTime() == null ? null : Arrays.asList(repeatReservation.getFriStartEndTime().split(",")));
        dayOfWeekStarEndTimeMap.put(DayOfWeek.SATURDAY, repeatReservation.getSatStartEndTime() == null ? null : Arrays.asList(repeatReservation.getSatStartEndTime().split(",")));
        return dayOfWeekStarEndTimeMap;
    }

    private Integer getExpectedUsage(RepeatSchedule repeatSchedule){
        List<DayOfWeek> scheduleList = new ArrayList<>();
        Integer expectedUsage = 0;

        if (repeatSchedule.getSunStartEndTime() != null){ scheduleList.add(DayOfWeek.SUNDAY); }
        if (repeatSchedule.getMonStartEndTime() != null){ scheduleList.add(DayOfWeek.MONDAY); }
        if (repeatSchedule.getTueStartEndTime() != null){ scheduleList.add(DayOfWeek.TUESDAY); }
        if (repeatSchedule.getWedStartEndTime() != null){ scheduleList.add(DayOfWeek.WEDNESDAY); }
        if (repeatSchedule.getThrStartEndTime() != null){ scheduleList.add(DayOfWeek.THURSDAY); }
        if (repeatSchedule.getFriStartEndTime() != null){ scheduleList.add(DayOfWeek.FRIDAY); }
        if (repeatSchedule.getSatStartEndTime() != null){ scheduleList.add(DayOfWeek.SATURDAY); }

        for (LocalDate date = repeatSchedule.getStartDate(); date.isBefore(repeatSchedule.getEndDate().plusDays(1)); date = date.plusDays(1)) {
            if (scheduleList.contains(date.getDayOfWeek())) expectedUsage++;
        }

        return expectedUsage;
    }

    private void generateReservations(Schedule schedule, Reservation reservation, RepeatReservation repeatReservation){
//        반복 일정 조회
        RepeatSchedule repeatSchedule = schedule.getRepeatSchedule();

        LocalDate startDate = repeatSchedule.getStartDate();
        LocalDate endDate = repeatSchedule.getEndDate();

        Map<DayOfWeek, List<String>> dayOfWeekStarEndTimeMap = scheduleService.createDayOfWeekStarEndTimeMap(repeatSchedule);
//        회원이 반복예약을 다른 사용자와 다른 방식으로 했을경우 해당 값이 필요 필요
        Map<DayOfWeek, List<String>> dayOfWeekStarEndTimeMapReservation = createDayOfWeekStarEndTimeMap(repeatReservation);

        for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
            List<String> startEndTimeList = dayOfWeekStarEndTimeMap.get(date.getDayOfWeek());
            if (startEndTimeList == null) {
                continue;
            }

            for (String startEnd : startEndTimeList) {
                String[] time = startEnd.split("~");
                ZonedDateTime startDt = TimeFunction.toZonedDateTime(date, time[0]);
                ZonedDateTime endDt = TimeFunction.toZonedDateTime(date, time[1]);

//                시작과 종료일에 지점과 강사가 맞는 일정을 조회
                Optional<Schedule> scheduleOptionalByRepeated = scheduleRepository.findByBranchIdAndMainTutorIdAndStartDtAndEndDt(schedule.getBranch().getId(), schedule.getMainTutor().getId(), startDt, endDt);

                if (!scheduleOptionalByRepeated.isPresent()){
//                예약은 일정에 종속되어 있기 때문에, 그 날의 일정이 등록되어 있어야지만, 예약을 형성할 수 있음
                    throw new NotFoundException("일정을 먼저 등록 해주세요.");
                }
                Schedule scheduleByRepeated = scheduleOptionalByRepeated.get();

//                일정에 맞춰 값이 존재하는지 안하는지 확인 필요
                Optional<Reservation> reservationOptional = reservationRepository.findByScheduleIdAndTuteeId(scheduleByRepeated.getId(), reservation.getTutee().getId());
                if (!reservationOptional.isPresent()){
                    Boolean isCreate = createReservation(reservation, scheduleByRepeated, repeatReservation);
                    if (isCreate){
                        continue;
                    }
                    return;
                }

//                시작과 종료일에 맞는 예약은 존재하지만, 입력한 반복일정에는 값이 존재하지 않을경우 회원이 원하지 않는 예약일정 이므로 삭제한다
                if (dayOfWeekStarEndTimeMapReservation.get(date.getDayOfWeek()) == null){
                    Reservation reservationByRepeated = reservationOptional.get();

                    cancelReservation(reservationByRepeated);
                }
            }
        }
    }

    private Boolean createReservation(Reservation reservation, Schedule schedule, RepeatReservation repeatReservation){
        Reservation reservationByRepeated = reservation.toEntityByRepeated(schedule, reservation.getTicket(), reservation.getTutee(), repeatReservation);
        Ticket ticket = ticketService.decreaseRemainReservationCount(reservationByRepeated);

        if (ticket.getAvailableReservationCount() < 0) {
            return false;
        }
        schedule.increaseCurrentTuteeNum(1);

        reservationRepository.save(reservationByRepeated);
        ticketRepository.save(ticket);
        scheduleRepository.save(schedule);
        return true;
    }

    private void cancelReservation(Reservation reservation){
        Schedule schedule = reservation.getSchedule();

        Ticket ticket = ticketService.increaseAvailableReservationCount(reservation);
        ticketRepository.save(ticket);

        schedule.decreaseCurrentTuteeNum(1);
        scheduleRepository.save(schedule);

        reservation.cancelReservation();
        reservationRepository.save(reservation);
    }
}

