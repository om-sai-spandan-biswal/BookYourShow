package com.projects.backend.dto;

import com.projects.backend.enums.SeatType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeatDto {
    private Long id;

    private String rowName;

    private Integer seatNumber;

    private SeatType type;

    private ScreenDto screen;
}
