package com.projects.backend.repository;

import com.projects.backend.enitiy.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("""
    SELECT DISTINCT s.movie
    FROM Show s
    WHERE LOWER(s.screen.theater.city) = LOWER(:city)
""")
    List<Movie> findMoviesByCity(String city);
}