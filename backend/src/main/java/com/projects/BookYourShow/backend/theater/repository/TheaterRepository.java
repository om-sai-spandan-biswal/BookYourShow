package com.projects.BookYourShow.backend.theater.repository;

import com.projects.BookYourShow.backend.theater.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
}