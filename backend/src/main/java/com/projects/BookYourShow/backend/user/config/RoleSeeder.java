package com.projects.BookYourShow.backend.user.config;

import com.projects.BookYourShow.backend.user.entity.Role;
import com.projects.BookYourShow.backend.user.enums.UserRole;
import com.projects.BookYourShow.backend.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoleSeeder {

    private final RoleRepository roleRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void seedRoles() {
        Set<UserRole> existingRoles = roleRepository.findAll()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        for (UserRole role : UserRole.values()) {
            if (!existingRoles.contains(role)) {
                roleRepository.save(new Role(role));
            }
        }
    }
}