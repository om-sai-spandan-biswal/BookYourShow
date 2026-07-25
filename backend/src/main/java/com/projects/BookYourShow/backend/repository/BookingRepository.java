package com.projects.BookYourShow.backend.repository;

import com.projects.BookYourShow.backend.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}