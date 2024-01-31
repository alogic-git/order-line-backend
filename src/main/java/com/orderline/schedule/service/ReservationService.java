package com.orderline.schedule.service;

import com.orderline.basic.exception.NotFoundException;
import com.orderline.branch.model.entity.Branch;
import com.orderline.branch.model.entity.BranchUserRole;
import com.orderline.branch.repository.BranchRepository;
import com.orderline.branch.repository.BranchUserRoleRepository;
import com.orderline.common.user.enums.UserRoleEnum;
import com.orderline.common.user.model.entity.User;
import com.orderline.common.user.repository.UserRepository;
import com.orderline.klass.repository.KlassRepository;
import com.orderline.schedule.enums.ReservationStatusTypeEnum;
import com.orderline.schedule.model.dto.ReservationDto;
import com.orderline.schedule.model.entity.RepeatReservation;
import com.orderline.schedule.model.entity.Reservation;
import com.orderline.schedule.model.entity.Schedule;
import com.orderline.klass.model.entity.Klass;
import com.orderline.schedule.repository.RepeatReservationRepository;
import com.orderline.schedule.repository.ReservationRepository;
import com.orderline.schedule.repository.ScheduleRepository;
import com.orderline.ticket.model.dto.TicketDto;
import com.orderline.ticket.model.entity.Ticket;
import com.orderline.ticket.repository.TicketRepository;
import com.orderline.ticket.service.TicketService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Resource;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    @Resource(name = "scheduleRepository")
    ScheduleRepository scheduleRepository;

    @Resource(name = "branchRepository")
    BranchRepository branchRepository;

    @Resource(name = "klassRepository")
    KlassRepository klassRepository;

    @Resource(name = "userRepository")
    UserRepository userRepository;

    @Resource(name = "reservationRepository")
    ReservationRepository reservationRepository;

    @Resource(name = "ticketRepository")
    TicketRepository ticketRepository;

    @Resource(name = "branchUserRoleRepository")
    BranchUserRoleRepository branchUserRoleRepository;

    @Resource(name = "repeatReservationRepository")
    private RepeatReservationRepository repeatReservationRepository;

    @Resource(name = "ticketService")
    private TicketService ticketService;

    public ReservationDto.ResponseReservationDto get(Long reservationId){

        Optional<Reservation> reservationOptional = reservationRepository.findById(reservationId);
        if(!reservationOptional.isPresent()){
            throw  new NotFoundException("예약이 존재하지 않습니다.");
        }

        Reservation reservation = reservationOptional.get();
        return ReservationDto.ResponseReservationDto.toDto(reservation);
    }

    public Page<ReservationDto.ResponseReservationDto> getList(Long userId, Pageable pageable){

        Page<Reservation> reservations = reservationRepository.findByTuteeId(userId, pageable);
        return reservations.map(ReservationDto.ResponseReservationDto::toDto);
    }

    @Transactional
    public ReservationDto.ResponseReservationDto create(Long branchId, ReservationDto.RequestReservationDto requestReservationDto){
        Optional<Branch> branchOptional = branchRepository.findById(branchId);
        if (!branchOptional.isPresent()){
            throw new NotFoundException("지점이 존재하지 않습니다.");
        }
        Branch branch = branchOptional.get();

        Optional<Klass> klassOptional = klassRepository.findById(requestReservationDto.getKlassId());
        if (!klassOptional.isPresent()){
            throw new NotFoundException("클래스가 존재하지 않습니다.");
        }
        Klass klass = klassOptional.get();

        Optional<Schedule> scheduleOptional = scheduleRepository.findById(requestReservationDto.getScheduleId());
        if (!scheduleOptional.isPresent()){
            throw new NotFoundException("일정이 존재하지 않습니다.");
        }
        Schedule schedule = scheduleOptional.get();

        Optional<Ticket> ticketOptional = ticketRepository.findById(requestReservationDto.getTicketId());
        if (!ticketOptional.isPresent()){
            throw new NotFoundException("수강권이 존재하지 않습니다.");
        }
        Ticket ticket = ticketOptional.get();

        Optional<User> tuteeOptional = userRepository.findById(requestReservationDto.getTuteeId());
        if (!tuteeOptional.isPresent()){
            throw new NotFoundException("회원값이 정확하지 않습니다.");
        }
        User tutee = tuteeOptional.get();

        Reservation reservation = requestReservationDto.toEntity(branch, klass, schedule, ticket, tutee);

        ticket = ticketService.decreaseRemainReservationCount(reservation);
        if (ticket.getAvailableReservationCount() < 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수강권의 수강가능한 횟수가 부족합니다.");
        }
        schedule.increaseCurrentTuteeNum(1);

        reservationRepository.save(reservation);
        ticketRepository.save(ticket);
        scheduleRepository.save(schedule);

        return ReservationDto.ResponseReservationDto.toDto(reservation);
    }

    @Transactional
    public ReservationDto.ResponseReservationDto cancel(Long reservationId){

        Optional<Reservation> reservationOptional = reservationRepository.findById(reservationId);
        if (!reservationOptional.isPresent()){
            throw new NotFoundException("예약이 없습니다.");
        }

        Reservation reservation = reservationOptional.get();

        reservation.cancelReservation();
        reservationRepository.save(reservation);

        Ticket ticket = ticketService.increaseAvailableReservationCount(reservation);
        ticketRepository.save(ticket);

        Schedule schedule = reservation.getSchedule();
        schedule.decreaseCurrentTuteeNum(1);
        scheduleRepository.save(schedule);

        return ReservationDto.ResponseReservationDto.toDto(reservation);
    }

    @Transactional
    public ReservationDto.ResponseReservationDto update(Long reservationId, ReservationDto.RequestReservationDto requestReservationDto){

        Optional<Reservation> reservationOptional = reservationRepository.findById(reservationId);
        if (!reservationOptional.isPresent()){
            throw new NotFoundException("예약이 존재하지 않습니다.");
        }
        Reservation reservation = reservationOptional.get();

        Optional<Ticket> ticketOptional = ticketRepository.findById(requestReservationDto.getTicketId());
        if (!ticketOptional.isPresent()){
            throw new NotFoundException("수강권이 존재하지 않습니다.");
        }
        Ticket ticket = ticketOptional.get();

        Optional<User> tuteeOptional = userRepository.findById(requestReservationDto.getTuteeId());
        if (!tuteeOptional.isPresent()){
            throw new NotFoundException("회원값이 정확하지 않습니다.");
        }
        User tutee = tuteeOptional.get();

        reservation.updateReservation(requestReservationDto, ticket, tutee);
        reservationRepository.save(reservation);

        return ReservationDto.ResponseReservationDto.toDto(reservation);
    }

    public List<ReservationDto.ResponseAttendanceReservationDto> getAttendanceReservationList(Long branchId, String phone){

        //branchId, Phone, reservation의 status가 예약확정인 오늘 경우만 예약 목록 조회
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime todayStartDt = ZonedDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 0, 0, 0, 0, now.getZone());
        ZonedDateTime todayEndDt = ZonedDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 23, 59, 59, 999, now.getZone());
        List<Reservation> reservations = reservationRepository.findTodayReservation(branchId, phone, ReservationStatusTypeEnum.CONFIRMATION, todayStartDt, todayEndDt);
        //klass 체크인 가능 시간 확인 (스케쥴 시작 시간이 체크인 가능 시간 사이에 있는 경우)
        List<Reservation> checkInEnableReservations = new ArrayList<>();
        if (!reservations.isEmpty()){
            reservations.forEach(reservation -> {
                Integer checkInBefore = reservation.getKlass().getCheckInEnableBeforeTime();
                Integer checkInAfter = reservation.getKlass().getCheckInEnableAfterTime();
                ZonedDateTime startTime = reservation.getSchedule().getStartDt();
                if(!startTime.isBefore(now.minusMinutes(checkInBefore)) && !startTime.isAfter(now.plusMinutes(checkInAfter))) {
                    checkInEnableReservations.add(reservation);
                }
            });
        }

        return checkInEnableReservations.stream().map(ReservationDto.ResponseAttendanceReservationDto::toDto).collect(Collectors.toList());
    }

    public TicketDto.ResponseTicketDto getAttendanceReservationTicket(Long reservationId){

        Optional<Reservation> reservationOptional = reservationRepository.findById(reservationId);
        if (!reservationOptional.isPresent()){
            throw new NotFoundException("예약이 존재하지 않습니다.");
        }
        Reservation reservation = reservationOptional.get();
        return TicketDto.ResponseTicketDto.toDto(reservation.getTicket());
    }

    public ReservationDto.ResponseAttendanceReservationDto updateAttendanceReservation(Long reservationId){

        Optional<Reservation> reservationOptional = reservationRepository.findById(reservationId);
        if (!reservationOptional.isPresent()){
            throw new NotFoundException("예약이 존재하지 않습니다.");
        }
        Reservation reservation = reservationOptional.get();

        reservation.updateStatus(ReservationStatusTypeEnum.ATTENDANCE);
        reservation.updateAttendanceDt(ZonedDateTime.now());
        reservationRepository.save(reservation);
        return ReservationDto.ResponseAttendanceReservationDto.toDto(reservation);
    }

    public Page<ReservationDto.ResponseTuteeReservationDto> getTuteeReservationList(Long branchId, Long tuteeId, Pageable pageable){

        Page<Reservation> reservationPage = reservationRepository.findByBranchIdAndTuteeId(branchId, tuteeId, pageable);

        return reservationPage.map(ReservationDto.ResponseTuteeReservationDto::toDto);
    }

    public Page<ReservationDto.ResponseReservationTuteeDto> getReservationListByScheduleId(Long scheduleId, Pageable pageable) {
        Page<Reservation> reservationPage = reservationRepository.findByScheduleId(scheduleId, pageable);

        return reservationPage.map(reservation -> {
            Optional<BranchUserRole> branchUserRoleOptional = branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(reservation.getBranch().getId(), reservation.getTutee().getId(), UserRoleEnum.TUTEE);

            if (!branchUserRoleOptional.isPresent()) {
                branchUserRoleOptional = branchUserRoleRepository.findByBranchIdAndUserIdAndRoleType(reservation.getTicket().getBranch().getId(), reservation.getTutee().getId(), UserRoleEnum.TUTEE);
            }

            BranchUserRole branchUserRole = branchUserRoleOptional.get();
            return ReservationDto.ResponseReservationTuteeDto.toDto(reservation, branchUserRole.getConnectionType());
        });
    }
  
    @Transactional
    public void updateReservationStatus(Long scheduleId, ReservationDto.RequestUpdateReservationAttendanceStatusDto requestReservationAttendanceDto){

        Optional<Schedule> scheduleOptional = scheduleRepository.findById(scheduleId);
        if (!scheduleOptional.isPresent()){
            throw new NotFoundException("일정이 존재하지 않습니다.");
        }
        Schedule schedule = scheduleOptional.get();

        List<Reservation> reservationList = reservationRepository.findByScheduleIdAndIdIn(scheduleId, requestReservationAttendanceDto.getReservationIdList());
        List<Reservation> updateReservationList = new ArrayList<>();
        List<Schedule> updateScheduleList = new ArrayList<>();
        List<Ticket> updateTicketList = new ArrayList<>();
        reservationList.forEach(updateReservation -> {
            updateReservation.updateReservationStatus(requestReservationAttendanceDto.getStatusType());
            Schedule updateSchedule = updateReservation.getSchedule();
            Ticket updateTicket = null;
            //출석, 결석으로 변경하는 경우
            if(requestReservationAttendanceDto.getStatusType() == ReservationStatusTypeEnum.ATTENDANCE || requestReservationAttendanceDto.getStatusType() == ReservationStatusTypeEnum.ABSENCE){
                //출석인 경우는 출석 처리한 날짜 추가
                if (requestReservationAttendanceDto.getStatusType() == ReservationStatusTypeEnum.ATTENDANCE){
                    updateReservation.updateAttendanceDt(ZonedDateTime.now());
                }
                //수강권 잔여 횟수 감소
                updateTicket = ticketService.decreaseRemainReservationCount(updateReservation);

            //취소로 변경하는 경우
            } else if (requestReservationAttendanceDto.getStatusType() == ReservationStatusTypeEnum.CANCELLATION){

                //예약 확정 -> 취소인 경우, 일정 - currentTuteeNum 감소, 수강권 예약 가능 횟수 증가
                if (updateReservation.getStatusType() == ReservationStatusTypeEnum.CONFIRMATION){
                    updateReservation.cancelReservation();
                    updateSchedule.decreaseCurrentTuteeNum(updateReservation.getReservationCount());
                    updateTicket = ticketService.increaseAvailableReservationCount(updateReservation);
                //대기 예약 -> 취소인 경우, 일정 - waitTuteeNum 감소, 수강권 예약 가능 횟수 증가
                } else if (updateReservation.getStatusType() == ReservationStatusTypeEnum.WAIT){
                    updateReservation.cancelReservation();
                    updateSchedule.decreaseWaitingCurrentTuteeNum(updateReservation.getReservationCount());
                    updateTicket = ticketService.increaseAvailableReservationCount(updateReservation);
                }

            //예약 확정으로 변경하는 경우
            } else if (requestReservationAttendanceDto.getStatusType() == ReservationStatusTypeEnum.CONFIRMATION){
                updateSchedule.decreaseWaitingCurrentTuteeNum(updateReservation.getReservationCount());
                updateSchedule.increaseCurrentTuteeNum(updateReservation.getReservationCount());
            //대기 예약으로 변경하는 경우
            } else if (requestReservationAttendanceDto.getStatusType() == ReservationStatusTypeEnum.WAIT){
                updateSchedule.increaseWaitingCurrentTuteeNum(updateReservation.getReservationCount());
                updateSchedule.decreaseCurrentTuteeNum(updateReservation.getReservationCount());
            }

            //변경한 수강권,일정,예약 list에 추가
            if (updateTicket !=null) {
                updateTicketList.add(updateTicket);
            }
            updateScheduleList.add(updateSchedule);
            updateReservationList.add(updateReservation);
            //알림 추가
        });

        if(!updateTicketList.isEmpty()){
            ticketRepository.saveAll(updateTicketList);
        }
        scheduleRepository.saveAll(updateScheduleList);
        reservationRepository.saveAll(updateReservationList);
    }

    @Transactional
    public void deleteRepeatReservation(Long repeatReservationId){

        //repeatReservationId로 reservation 에서 예약 취소
        //reservation의 statusType이 예약확정, 대기인 경우만 일정 삭제, 이미 결석/출석/취소인 경우는 일정 삭제 안함
        List<ReservationStatusTypeEnum> statusTypeList = Arrays.asList(ReservationStatusTypeEnum.CONFIRMATION, ReservationStatusTypeEnum.WAIT);
        List<Reservation> reservationList = reservationRepository.findByRepeatReservationIdAndStatusTypeIn(repeatReservationId, statusTypeList);
        List<Schedule> scheduleList = new ArrayList<>();
        List<Ticket> ticketList = new ArrayList<>();

        reservationList.forEach(reservation -> {

            //reservation list의 schedueId로 schedule의 current tutee num 을 tuteeNum 만큼 감소,
            Schedule schedule = reservation.getSchedule();
            schedule.decreaseCurrentTuteeNum(reservation.getTuteeNum());
            scheduleList.add(schedule);

            //ticket도 reservationCount 만큼 다시 증가
            Ticket ticket = ticketService.increaseAvailableReservationCount(reservation);
            ticketList.add(ticket);

            //reservation 취소
            reservation.cancelReservation();
            reservation.updateStatus(ReservationStatusTypeEnum.CANCELLATION);
        });

        scheduleRepository.saveAll(scheduleList);
        ticketRepository.saveAll(ticketList);
        reservationRepository.saveAll(reservationList);

        //repeatReservationId로 repeatReservation 삭제
        Optional<RepeatReservation> repeatReservationOptional = repeatReservationRepository.findById(repeatReservationId);
        if(!repeatReservationOptional.isPresent()){
            throw new NotFoundException("반복예약이 존재하지 않습니다.");
        }
        RepeatReservation repeatReservation = repeatReservationOptional.get();
        repeatReservation.deleteRepeatReservation();
        repeatReservationRepository.save(repeatReservation);
    }
}
