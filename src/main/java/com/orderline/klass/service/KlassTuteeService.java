package com.orderline.klass.service;

import com.orderline.basic.exception.NotFoundException;
import com.orderline.klass.model.dto.KlassTuteeDto;
import com.orderline.klass.model.entity.Klass;
import com.orderline.klass.repository.KlassRepository;
import com.orderline.product.model.entity.ProductKlass;
import com.orderline.product.repository.ProductKlassRepository;
import com.orderline.ticket.model.entity.Ticket;
import com.orderline.ticket.repository.TicketRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KlassTuteeService {
    @Resource(name = "ticketRepository")
    TicketRepository ticketRepository;

    @Resource(name = "klassRepository")
    KlassRepository klassRepository;

    @Resource(name = "productKlassRepository")
    ProductKlassRepository productKlassRepository;

    public Page<KlassTuteeDto.ResponseTuteeKlassByTicketIdDto> getKlassList(Long ticketId, Pageable pageable){

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("수강권이 존재하지 않습니다."));

        Long productId = ticket.getProduct().getId();

        List<ProductKlass> productKlassList = productKlassRepository.findByProductId(productId);
        if (productKlassList.isEmpty()){
            throw new NotFoundException("수강권에 유효한 강의가 현재 존재하지 않습니다.");
        }

        List<Long> klassIds = productKlassList
                .stream()
                .map(productKlass -> productKlass.getKlass().getId())
                .distinct()
                .collect(Collectors.toList());
        Page<Klass> klassPage = klassRepository.findByIdInAndReservationEnableYn(klassIds, true, pageable);

        return klassPage.map(klass -> KlassTuteeDto.ResponseTuteeKlassByTicketIdDto.toDto(klass, ticketId));
    }
}
