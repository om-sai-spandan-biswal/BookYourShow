package com.projects.BookYourShow.backend.movie.repository;

import com.projects.BookYourShow.backend.movie.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}