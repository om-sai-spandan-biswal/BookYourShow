package com.projects.backend.repository;

import com.projects.backend.enitiy.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}