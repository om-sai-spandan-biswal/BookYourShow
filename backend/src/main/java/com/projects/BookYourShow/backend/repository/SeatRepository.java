package com.projects.BookYourShow.backend.repository;

import com.projects.BookYourShow.backend.entity.Screen;
import com.projects.BookYourShow.backend.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByScreen(Screen screen);
}
