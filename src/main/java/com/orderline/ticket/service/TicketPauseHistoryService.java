package com.orderline.ticket.service;

import com.orderline.branch.enums.StateTypeEnum;
import com.orderline.ticket.enums.TicketPauseReasonTypeEnum;
import com.orderline.ticket.enums.TicketStatusTypeEnum;
import com.orderline.ticket.model.dto.TicketPauseHistoryDto;
import com.orderline.ticket.model.entity.Ticket;
import com.orderline.ticket.model.entity.TicketPauseHistory;
import com.orderline.ticket.repository.TicketPauseHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketPauseHistoryService {

    @Resource(name = "ticketPauseHistoryRepository")
    TicketPauseHistoryRepository ticketPauseHistoryRepository;

    public Page<TicketPauseHistoryDto.ResponseTicketPauseHistoryDto> getPauseList(Long ticketId, Pageable pageable){

        Page<TicketPauseHistory> ticketPauseHistoryOptional = ticketPauseHistoryRepository.findByTicketId(ticketId, pageable);

        return ticketPauseHistoryOptional.map(TicketPauseHistoryDto.ResponseTicketPauseHistoryDto::toDto);
    }

    public Page<TicketPauseHistoryDto.ResponseTicketPauseHistoryDto> getList(Long branchId, Long userId, StateTypeEnum statusType, Pageable pageable){
        Page<TicketPauseHistory> ticketPauseHistoryList;
        if (statusType == StateTypeEnum.ONGOING) {
            ticketPauseHistoryList = ticketPauseHistoryRepository.findByBranchIdAndTicket_User_idAndTicket_StatusType(branchId, userId, TicketStatusTypeEnum.PAUSE, pageable);
        } else {
            ticketPauseHistoryList = ticketPauseHistoryRepository.findByBranchIdAndTicket_User_id(branchId, userId, pageable);
        }
        return ticketPauseHistoryList.map(TicketPauseHistoryDto.ResponseTicketPauseHistoryDto::toDto);
    }

    public void createTicketPauseHistoryList(List<Ticket> ticketList, List<LocalDate> extendDateList, Boolean extensionYn){
        List<TicketPauseHistory> ticketPauseHistoryList = new ArrayList<>();
        ticketList.forEach(ticket ->
                extendDateList.forEach(date ->
                        ticketPauseHistoryList.add(TicketPauseHistory.builder()
                                .branch(ticket.getBranch())
                                .product(ticket.getProduct())
                                .ticket(ticket)
                                .startDate(date)
                                .endDate(date)
                                .reasonType(TicketPauseReasonTypeEnum.BRANCH_REQUEST)
                                .extensionYn(extensionYn)
                                .build())
                )
        );
        ticketPauseHistoryRepository.saveAll(ticketPauseHistoryList);
    }
}
