package com.projects.backend.repository;

import com.projects.backend.enitiy.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}