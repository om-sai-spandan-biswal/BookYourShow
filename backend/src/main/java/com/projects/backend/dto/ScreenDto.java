package com.projects.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScreenDto {
    private Long id;
    private String name;
    private Integer totalSeats;
    private TheaterDto theater;
}
