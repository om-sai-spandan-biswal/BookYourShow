package com.projects.BookYourShow.backend.show.service.impl;

import com.projects.BookYourShow.backend.movie.dto.MovieResponse;
import com.projects.BookYourShow.backend.movie.entity.Movie;
import com.projects.BookYourShow.backend.movie.mapper.MovieMapper;
import com.projects.BookYourShow.backend.movie.service.MovieService;
import com.projects.BookYourShow.backend.shared.exceptions.ConflictException;
import com.projects.BookYourShow.backend.shared.exceptions.ResourceNotFoundException;
import com.projects.BookYourShow.backend.show.dto.ShowRequest;
import com.projects.BookYourShow.backend.show.dto.ShowResponse;
import com.projects.BookYourShow.backend.show.entity.Show;
import com.projects.BookYourShow.backend.show.mapper.ShowMapper;
import com.projects.BookYourShow.backend.show.repository.ShowRepository;
import com.projects.BookYourShow.backend.show.service.ShowSeatService;
import com.projects.BookYourShow.backend.show.service.ShowService;
import com.projects.BookYourShow.backend.theater.entity.Screen;
import com.projects.BookYourShow.backend.theater.service.ScreenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class ShowServiceImpl implements ShowService {
    private final ShowRepository showRepository;
    private final ShowMapper showMapper;
    private final MovieService movieService;
    private final MovieMapper movieMapper;
    private final ScreenService screenService;
    private final ShowSeatService showSeatService;
    final int PAGE_SIZE = 5;

    @Override
    public ShowResponse getShow(Long showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show Not Found"));
        return showMapper.toResponse(show);
    }

    @Override
    @Transactional
    public ShowResponse createShow(ShowRequest showRequest) {
        Movie movie = movieService.getMovie(showRequest.movieId());
        Screen screen = screenService.getScreen(showRequest.screenId());

        boolean existsOverlappingShow = showRepository.existsOverlappingShow(
                screen.getId(),
                showRequest.startTime().toLocalDate(),
                showRequest.startTime(),
                showRequest.endTime()) ;

        if(existsOverlappingShow) throw new ConflictException("Show Overlaps with another Show");

        Show show = Show.builder()
                .basePrice(showRequest.basePrice())
                .movie(movie)
                .screen(screen)
                .date(showRequest.startTime().toLocalDate())
                .startTime(showRequest.startTime())
                .endTime(showRequest.endTime())
                .build() ;

        show = showRepository.save(show);
        showSeatService.createShowSeats(show);
        return showMapper.toResponse(show);
    }

    @Override
    public void deleteShow(Long showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show Not Found"));
        showRepository.delete(show);
    }

    @Override
    public MovieResponse searchMovieByTitleAndCity(String title, String city) {
        Movie movie = showRepository.findMovieByTitleAndCity(title,city)
                .orElseThrow(() -> new ResourceNotFoundException("Movie Not Found"));
        return movieMapper.toResponse(movie);
    }

    @Override
    public Page<MovieResponse> searchMovieByCity(String city, Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.by("movie.title").ascending());
        Page<Movie> movies = showRepository.findMoviesByCity(city,pageable);
        return movies.map(movieMapper::toResponse) ;
    }
}
