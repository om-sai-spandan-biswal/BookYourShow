package com.projects.backend.repository;

import com.projects.backend.enitiy.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
}