package com.projects.backend.service.impl;

import com.projects.backend.dto.SeatDto;
import com.projects.backend.enitiy.Screen;
import com.projects.backend.enitiy.Seat;
import com.projects.backend.exception.ResourceNotFoundException;
import com.projects.backend.mapper.SeatMapper;
import com.projects.backend.repository.ScreenRepository;
import com.projects.backend.repository.SeatRepository;
import com.projects.backend.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final ScreenRepository screenRepository;
    private final SeatMapper seatMapper;

    @Override
    public List<SeatDto> getAllSeats(Long screenId) {
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found with id " + screenId));
        List<Seat> seats = seatRepository.findByScreen(screen);
        return seats.stream()
                .map(seatMapper::toDto)
                .toList() ;
    }

    @Override
    public SeatDto createSeat(Long screenId, SeatDto seatDto) {
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found with id " + screenId));
        Seat seat = seatMapper.toEntity(seatDto) ;
        seat.setScreen(screen);
        seatRepository.save(seat);
        return seatMapper.toDto(seat);
    }

    @Override
    public SeatDto updateSeat(Long seatId, SeatDto seatDto) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with id " + seatId));
        seat.setSeatNumber(seatDto.getSeatNumber());
        seat.setType(seatDto.getType());
        seat.setRowName(seatDto.getRowName());
        seatRepository.save(seat);
        return seatMapper.toDto(seat);
    }

    @Override
    public void deleteSeat(Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with id " + seatId));
        seatRepository.deleteById(seatId);
    }
}
