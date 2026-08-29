package com.projects.BookYourShow.backend.user.service.impl;

import com.projects.BookYourShow.backend.user.entity.Role;
import com.projects.BookYourShow.backend.user.enums.UserRole;
import com.projects.BookYourShow.backend.user.repository.RoleRepository;
import com.projects.BookYourShow.backend.user.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getAdminRole() {
        return roleRepository.findByName(UserRole.ADMIN) ;
    }

    @Override
    public Role getUserRole() {
        return roleRepository.findByName(UserRole.USER) ;
    }
}
