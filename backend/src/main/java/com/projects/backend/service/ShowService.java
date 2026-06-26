package com.projects.backend.service;

import com.projects.backend.dto.ShowDto;

import java.util.List;

public interface ShowService {
    ShowDto getShowById(Long showId) ;
    List<ShowDto>  getShows();
    ShowDto createShow(Long movieId, Long screenId, ShowDto showDto) ;
    ShowDto updateShow(Long showId, Long movieId, Long screenId, ShowDto showDto) ;
    void deleteShow(Long showId);
}
