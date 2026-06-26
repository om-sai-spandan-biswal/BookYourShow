package com.projects.backend.service;

import com.projects.backend.dto.TheaterDto;

import java.util.List;

public interface TheaterService {
    TheaterDto getTheaterById(Long theaterId);
    List<TheaterDto> getAllTheaters();
    TheaterDto createTheater(TheaterDto theaterDto);
    TheaterDto updateTheater(Long theaterId, TheaterDto theaterDto);
    void deleteTheater(Long theaterId);
}
