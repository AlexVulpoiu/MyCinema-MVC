package com.unibuc.fmi.mycinemamvc.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer numberOfRow;

    private Integer seat;

    @ManyToOne(fetch = FetchType.LAZY)
    private MovieSchedule movieSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;
}
