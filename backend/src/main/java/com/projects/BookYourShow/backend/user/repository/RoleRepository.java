package com.projects.BookYourShow.backend.user.repository;

import com.projects.BookYourShow.backend.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
}