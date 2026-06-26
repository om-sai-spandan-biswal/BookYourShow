package com.projects.backend.service.impl;

import com.projects.backend.dto.TheaterDto;
import com.projects.backend.enitiy.Theater;
import com.projects.backend.exception.ResourceNotFoundException;
import com.projects.backend.mapper.TheaterMapper;
import com.projects.backend.repository.TheaterRepository;
import com.projects.backend.service.TheaterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TheaterServiceImpl implements TheaterService {

    private final TheaterRepository theaterRepository;
    private final TheaterMapper theaterMapper;

    @Override
    public TheaterDto getTheaterById(Long theaterId) {
        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id: " + theaterId));
        return theaterMapper.toDto(theater);
    }

    @Override
    public List<TheaterDto> getAllTheaters() {
        List<Theater> theaters = theaterRepository.findAll();
        return theaters.stream()
                .map(theaterMapper::toDto)
                .toList();
    }

    @Override
    public TheaterDto createTheater(TheaterDto theaterDto) {
        Theater theater = theaterMapper.toEntity(theaterDto);
        theaterRepository.save(theater);
        return theaterMapper.toDto(theater);
    }

    @Override
    public TheaterDto updateTheater(Long theaterId, TheaterDto theaterDto) {
        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id: " + theaterId));
        theater.setName(theaterDto.getName());
        theater.setAddress(theaterDto.getAddress());
        theater.setCity(theaterDto.getCity());
        theaterRepository.save(theater);
        return theaterMapper.toDto(theater);
    }

    @Override
    public void deleteTheater(Long theaterId) {
        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id: " + theaterId));
        theaterRepository.delete(theater);
    }
}
