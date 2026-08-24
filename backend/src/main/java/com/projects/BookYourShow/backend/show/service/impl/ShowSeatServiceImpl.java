package com.projects.BookYourShow.backend.show.service.impl;

import com.projects.BookYourShow.backend.shared.exceptions.ResourceNotFoundException;
import com.projects.BookYourShow.backend.show.dto.ShowSeatResponse;
import com.projects.BookYourShow.backend.show.entity.Show;
import com.projects.BookYourShow.backend.show.entity.ShowSeat;
import com.projects.BookYourShow.backend.show.enums.ShowSeatStatus;
import com.projects.BookYourShow.backend.show.mapper.ShowSeatMapper;
import com.projects.BookYourShow.backend.show.repository.ShowRepository;
import com.projects.BookYourShow.backend.show.repository.ShowSeatRepository;
import com.projects.BookYourShow.backend.show.service.ShowSeatService;
import com.projects.BookYourShow.backend.theater.entity.Seat;
import com.projects.BookYourShow.backend.theater.service.SeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShowSeatServiceImpl implements ShowSeatService {
    private final ShowRepository showRepository;
    private final ShowSeatRepository showSeatRepository;
    private final SeatService seatService;
    private final ShowSeatMapper showSeatMapper;

    @Override
    public void createShowSeats(Show show) {
        List<Seat> seats = seatService.getSeatsOfScreen(show.getScreen().getId());
        List<ShowSeat> showSeats = new ArrayList<>();
        for (Seat seat : seats) {
            ShowSeat showSeat = ShowSeat.builder()
                    .show(show)
                    .price(show.getBasePrice()) // TODO change dynamic price
                    .status(ShowSeatStatus.AVAILABLE)
                    .seat(seat)
                    .build() ;
            showSeats.add(showSeat);
        }
        showSeatRepository.saveAll(showSeats);
    }

    @Override
    public List<ShowSeat> getShowSeats(List<Long> showSeatIds) {
        return showSeatRepository.findAllById(showSeatIds);  // TODO optimize
    }

    @Override
    public List<ShowSeatResponse> getShowSeatsOfShow(Long showId) {
        Show show = showRepository.findById(showId)
                        .orElseThrow(() -> new ResourceNotFoundException("Show Not Exist with ID "+showId));
        return showSeatRepository.findByShow(show)
                .stream()
                .map(showSeatMapper::toResponse)
                .toList();
    }
}
