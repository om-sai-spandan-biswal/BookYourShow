package com.projects.BookYourShow.backend.show.repository;

import com.projects.BookYourShow.backend.support.RepositoryTestSupport;
import com.projects.BookYourShow.backend.show.entity.ShowSeat;
import com.projects.BookYourShow.backend.show.enums.ShowSeatStatus;
import jakarta.persistence.LockModeType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ShowSeatRepositoryTest extends RepositoryTestSupport {
    @Autowired ShowSeatRepository repository;
    @Test void findsOnlySeatsBelongingToRequestedShow() {
        var screen = screen(theater("Pune")); var movie = movie("Film"); var first = show(movie,screen,date,10);
        var second = show(movie,screen,date,14); var wanted = showSeat(first,1,ShowSeatStatus.AVAILABLE);
        showSeat(second,2,ShowSeatStatus.AVAILABLE); em.flush(); em.clear();
        assertEquals(List.of(wanted.getId()),repository.findByShow(first).stream().map(ShowSeat::getId).toList());
    }
    @Test void lockingQueryReturnsOnlyRequestedLockedSeatsAndAppliesWriteLock() {
        var show = show(movie("Film"),screen(theater("Pune")),date,10);
        var locked = showSeat(show,1,ShowSeatStatus.LOCKED); var available = showSeat(show,2,ShowSeatStatus.AVAILABLE);
        var booked = showSeat(show,3,ShowSeatStatus.BOOKED); showSeat(show,4,ShowSeatStatus.LOCKED); em.flush(); em.clear();
        // Despite its name, this query intentionally selects LOCKED seats for confirmation.
        var result = repository.findAndLockAvailableShowSeats(List.of(locked.getId(),available.getId(),booked.getId(),-1L));
        assertEquals(List.of(locked.getId()),result.stream().map(ShowSeat::getId).toList());
        assertEquals(LockModeType.PESSIMISTIC_WRITE,em.getLockMode(result.getFirst()));
    }
    @Test void lockingQueryHandlesEmptyMissingAndDuplicateIds() {
        var show = show(movie("Film"),screen(theater("Pune")),date,10); var locked = showSeat(show,1,ShowSeatStatus.LOCKED); em.flush(); em.clear();
        assertTrue(repository.findAndLockAvailableShowSeats(List.of()).isEmpty());
        assertTrue(repository.findAndLockAvailableShowSeats(List.of(-1L)).isEmpty());
        assertEquals(1,repository.findAndLockAvailableShowSeats(List.of(locked.getId(),locked.getId())).size());
    }
    @Test void bookedSeatsAreExcludedFromSubsequentConfirmation() {
        var show = show(movie("Film"),screen(theater("Pune")),date,10); var locked = showSeat(show,1,ShowSeatStatus.LOCKED); em.flush(); em.clear();
        repository.findAndLockAvailableShowSeats(List.of(locked.getId())).getFirst().setStatus(ShowSeatStatus.BOOKED);
        em.flush(); em.clear(); assertTrue(repository.findAndLockAvailableShowSeats(List.of(locked.getId())).isEmpty());
    }
}
