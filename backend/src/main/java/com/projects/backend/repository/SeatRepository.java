package com.projects.backend.repository;

import com.projects.backend.enitiy.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}