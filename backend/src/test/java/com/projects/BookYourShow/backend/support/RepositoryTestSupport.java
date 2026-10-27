package com.projects.BookYourShow.backend.support;

import com.projects.BookYourShow.backend.movie.entity.Movie;
import com.projects.BookYourShow.backend.show.entity.Show;
import com.projects.BookYourShow.backend.show.entity.ShowSeat;
import com.projects.BookYourShow.backend.show.enums.ShowSeatStatus;
import com.projects.BookYourShow.backend.theater.entity.*;
import com.projects.BookYourShow.backend.user.entity.User;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.time.LocalDate;

@DataJpaTest(showSql = false)
@ActiveProfiles("test")
public abstract class RepositoryTestSupport {
    @Autowired protected EntityManager em;
    protected final LocalDate date = LocalDate.of(2026, 11, 1);
    protected <T> T persist(T value) { em.persist(value); return value; }
    protected User user(String email) {
        User user = new User(); user.setName("Customer"); user.setEmail(email);
        user.setPassword("encoded"); user.setEnabled(true); return persist(user);
    }
    protected Theater theater(String city) {
        return persist(Theater.builder().name("Cinema").city(city).address("Main Road")
            .owner(user(java.util.UUID.randomUUID() + "@example.com")).build());
    }
    protected Screen screen(Theater theater) {
        return persist(Screen.builder().name("Screen 1").theater(theater).build());
    }
    protected Movie movie(String title) {
        Movie movie = new Movie(); movie.setTitle(title); return persist(movie);
    }
    protected Show show(Movie movie, Screen screen, LocalDate day, int hour) {
        return persist(Show.builder().movie(movie).screen(screen).date(day)
            .startTime(day.atTime(hour,0)).endTime(day.atTime(hour+2,0)).basePrice(new BigDecimal("200")).build());
    }
    protected Seat seat(Screen screen, int number) {
        Seat seat = new Seat(); seat.setScreen(screen); seat.setRowName("A"); seat.setSeatNumber(number); return persist(seat);
    }
    protected ShowSeat showSeat(Show show, int number, ShowSeatStatus status) {
        return persist(ShowSeat.builder().show(show).seat(seat(show.getScreen(),number)).status(status).price(new BigDecimal("200")).build());
    }
}
