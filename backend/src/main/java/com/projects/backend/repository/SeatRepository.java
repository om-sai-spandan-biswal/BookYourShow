package com.projects.backend.repository;

import com.projects.backend.enitiy.Screen;
import com.projects.backend.enitiy.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByScreen(Screen screen);
}