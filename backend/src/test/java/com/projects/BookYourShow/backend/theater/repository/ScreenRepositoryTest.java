package com.projects.BookYourShow.backend.theater.repository;

import com.projects.BookYourShow.backend.support.RepositoryTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;

class ScreenRepositoryTest extends RepositoryTestSupport {
    @Autowired ScreenRepository repository;

    @Test void findsOnlyScreensOfRequestedTheater() {
        var first = theater("Pune"); var second = theater("Mumbai"); var screen = screen(first); screen(second); em.flush(); em.clear();
        assertEquals(java.util.List.of(screen.getId()),repository.findByTheater(first).stream().map(com.projects.BookYourShow.backend.theater.entity.Screen::getId).toList());
        assertTrue(repository.findByTheater(theater("Delhi")).isEmpty());
    }
}
