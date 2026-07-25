package com.projects.BookYourShow.backend.service;

import com.projects.BookYourShow.backend.dto.ScreenRequest;
import com.projects.BookYourShow.backend.dto.ScreenResponse;

import java.util.List;

public interface ScreenService {
    List<ScreenResponse> getAllScreensOfTheater(Long theaterId);
    ScreenResponse createScreen(Long theaterId,ScreenRequest request);
    ScreenResponse updateScreen(Long screenId, ScreenRequest request);
    void deleteScreen(Long screenId);
}
