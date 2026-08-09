package com.projects.BookYourShow.backend.theater.service;

import com.projects.BookYourShow.backend.theater.dto.TheaterRequest;
import com.projects.BookYourShow.backend.theater.dto.TheaterResponse;

import java.util.List;

public interface TheaterService {
    List<TheaterResponse> getTheaters();
    TheaterResponse createTheater(TheaterRequest theaterRequest) ;
    TheaterResponse updateTheater(Long theaterId, TheaterRequest theaterRequest) ;
    void activateTheater(Long theaterId) ;
    void deleteTheater(Long theaterId) ;
}
