package com.ptglue.klass.service;

import com.ptglue.basic.exception.NotFoundException;
import com.ptglue.klass.model.dto.KlassTuteeDto;
import com.ptglue.klass.model.entity.Klass;
import com.ptglue.klass.repository.KlassRepository;
import com.ptglue.product.model.entity.ProductKlass;
import com.ptglue.product.repository.ProductKlassRepository;
import com.ptglue.ticket.model.entity.Ticket;
import com.ptglue.ticket.repository.TicketRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
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
