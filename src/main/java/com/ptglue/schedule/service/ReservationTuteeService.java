package com.ptglue.schedule.service;

import com.ptglue.schedule.enums.ReservationStatusTypeEnum;
import com.ptglue.schedule.model.dto.ScheduleTuteeDto;
import com.ptglue.schedule.model.entity.Reservation;
import com.ptglue.schedule.repository.ReservationRepository;
import com.ptglue.schedule.repository.ScheduleRepository;
import com.ptglue.ticket.repository.TicketRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ReservationTuteeService {
    @Resource(name = "reservationRepository")
    private ReservationRepository reservationRepository;

    @Resource(name = "scheduleRepository")
    private ScheduleRepository scheduleRepository;

    @Resource(name = "ticketRepository")
    private TicketRepository ticketRepository;

    public Integer getReservationAbsenceCount(Long ticketId, Long userId){
        return reservationRepository.countByTicketIdAndTuteeIdAndStatusType(ticketId, userId, ReservationStatusTypeEnum.ABSENCE);
    }

    public Page<ScheduleTuteeDto.ResponseReservationTuteeByTicketIdDto> getAttendanceStamp(Long userId, Long ticketId, Pageable pageable){
        Page<Reservation> reservationPage = reservationRepository.findByTicketIdAndTuteeId(ticketId, userId, pageable);

        return reservationPage.map(ScheduleTuteeDto.ResponseReservationTuteeByTicketIdDto::toDto);
    }
}
