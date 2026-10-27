package com.projects.BookYourShow.backend.show.repository;

import com.projects.BookYourShow.backend.support.RepositoryTestSupport;
import com.projects.BookYourShow.backend.show.enums.ShowSeatStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import java.util.List;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;

// A separate context lets this test commit its fixture without leaking data into query tests.
@DataJpaTest(showSql = false, properties = "spring.jpa.properties.hibernate.jdbc.batch_size=0")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ShowSeatLockingTest extends RepositoryTestSupport {
    @Autowired ShowSeatRepository repository;
    @Autowired PlatformTransactionManager transactionManager;

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void competingTransactionWaitsAndCannotBookSeatAfterFirstTransactionCommits() throws Exception {
        var transaction = new TransactionTemplate(transactionManager);
        Long id = transaction.execute(status -> {
            var show = show(movie("Film"), screen(theater("Pune")), date, 10);
            return showSeat(show, 1, ShowSeatStatus.LOCKED).getId();
        });
        var firstHasLock = new CountDownLatch(1);
        var releaseFirst = new CountDownLatch(1);
        var secondStarted = new CountDownLatch(1);
        try (var executor = Executors.newFixedThreadPool(2)) {
            Future<?> first = executor.submit(() -> transaction.executeWithoutResult(status -> {
                var seats = repository.findAndLockAvailableShowSeats(List.of(id));
                assertEquals(1, seats.size());
                firstHasLock.countDown();
                await(releaseFirst);
                seats.getFirst().setStatus(ShowSeatStatus.BOOKED);
            }));
            try {
                assertTrue(firstHasLock.await(5, TimeUnit.SECONDS), "First transaction must acquire the seat lock");
                Future<Integer> second = executor.submit(() -> transaction.execute(status -> {
                    secondStarted.countDown();
                    return repository.findAndLockAvailableShowSeats(List.of(id)).size();
                }));
                assertTrue(secondStarted.await(5, TimeUnit.SECONDS));
                assertThrows(TimeoutException.class, () -> second.get(300, TimeUnit.MILLISECONDS),
                        "Competing transaction must wait while the first transaction holds the lock");
                releaseFirst.countDown();
                first.get(5, TimeUnit.SECONDS);
                assertEquals(0, second.get(5, TimeUnit.SECONDS));
                assertEquals(ShowSeatStatus.BOOKED, transaction.execute(status -> repository.findById(id).orElseThrow().getStatus()));
            } finally {
                releaseFirst.countDown();
            }
        }
    }

    private static void await(CountDownLatch latch) {
        try {
            if (!latch.await(5, TimeUnit.SECONDS)) throw new AssertionError("Timed out waiting for transaction coordination");
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new AssertionError(exception);
        }
    }
}
