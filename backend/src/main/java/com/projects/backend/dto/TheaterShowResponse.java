package com.projects.backend.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class TheaterShowResponse {
    private Long theaterId;
    private String theaterName;
    private String address;
    private List<ShowDto> shows  = new ArrayList<>();
}
