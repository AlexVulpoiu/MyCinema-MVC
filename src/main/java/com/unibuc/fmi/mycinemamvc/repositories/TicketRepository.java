package com.unibuc.fmi.mycinemamvc.repositories;

import com.unibuc.fmi.mycinemamvc.domain.MovieSchedule;
import com.unibuc.fmi.mycinemamvc.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT t " +
            "FROM Ticket t " +
            "WHERE t.movieSchedule = ?1 " +
            "ORDER BY t.numberOfRow, t.seat DESC " +
            "LIMIT 1")
    Optional<Ticket> findLastTicketForMovie(MovieSchedule movieSchedule);
}
