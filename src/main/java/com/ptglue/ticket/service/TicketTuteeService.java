package com.ptglue.ticket.service;

import com.ptglue.ticket.enums.TicketStatusTypeEnum;
import com.ptglue.basic.exception.NotFoundException;
import com.ptglue.ticket.model.dto.TicketTuteeDto;
import com.ptglue.ticket.model.entity.Ticket;
import com.ptglue.ticket.repository.TicketRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class TicketTuteeService {
    @Resource(name = "ticketRepository")
    TicketRepository ticketRepository;

    public TicketTuteeDto.ResponseTicketTuteeDetailDto get(Long ticketId){

        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);
        if(!ticketOptional.isPresent()){
            throw new NotFoundException("수강권이 존재하지 않습니다.");
        }

        Ticket ticket = ticketOptional.get();
        return TicketTuteeDto.ResponseTicketTuteeDetailDto.toDto(ticket);
    }

    public Page<TicketTuteeDto.ResponseTicketTuteeDto> getList(Long userId, TicketStatusTypeEnum statusType, Pageable pageable){

        Page<Ticket> ticketList = null;

        if (statusType == TicketStatusTypeEnum.ALL){
            ticketList = ticketRepository.findByUserId(userId, pageable);
        } else {
            ticketList = ticketRepository.findByUserIdAndStatusType(userId, statusType, pageable);
        }

        return ticketList.map(TicketTuteeDto.ResponseTicketTuteeDto::toDto);
    }
}
