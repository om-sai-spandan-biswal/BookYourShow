package com.projects.BookYourShow.backend.repository;

import com.projects.BookYourShow.backend.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
