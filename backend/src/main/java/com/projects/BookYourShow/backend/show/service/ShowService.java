package com.projects.BookYourShow.backend.show.service;

import com.projects.BookYourShow.backend.movie.dto.MovieResponse;
import com.projects.BookYourShow.backend.movie.dto.MovieShowsResponse;
import com.projects.BookYourShow.backend.show.dto.ShowRequest;
import com.projects.BookYourShow.backend.show.dto.ShowResponse;
import com.projects.BookYourShow.backend.show.dto.TheaterShowsResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;


public interface ShowService {
    ShowResponse getShow(Long showId);
    ShowResponse createShow(ShowRequest showRequest);
    void deleteShow(Long showId);
    MovieResponse searchMovieByTitleAndCity(String title, String city);
    Page<MovieResponse> searchMovieByCity(String city,Integer pageNumber);
    MovieShowsResponse findShows(Long movieId, String city, LocalDate date);
}
