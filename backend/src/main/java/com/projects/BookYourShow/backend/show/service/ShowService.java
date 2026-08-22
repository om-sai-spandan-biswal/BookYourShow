package com.projects.BookYourShow.backend.show.service;

import com.projects.BookYourShow.backend.movie.dto.MovieResponse;
import com.projects.BookYourShow.backend.show.dto.ShowRequest;
import com.projects.BookYourShow.backend.show.dto.ShowResponse;
import org.springframework.data.domain.Page;


public interface ShowService {
    ShowResponse getShow(Long showId);
    ShowResponse createShow(ShowRequest showRequest);
    void deleteShow(Long showId);
    MovieResponse searchMovieByTitleAndCity(String title, String city);
    Page<MovieResponse> searchMovieByCity(String city,Integer pageNumber);
}
