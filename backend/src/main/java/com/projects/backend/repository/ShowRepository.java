package com.projects.backend.repository;

import com.projects.backend.enitiy.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShowRepository extends JpaRepository<Show, Long> {
    @Query("""
    SELECT s
    FROM Show s
    WHERE s.movie.id = :movieId
      AND LOWER(s.screen.theater.city) = LOWER(:city)
    ORDER BY s.screen.theater.name, s.startTime
""")
    List<Show> findByMovieAndCity(Long movieId, String city);
}