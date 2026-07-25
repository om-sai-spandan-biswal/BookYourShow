package com.projects.BookYourShow.backend.service.impl;

import com.projects.BookYourShow.backend.dto.SeatRequest;
import com.projects.BookYourShow.backend.dto.SeatResponse;
import com.projects.BookYourShow.backend.entity.Screen;
import com.projects.BookYourShow.backend.entity.Seat;
import com.projects.BookYourShow.backend.exceptions.ResourceNotFoundException;
import com.projects.BookYourShow.backend.mapper.ScreenMapper;
import com.projects.BookYourShow.backend.mapper.SeatMapper;
import com.projects.BookYourShow.backend.repository.ScreenRepository;
import com.projects.BookYourShow.backend.repository.SeatRepository;
import com.projects.BookYourShow.backend.service.SeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {
    private final SeatRepository seatRepository;
    private final SeatMapper seatMapper;
    private final ScreenRepository screenRepository;

    @Override
    public List<SeatResponse> getSeatsOfScreen(Long screenId) {
        log.info("Getting seats of screen with id {}", screenId);
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found"));

        List<Seat> seats = seatRepository.findByScreen(screen);
        return seats.stream().map(seatMapper::toResponse).toList() ;
    }

    @Override
    public SeatResponse createSeat(Long screenId, SeatRequest seatRequest) {
        log.info("Creating seat in Screen with id {}", screenId);
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found"));

        Seat seat = seatMapper.toEntity(seatRequest);
        seat.setScreen(screen);
        seat = seatRepository.save(seat);

        log.info("Seat with id {} has been created", seat.getId());
        return seatMapper.toResponse(seat);
    }

    @Override
    public List<SeatResponse> createSeatsInBulk(Long screenId, List<SeatRequest> seatRequests) {
        log.info("Creating seats in Screen with id {}", screenId);
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found"));

        List<Seat> seats = new ArrayList<>() ;
        for (SeatRequest seatRequest : seatRequests) {
            Seat seat = seatMapper.toEntity(seatRequest);
            seat.setScreen(screen);
            seats.add(seat);
        };
        seatRepository.saveAll(seats);
        log.info("Seats in Screen with id {} has been created", screenId);
        return seats.stream().map(seatMapper::toResponse).toList();
    }

    @Override
    public SeatResponse updateSeat(Long seatId, SeatRequest seatRequest) {
        log.info("Updating Seat with id {}", seatId);
        Seat seat = seatRepository.findById(seatId)
                        .orElseThrow(() -> new ResourceNotFoundException("Seat with id " + seatId + " not found"));

        seat.setRowName(seatRequest.rowName());
        seat.setSeatNumber(seatRequest.seatNumber());
        seat.setType(seatRequest.type());

        log.info("Seat with id {} has been updated", seatId);
        seat = seatRepository.save(seat);
        return seatMapper.toResponse(seat);
    }

    @Override
    public void deleteSeat(Long seatId) {
        log.info("Deleting Seat with id {}", seatId);
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException("Seat with id " + seatId + " not found"));
        seatRepository.delete(seat);
        log.info("Seat with id {} has been deleted", seatId);
    }
}
