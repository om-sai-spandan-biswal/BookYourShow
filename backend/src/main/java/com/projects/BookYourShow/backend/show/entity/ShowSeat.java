package com.projects.BookYourShow.backend.show.entity;

import com.projects.BookYourShow.backend.booking.entity.BookingSeat;
import com.projects.BookYourShow.backend.theater.entity.Seat;
import com.projects.BookYourShow.backend.show.enums.ShowSeatStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        uniqueConstraints = { @UniqueConstraint(columnNames = {"show_id", "seat_id"})}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_id",nullable = false)
    private Show show;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id",nullable = false)
    private Seat seat;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private ShowSeatStatus status;
}