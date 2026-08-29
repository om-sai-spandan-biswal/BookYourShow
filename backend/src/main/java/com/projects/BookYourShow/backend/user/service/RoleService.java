package com.projects.BookYourShow.backend.user.service;

import com.projects.BookYourShow.backend.user.entity.Role;

public interface RoleService {
    Role getAdminRole();
    Role getUserRole();
}
