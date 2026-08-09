package com.projects.BookYourShow.backend.booking.repository;

import com.projects.BookYourShow.backend.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}