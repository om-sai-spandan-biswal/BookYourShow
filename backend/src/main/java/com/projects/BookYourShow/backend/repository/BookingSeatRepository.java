package com.projects.BookYourShow.backend.repository;

import com.projects.BookYourShow.backend.entity.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {
}