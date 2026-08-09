package com.projects.BookYourShow.backend.show.repository;

import com.projects.BookYourShow.backend.show.entity.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {
}