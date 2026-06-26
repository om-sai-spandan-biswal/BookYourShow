package com.projects.backend.service.impl;

import com.projects.backend.dto.ScreenDto;
import com.projects.backend.enitiy.Screen;
import com.projects.backend.enitiy.Theater;
import com.projects.backend.exception.ResourceNotFoundException;
import com.projects.backend.mapper.ScreenMapper;
import com.projects.backend.repository.ScreenRepository;
import com.projects.backend.repository.TheaterRepository;
import com.projects.backend.service.ScreenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreenServiceImpl implements ScreenService {
    private final ScreenRepository screenRepository;
    private final ScreenMapper screenMapper;
    private final TheaterRepository theaterRepository;

    @Override
    public ScreenDto getScreenById(Long screenId) {
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found with Id: " + screenId));
        return screenMapper.toDto(screen);
    }

    @Override
    public List<ScreenDto> getScreensByTheaterId(Long theaterId) {
        List<Screen> screens = screenRepository.findAll();
        return screens.stream()
                .map(screenMapper::toDto)
                .toList() ;
    }

    @Override
    public ScreenDto createScreen(Long theaterId, ScreenDto screenDto) {
        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found with Id: " + theaterId));
        Screen screen = screenMapper.toEntity(screenDto);
        screen.setTheater(theater);
        screenRepository.save(screen);
        return screenMapper.toDto(screen);
    }

    @Override
    public ScreenDto updateScreen(Long theaterId, Long screenId, ScreenDto screenDto) {
        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found with Id: " + theaterId));
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found with Id: " + screenId));
        screen.setTheater(theater);
        screen.setName(screenDto.getName());
        screen.setTotalSeats(screenDto.getTotalSeats());
        screenRepository.save(screen);
        return screenMapper.toDto(screen);

    }

    @Override
    public void deleteScreen(Long screenId) {
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found with Id: " + screenId));
        screenRepository.delete(screen);
    }
}
