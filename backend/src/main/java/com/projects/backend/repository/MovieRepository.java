package com.projects.backend.repository;

import com.projects.backend.enitiy.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}