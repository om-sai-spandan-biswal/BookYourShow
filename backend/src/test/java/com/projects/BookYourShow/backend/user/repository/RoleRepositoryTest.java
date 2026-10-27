package com.projects.BookYourShow.backend.user.repository;

import com.projects.BookYourShow.backend.support.RepositoryTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;

class RoleRepositoryTest extends RepositoryTestSupport {
    @Autowired RoleRepository repository;

    @Test void resolvesRoleByEnumName() {
        var admin = persist(new com.projects.BookYourShow.backend.user.entity.Role(com.projects.BookYourShow.backend.user.enums.UserRole.ADMIN));
        em.flush(); em.clear();
        assertEquals(admin.getId(),repository.findByName(com.projects.BookYourShow.backend.user.enums.UserRole.ADMIN).getId());
        assertNull(repository.findByName(com.projects.BookYourShow.backend.user.enums.UserRole.USER));
    }
}
