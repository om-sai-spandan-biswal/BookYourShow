package com.projects.BookYourShow.backend.theater.repository;

import com.projects.BookYourShow.backend.support.RepositoryTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;

class SeatRepositoryTest extends RepositoryTestSupport {
    @Autowired SeatRepository repository;

    @Test void findsOnlySeatsOfRequestedScreen() {
        var theater = theater("Pune"); var first = screen(theater); var second = screen(theater);
        var seat = seat(first,1); seat(second,1); em.flush(); em.clear();
        assertEquals(java.util.List.of(seat.getId()),repository.findByScreen(first).stream().map(com.projects.BookYourShow.backend.theater.entity.Seat::getId).toList());
        assertTrue(repository.findByScreen(screen(theater)).isEmpty());
    }
}
