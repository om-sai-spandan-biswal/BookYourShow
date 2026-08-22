package com.projects.BookYourShow.backend.show.repository;

import com.projects.BookYourShow.backend.movie.entity.Movie;
import com.projects.BookYourShow.backend.show.entity.Show;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShowRepository extends JpaRepository<Show, Long> {
    @Query("""
        SELECT COUNT(s) > 0
        FROM Show s
        WHERE s.screen.id = :screenId
            AND s.date = :date
            AND s.endTime > :startTime
            AND s.startTime < :endTime
    """)
    boolean existsOverlappingShow(
            @Param("screenId") Long screenId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    @Query("""
    SELECT DISTINCT s.movie
    FROM Show s
    WHERE LOWER(s.screen.theater.city) = LOWER(:city)
      AND LOWER(s.movie.title) = LOWER(:title)
""")
    Optional<Movie> findMovieByTitleAndCity(
            @Param("title") String title,
            @Param("city") String city
    );

    @Query("""
    SELECT DISTINCT s.movie
    FROM Show s
    WHERE LOWER(s.screen.theater.city) = LOWER(:city)
""")
    Page<Movie> findMoviesByCity(
            @Param("city") String city,
            Pageable pageable);
}