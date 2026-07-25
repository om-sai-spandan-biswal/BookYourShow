package com.projects.BookYourShow.backend.repository;

import com.projects.BookYourShow.backend.entity.Screen;
import com.projects.BookYourShow.backend.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScreenRepository extends JpaRepository<Screen, Long> {
    List<Screen> findByTheater(Theater theater);
}
