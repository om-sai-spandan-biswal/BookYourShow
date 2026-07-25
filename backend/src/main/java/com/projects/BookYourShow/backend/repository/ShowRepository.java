package com.projects.BookYourShow.backend.repository;

import com.projects.BookYourShow.backend.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowRepository extends JpaRepository<Show, Long> {
}