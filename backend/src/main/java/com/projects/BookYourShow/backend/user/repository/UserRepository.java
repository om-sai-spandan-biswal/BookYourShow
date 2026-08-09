package com.projects.BookYourShow.backend.user.repository;

import com.projects.BookYourShow.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}