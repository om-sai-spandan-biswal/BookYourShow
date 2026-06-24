package com.projects.backend.repository;

import com.projects.backend.enitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}