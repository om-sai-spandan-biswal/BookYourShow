package com.projects.BookYourShow.backend.repository;

import com.projects.BookYourShow.backend.entity.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {
}