package com.projects.BookYourShow.backend.show.repository;

import com.projects.BookYourShow.backend.show.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowRepository extends JpaRepository<Show, Long> {
}