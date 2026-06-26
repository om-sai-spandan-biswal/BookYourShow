package com.projects.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShowDto {
    private Long id;

    private MovieDto movie;

    private ScreenDto screen;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private BigDecimal basePrice;
}
