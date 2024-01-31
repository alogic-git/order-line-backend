package com.orderline.schedule.service;

import com.orderline.schedule.enums.ReservationStatusTypeEnum;
import com.orderline.schedule.model.dto.ScheduleTuteeDto;
import com.orderline.schedule.model.entity.Reservation;
import com.orderline.schedule.repository.ReservationRepository;
import com.orderline.schedule.repository.ScheduleRepository;
import com.orderline.ticket.repository.TicketRepository;
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
