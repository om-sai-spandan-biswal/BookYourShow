package com.projects.backend.enitiy;

import com.projects.backend.enums.SeatType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rowName;

    private Integer seatNumber;

    @Enumerated(EnumType.STRING)
    private SeatType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id")
    private Screen screen;

    @OneToMany(mappedBy = "seat")
    private List<ShowSeat> showSeats = new ArrayList<>();
}