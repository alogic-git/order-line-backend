package com.orderline.payment.service;

import com.orderline.basic.exception.NotFoundException;
import com.orderline.payment.repository.TuteePaymentHistoryRepository;
import com.orderline.ticket.model.dto.TicketTuteeDto;
import com.orderline.ticket.model.entity.Ticket;
import com.orderline.ticket.repository.TicketRepository;
import com.orderline.payment.model.dto.TuteePaymentHistoryDto;
import com.orderline.payment.model.entity.TuteePaymentHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TuteePaymentHistoryService {
    @Resource(name = "tuteePaymentHistoryRepository")
    TuteePaymentHistoryRepository tuteePaymentHistoryRepository;

    @Resource(name = "ticketRepository")
    TicketRepository ticketRepository;

    public TuteePaymentHistoryDto.ResponseTuteePaymentHistoryDto getPayment(Long tuteePaymentHistoryId){

        Optional<TuteePaymentHistory> tuteePaymentHistoryOptional = tuteePaymentHistoryRepository.findById(tuteePaymentHistoryId);
        if(!tuteePaymentHistoryOptional.isPresent()){
            throw new NotFoundException("결제 내역이 존재하지 않습니다.");
        }

        TuteePaymentHistory tuteePaymentHistory = tuteePaymentHistoryOptional.get();

        return TuteePaymentHistoryDto.ResponseTuteePaymentHistoryDto.toDto(tuteePaymentHistory);
    }

    public TicketTuteeDto.ResponseTicketPaymentDto getPaymentByTicketId(Long ticketId){

        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);
        if(!ticketOptional.isPresent()){
            throw new NotFoundException("수강권이 존재하지 않습니다.");
        }

        Ticket ticket = ticketOptional.get();

        List<TuteePaymentHistory> tuteePaymentHistoryList = tuteePaymentHistoryRepository.findByTicketId(ticketId);

        List<TuteePaymentHistoryDto.ResponseTuteePaymentHistoryDto> tuteePaymentHistoryDtoList =
                tuteePaymentHistoryList.stream()
                        .map(TuteePaymentHistoryDto.ResponseTuteePaymentHistoryDto::toDto)
                        .collect(Collectors.toList());

        return TicketTuteeDto.ResponseTicketPaymentDto.toDto(ticket, tuteePaymentHistoryDtoList);
    }

    public Page<TuteePaymentHistoryDto.ResponseTuteePaymentHistoryDto> getPaymentList(Long userId, Pageable pageable){
        List<Ticket> ticketList = ticketRepository.findByUserId(userId);
        List<Long> ticketIds = ticketList.stream()
                .map(Ticket::getId)
                .collect(Collectors.toList());

        if (ticketIds.isEmpty()){
            throw new NotFoundException("해당 사용자는 수강권을 소유하고 있지 않습니다.");
        }

        Page<TuteePaymentHistory> paymentList = tuteePaymentHistoryRepository.findByTicketIdIn(ticketIds, pageable);

        return paymentList.map(TuteePaymentHistoryDto.ResponseTuteePaymentHistoryDto::toDto);
    }
}
