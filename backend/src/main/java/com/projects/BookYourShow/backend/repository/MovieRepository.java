package com.projects.BookYourShow.backend.repository;

import com.projects.BookYourShow.backend.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}