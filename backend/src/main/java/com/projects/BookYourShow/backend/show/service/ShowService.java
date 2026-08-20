package com.projects.BookYourShow.backend.show.service;

import com.projects.BookYourShow.backend.show.dto.ShowRequest;
import com.projects.BookYourShow.backend.show.dto.ShowResponse;

public interface ShowService {
    ShowResponse getShow(Long showId);
    ShowResponse createShow(ShowRequest showRequest);
    void deleteShow(Long showId);
}
