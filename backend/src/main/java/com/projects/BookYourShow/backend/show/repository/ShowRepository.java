package com.projects.BookYourShow.backend.show.repository;

import com.projects.BookYourShow.backend.show.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
}