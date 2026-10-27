package com.projects.BookYourShow.backend.show.repository;

import com.projects.BookYourShow.backend.show.entity.Show;
import com.projects.BookYourShow.backend.show.entity.ShowSeat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {
    List<ShowSeat> findByShow(Show show);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT ss FROM ShowSeat ss
            WHERE ss.id IN :seatIds
              AND ss.status = com.projects.BookYourShow.backend.show.enums.ShowSeatStatus.LOCKED
            """)
    List<ShowSeat> findAndLockAvailableShowSeats(@Param("seatIds") List<Long> seatIds);
}
