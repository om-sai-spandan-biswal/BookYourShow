package com.projects.backend.service.impl;

import com.projects.backend.dto.ShowDto;
import com.projects.backend.enitiy.Movie;
import com.projects.backend.enitiy.Screen;
import com.projects.backend.enitiy.Show;
import com.projects.backend.exception.ResourceNotFoundException;
import com.projects.backend.mapper.ShowMapper;
import com.projects.backend.repository.MovieRepository;
import com.projects.backend.repository.ScreenRepository;
import com.projects.backend.repository.ShowRepository;
import com.projects.backend.service.ShowService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;
    private final ShowMapper showMapper;
    private final MovieRepository movieRepository;
    private final ScreenRepository screenRepository;

    @Override
    public ShowDto getShowById(Long showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));
        return showMapper.toDto(show);
    }

    @Override
    public List<ShowDto> getShows() {
        List<Show> shows = showRepository.findAll();
        return shows.stream()
                .map(showMapper::toDto)
                .toList() ;
    }

    @Override
    public ShowDto createShow(Long movieId, Long screenId, ShowDto showDto) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found"));
        Show show = showMapper.toEntity(showDto);
        show.setMovie(movie);
        show.setScreen(screen);
        showRepository.save(show);
        return showMapper.toDto(show);
    }

    @Override
    @Transactional
    public ShowDto updateShow(Long showId, Long movieId, Long screenId, ShowDto showDto) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found"));
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));
        show.setMovie(movie);
        show.setScreen(screen);
        show.setBasePrice(showDto.getBasePrice());
        show.setStartTime(showDto.getStartTime());
        show.setStartTime(showDto.getStartTime());
        showRepository.save(show);
        return showMapper.toDto(show);
    }

    @Override
    public void deleteShow(Long showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));
        showRepository.delete(show);
    }
}
