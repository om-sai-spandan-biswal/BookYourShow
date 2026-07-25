package com.projects.BookYourShow.backend.service.impl;

import com.projects.BookYourShow.backend.dto.TheaterRequest;
import com.projects.BookYourShow.backend.dto.TheaterResponse;
import com.projects.BookYourShow.backend.entity.Theater;
import com.projects.BookYourShow.backend.exceptions.ResourceNotFoundException;
import com.projects.BookYourShow.backend.mapper.TheaterMapper;
import com.projects.BookYourShow.backend.repository.TheaterRepository;
import com.projects.BookYourShow.backend.service.TheaterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TheaterServiceImpl implements TheaterService {

    private final TheaterRepository theaterRepository;
    private final TheaterMapper theaterMapper;

    @Override
    public List<TheaterResponse> getTheaters() {
        log.info("Getting theaters");
        List<Theater> theaters = theaterRepository.findAll();
        List<TheaterResponse> theaterResponse = new ArrayList<>();
        for (Theater theater : theaters) {
            theaterResponse.add(theaterMapper.toResponse(theater));
        }
        return theaterResponse;
    }

    @Override
    public TheaterResponse createTheater(TheaterRequest theaterRequest) {
        log.info("Creating new Theater");
        Theater theater = theaterMapper.toEntity(theaterRequest);
        theater = theaterRepository.save(theater);
        log.info("Created Theater with ID {}", theater.getId());
        return theaterMapper.toResponse(theater) ;
    }

    @Override
    public TheaterResponse updateTheater(Long theaterId, TheaterRequest theaterRequest) {
        log.info("Updating Theater with ID {}", theaterId);
        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new ResourceNotFoundException("Theater Not Exist with ID "+theaterId)) ;

        //updating fields
        theater.setName(theaterRequest.name());
        theater.setAddress(theaterRequest.address());
        theater.setCity(theaterRequest.city());

        theater = theaterRepository.save(theater);
        log.info("Updated Theater with ID {}", theater.getId());
        return theaterMapper.toResponse(theater) ;
    }

    @Override
    public void activateTheater(Long theaterId) {
        log.info("Activating Theater with ID {}", theaterId);
        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new ResourceNotFoundException("Theater Not Exist with ID "+theaterId)) ;
        if(theater.getActive()) throw new RuntimeException("Hotel Already Activated");
        theater.setActive(true);
        theaterRepository.save(theater);
        log.info("Activated Theater with ID {}", theater.getId());
    }

    @Override
    public void deleteTheater(Long theaterId) {
        log.info("Deleting Theater with ID {}", theaterId);
        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new ResourceNotFoundException("Theater Not Exist with ID "+theaterId)) ;
        theaterRepository.delete(theater);  // TODO : applying Soft delete
        log.info("Deleted Theater with ID {}", theater.getId());

    }
}
