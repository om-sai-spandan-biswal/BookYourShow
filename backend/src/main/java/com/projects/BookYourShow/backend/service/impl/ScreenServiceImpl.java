package com.projects.BookYourShow.backend.service.impl;

import com.projects.BookYourShow.backend.dto.ScreenRequest;
import com.projects.BookYourShow.backend.dto.ScreenResponse;
import com.projects.BookYourShow.backend.entity.Screen;
import com.projects.BookYourShow.backend.entity.Theater;
import com.projects.BookYourShow.backend.exceptions.ResourceNotFoundException;
import com.projects.BookYourShow.backend.mapper.ScreenMapper;
import com.projects.BookYourShow.backend.repository.ScreenRepository;
import com.projects.BookYourShow.backend.repository.TheaterRepository;
import com.projects.BookYourShow.backend.service.ScreenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScreenServiceImpl implements ScreenService {
    private final TheaterRepository theaterRepository;
    private final ScreenRepository screenRepository;
    private final ScreenMapper screenMapper;

    @Override
    public List<ScreenResponse> getAllScreensOfTheater(Long theaterId) {
        log.info("Getting Screens of Theater with ID {}", theaterId);
        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new ResourceNotFoundException("Theater Not Exist with ID "+theaterId)) ;

        List<Screen> screens = screenRepository.findByTheater(theater);
        List<ScreenResponse> screenResponse = new ArrayList<>();
        for(Screen screen : screens) {
            screenResponse.add(screenMapper.toResponse(screen));
        }
        return screenResponse;
    }

    @Override
    public ScreenResponse createScreen(Long theaterId, ScreenRequest request) {
        log.info("Creating new Screen in Theater with ID {}", theaterId);
        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new ResourceNotFoundException("Theater Not Exist with ID "+theaterId)) ;
        Screen screen = screenMapper.toEntity(request);
        screen.setTheater(theater);
        screen.setClosed(false);
        screen = screenRepository.save(screen);
        log.info("Created Screen with ID {}", screen.getId());
        return screenMapper.toResponse(screen);
    }

    @Override
    public ScreenResponse updateScreen(Long screenId, ScreenRequest request) {
        log.info("Updating screen with ID {}", screenId);
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new ResourceNotFoundException("Screen Not Exist with ID "+screenId));
        screen.setName(request.name());
        screen.setTotalSeats(request.totalSeats());
        screen = screenRepository.save(screen);
        log.info("Updated Screen with ID {}", screen.getId());
        return screenMapper.toResponse(screen);
    }

    @Override
    public void deleteScreen(Long screenId) {
        log.info("Deleting screen with ID {}", screenId);
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new ResourceNotFoundException("Screen Not Exist with ID "+screenId));
        screenRepository.delete(screen);
        log.info("Deleted screen with ID {}", screen.getId());
    }
}
