package com.projects.BookYourShow.backend.repository;

import com.projects.BookYourShow.backend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
