package com.orderline.ticket.service;

import com.orderline.common.user.repository.UserRepository;
import com.orderline.product.model.entity.Product;
import com.orderline.product.model.entity.ProductKlass;
import com.orderline.product.repository.ProductKlassRepository;
import com.orderline.product.repository.ProductRepository;
import com.orderline.basic.exception.NotFoundException;
import com.orderline.branch.model.entity.Branch;
import com.orderline.branch.repository.BranchRepository;
import com.orderline.common.user.model.entity.User;
import com.orderline.schedule.model.entity.Reservation;
import com.orderline.ticket.enums.TicketStatusTypeEnum;

import com.orderline.ticket.model.dto.TicketDto;
import com.orderline.ticket.model.entity.Ticket;
import com.orderline.ticket.repository.TicketPauseHistoryRepository;
import com.orderline.ticket.repository.TicketRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketService {

    @Resource(name = "branchRepository")
    private BranchRepository branchRepository;

    @Resource(name = "productRepository")
    private ProductRepository productRepository;

    @Resource(name="productKlassRepository")
    private ProductKlassRepository productKlassRepository;

    @Resource(name = "ticketRepository")
    private TicketRepository ticketRepository;

    @Resource(name = "userRepository")
    private UserRepository userRepository;

    @Resource(name = "ticketPauseHistoryRepository")
    private TicketPauseHistoryRepository ticketPauseHistoryRepository;

    @Resource(name = "ticketPauseHistoryService")
    private TicketPauseHistoryService ticketPauseHistoryService;

    public TicketDto.ResponseTicketDto get(Long ticketId) {

        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);
        if(!ticketOptional.isPresent()) {
            throw new NotFoundException("티켓이 존재 하지 않습니다.");
        }
        Ticket ticket = ticketOptional.get();
        return TicketDto.ResponseTicketDto.toDto(ticket);
    }

    public Page<TicketDto.ResponseTicketDto> getList(Long branchId, Long userId, Pageable pageable){
        Page<Ticket> ticketList = ticketRepository.findByBranchIdAndUserIdAndArchiveYn(branchId, userId, false, pageable);
        List<ProductKlass> productKlassList = productKlassRepository.findByBranchId(branchId);

        return ticketList.map(ticket -> {
            Integer klassCount = (int) productKlassList.stream().filter(productKlass -> productKlass.getProduct().equals(ticket.getProduct())).count();
            return TicketDto.ResponseTicketDto.toDto(ticket, klassCount);
        });
    }

    @Transactional
    public TicketDto.ResponseTicketDto createTicket(Long branchId, TicketDto.RequestTicketDto requestTicketDto){
        Optional<Branch> branchOptional = branchRepository.findById(branchId);
        if(!branchOptional.isPresent()){
            throw new NotFoundException("지점이 존재하지 않습니다.");
        }
        Branch branch = branchOptional.get();

        Optional<Product> productOptional = productRepository.findById(requestTicketDto.getProductId());
        if(!productOptional.isPresent()){
            throw new NotFoundException("수강권이 존재하지 않습니다.");
        }
        Product product = productOptional.get();

        Optional<User> userOptional = userRepository.findById(requestTicketDto.getUserId());
        if(!userOptional.isPresent()){
            throw new NotFoundException("사용자가 존재하지 않습니다.");
        }
        User user = userOptional.get();

        Ticket ticket = requestTicketDto.toEntity(branch, product, user);
        ticketRepository.save(ticket);

        return TicketDto.ResponseTicketDto.toDto(ticket);
    }

    @Transactional
    public TicketDto.ResponseTicketDto archive(Long ticketId){

        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);
        if(!ticketOptional.isPresent()) {
            throw new NotFoundException("해당 수강권이 존재하지 않습니다.");
        }

        Ticket ticket = ticketOptional.get();

        ticket.archiveTicket();
        ticketRepository.save(ticket);

        return TicketDto.ResponseTicketDto.toDto(ticket);
    }

    @Transactional
    public void pauseTicket(Long branchId, List<LocalDate> extendDateList, Boolean extensionYn) {

        //지점에 status가 before/ongoing/일시정지인 티켓을 가져온다
        List<TicketStatusTypeEnum> ticketStatusTypeEnumList = Arrays.asList(TicketStatusTypeEnum.BEFORE, TicketStatusTypeEnum.ONGOING, TicketStatusTypeEnum.PAUSE);
        List<Ticket> ticketList = ticketRepository.findByBranchIdAndStatusTypeIn(branchId, ticketStatusTypeEnumList);

        //수강권 일시정지 내역 추가
        ticketPauseHistoryService.createTicketPauseHistoryList(ticketList, extendDateList, extensionYn);

        //수강권 연장 활성화한 경우만 휴무날짜만큼 연장
        if (extensionYn) {
            //기간 무제한인 ticket을 제외한 ticketList
            List<Ticket> limitedTicketList = ticketList.stream().filter(ticket -> !ticket.getEndDate().isEqual(LocalDate.of(9999,12,31))).collect(Collectors.toList());
            //ticket 연장
            extendTicket(limitedTicketList, extendDateList);
        }
    }

    public void extendTicket(List<Ticket> ticketList, List<LocalDate> extendDateList){
        List<Ticket> extendTicketList = new ArrayList<>();
        for (Ticket ticket : ticketList) {
            //티켓의 시작일-종료일 사이에 있는 휴무일 수 확인
            Integer offDateNum = extendDateList.stream().filter(date -> !date.isBefore(ticket.getStartDate()) && !date.isAfter(ticket.getEndDate())).collect(Collectors.toList()).size();
            ticket.extendEndDate(offDateNum);
            extendTicketList.add(ticket);
        }
        ticketRepository.saveAll(extendTicketList);
    }

    public Ticket increaseAvailableReservationCount(Reservation reservation){
        Ticket ticket = reservation.getTicket();
        if(ticket.getBranch().equals(reservation.getBranch())){
            ticket.increaseAvailableReservationCount(reservation.getReservationCount());
        } else {
            ticket.increaseAvailableOtherBranchReservationCount(reservation.getReservationCount());
        }
        return ticket;
    }

    public Ticket decreaseRemainReservationCount(Reservation reservation){
        Ticket ticket = reservation.getTicket();
        if(ticket.getBranch().equals(reservation.getBranch())){
            ticket.decreaseRemainReservationCount(reservation.getReservationCount());
        } else {
            ticket.decreaseRemainOtherBranchReservationCount(reservation.getReservationCount());
        }
        return ticket;
    }

}
