package com.projects.BookYourShow.backend.theater.service;

import com.projects.BookYourShow.backend.theater.dto.ScreenRequest;
import com.projects.BookYourShow.backend.theater.dto.ScreenResponse;

import java.util.List;

public interface ScreenService {
    List<ScreenResponse> getAllScreensOfTheater(Long theaterId);
    ScreenResponse createScreen(Long theaterId,ScreenRequest request);
    ScreenResponse updateScreen(Long screenId, ScreenRequest request);
    void deleteScreen(Long screenId);
}
