package com.projects.backend.service;

import com.projects.backend.dto.ScreenDto;

import java.util.List;

public interface ScreenService {
    ScreenDto getScreenById(Long screenId) ;
    List<ScreenDto> getScreensByTheaterId(Long theaterId);
    ScreenDto createScreen(Long theaterId, ScreenDto screenDto);
    ScreenDto updateScreen(Long theaterId, Long screenId, ScreenDto screenDto);
    void deleteScreen(Long screenId);
}
