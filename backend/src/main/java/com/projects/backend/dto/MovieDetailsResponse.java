package com.projects.backend.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
public class MovieDetailsResponse {
    private MovieDto movie;
    private List<TheaterShowResponse> theaters ;

}
