package com.projects.backend.enitiy;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shows")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id")
    private Screen screen;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Column(precision = 10, scale = 2)
    private BigDecimal basePrice;

    @OneToMany(mappedBy = "show")
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "show")
    private List<ShowSeat> showSeats = new ArrayList<>();
}