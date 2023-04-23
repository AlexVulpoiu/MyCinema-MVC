package com.unibuc.fmi.mycinemamvc.repositories;

import com.unibuc.fmi.mycinemamvc.composed_id.MovieScheduleId;
import com.unibuc.fmi.mycinemamvc.domain.Movie;
import com.unibuc.fmi.mycinemamvc.domain.MovieSchedule;
import com.unibuc.fmi.mycinemamvc.domain.Room;
import com.unibuc.fmi.mycinemamvc.domain.Ticket;
import com.unibuc.fmi.mycinemamvc.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@DataJpaTest
@Rollback(false)
@ActiveProfiles("h2")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TicketRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MovieScheduleRepository movieScheduleRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @Test
    public void findLastTicketForMovieTest() {
        Room room = roomRepository.findByName("Room 1")
                .orElseThrow(() -> new ResourceNotFoundException("Room with name Room 1 is not found!"));

        Movie movie = movieRepository.findByName("Movie name 1")
                .orElseThrow(() -> new ResourceNotFoundException("Movie with name Movie name 1 is not found!"));

        MovieScheduleId movieScheduleId = MovieScheduleId.builder()
                .movieId(movie.getId())
                .roomId(room.getId())
                .date(LocalDate.now().plusDays(1))
                .hour(LocalTime.of(12, 0))
                .build();
        MovieSchedule movieSchedule = MovieSchedule.builder()
                .id(movieScheduleId)
                .movie(movie)
                .room(room)
                .price(20)
                .build();
        movieSchedule = movieScheduleRepository.save(movieSchedule);

        Optional<Ticket> optionalTicket = ticketRepository.findLastTicketForMovie(movieSchedule);
        assertEquals(Optional.empty(), optionalTicket);
    }
}
