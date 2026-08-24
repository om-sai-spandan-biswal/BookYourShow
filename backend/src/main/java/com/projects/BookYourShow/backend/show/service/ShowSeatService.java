package com.projects.BookYourShow.backend.show.service;

import com.projects.BookYourShow.backend.show.dto.ShowSeatResponse;
import com.projects.BookYourShow.backend.show.entity.Show;
import com.projects.BookYourShow.backend.show.entity.ShowSeat;

import java.util.List;

public interface ShowSeatService {
    void createShowSeats(Show show);
    List<ShowSeat> getShowSeats(List<Long> showSeatIds);
    List<ShowSeatResponse> getShowSeatsOfShow(Long showId);
}
