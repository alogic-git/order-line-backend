package com.orderline.ticket.model.entity;

import com.orderline.basic.model.entity.BaseTimeEntity;
import com.orderline.product.model.entity.Product;
import com.orderline.ticket.enums.TicketPauseReasonTypeEnum;
import com.orderline.ticket.model.dto.TicketPauseHistoryDto;
import com.orderline.branch.model.entity.Branch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

@DynamicUpdate
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
@Where(clause = "delete_yn = 0")
@Table(name = "ticket_pause_history")
public class TicketPauseHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason_type")
    private TicketPauseReasonTypeEnum reasonType;

    @Column(name = "extension_yn")
    private Boolean extensionYn;

    public void updateTicketPauseHistory(TicketPauseHistoryDto.RequestTicketPauseHistoryDto requestTicketPauseHistoryDto , Branch branch, Product product, Ticket ticket){
        this.branch = branch;
        this.product = product;
        this.ticket = ticket;
        this.startDate = requestTicketPauseHistoryDto.getStartDate();
        this.endDate = requestTicketPauseHistoryDto.getEndDate();
        this.reasonType = requestTicketPauseHistoryDto.getReasonType();
    }
}
