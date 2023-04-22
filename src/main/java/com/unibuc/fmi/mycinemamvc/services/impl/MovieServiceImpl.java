package com.unibuc.fmi.mycinemamvc.services.impl;

import com.unibuc.fmi.mycinemamvc.composed_id.MovieScheduleId;
import com.unibuc.fmi.mycinemamvc.domain.Actor;
import com.unibuc.fmi.mycinemamvc.domain.Movie;
import com.unibuc.fmi.mycinemamvc.domain.MovieSchedule;
import com.unibuc.fmi.mycinemamvc.domain.Room;
import com.unibuc.fmi.mycinemamvc.dto.MovieDto;
import com.unibuc.fmi.mycinemamvc.dto.MovieScheduleDto;
import com.unibuc.fmi.mycinemamvc.exceptions.BadRequestException;
import com.unibuc.fmi.mycinemamvc.exceptions.ResourceNotFoundException;
import com.unibuc.fmi.mycinemamvc.exceptions.UniqueConstraintException;
import com.unibuc.fmi.mycinemamvc.mapper.MovieMapper;
import com.unibuc.fmi.mycinemamvc.repositories.ActorRepository;
import com.unibuc.fmi.mycinemamvc.repositories.MovieRepository;
import com.unibuc.fmi.mycinemamvc.repositories.MovieScheduleRepository;
import com.unibuc.fmi.mycinemamvc.repositories.RoomRepository;
import com.unibuc.fmi.mycinemamvc.services.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final RoomRepository roomRepository;
    private final MovieScheduleRepository movieScheduleRepository;
    private final MovieMapper movieMapper;

    @Override
    public Page<Movie> getMoviesPaginated(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    @Override
    public Movie findById(Long id) {
        Optional<Movie> optionalMovie = movieRepository.findById(id);
        if(optionalMovie.isEmpty()) {
            log.warn("Movie with id " + id + " not found");
            throw new ResourceNotFoundException("Movie with id " + id + " not found!");
        }
        return optionalMovie.get();
    }

    @Override
    public Movie save(MovieDto movieDto) {
        Optional<Movie> optionalMovie = movieRepository.findByName(movieDto.getName());
        if(optionalMovie.isPresent() && !optionalMovie.get().getId().equals(movieDto.getId())) {
            log.warn("There is already a movie with name " + movieDto.getName());
            throw new UniqueConstraintException("There is already a movie with the same name!");
        }

        List<Actor> actors = new ArrayList<>();
        for(Long actorId : movieDto.getActors()) {
            Optional<Actor> optionalActor = actorRepository.findById(actorId);
            if(optionalActor.isEmpty()) {
                log.warn("Actor with id " + actorId + " not found");
                throw new ResourceNotFoundException("Actor with id " + actorId + " not found!");
            }
            actors.add(optionalActor.get());
        }
        Movie movie = movieMapper.mapToMovie(movieDto, actors);
        log.info("Movie saved");

        return movieRepository.save(movie);
    }

    @Override
    public void scheduleMovie(MovieScheduleDto movieScheduleDto) {
        Optional<Movie> optionalMovie = movieRepository.findById(movieScheduleDto.getMovieId());
        if(optionalMovie.isEmpty()) {
            log.warn("Movie with id " + movieScheduleDto.getMovieId() + " not found");
            throw new ResourceNotFoundException("Movie with id " + movieScheduleDto.getMovieId() + " not found!");
        }

        Optional<Room> optionalRoom = roomRepository.findById(movieScheduleDto.getRoomId());
        if(optionalRoom.isEmpty()) {
            log.warn("Room with id " + movieScheduleDto.getRoomId() + " not found");
            throw new ResourceNotFoundException("Room with id " + movieScheduleDto.getRoomId() + " not found!");
        }

        Movie movie = optionalMovie.get();
        LocalDate date = movieScheduleDto.getDate();
        LocalTime hour = movieScheduleDto.getHour();
        LocalDateTime scheduleStartTime = LocalDateTime.of(date, hour);
        LocalDateTime scheduleEndTime = scheduleStartTime.plusMinutes(movie.getDuration());

        if(scheduleStartTime.isBefore(LocalDateTime.now())) {
            log.warn("Schedule in the past is not possible: " + scheduleStartTime);
            throw new BadRequestException("The movie can't be scheduled in a past date!");
        }

        Room room = optionalRoom.get();
        for(MovieSchedule movieSchedule : room.getSchedules()) {
            LocalDateTime startTime = LocalDateTime.of(movieSchedule.getId().getDate(), movieSchedule.getId().getHour());
            LocalDateTime endTime = startTime.plusMinutes(movieSchedule.getMovie().getDuration());
            if(scheduleStartTime.isBefore(endTime) && startTime.isBefore(scheduleEndTime)) {
                log.warn("Room " + room.getName() + " not available on " + startTime);
                throw new BadRequestException("The room is not available at this hour!");
            }
        }

        MovieScheduleId movieScheduleId = MovieScheduleId.builder()
                .movieId(movie.getId())
                .roomId(room.getId())
                .date(date)
                .hour(hour)
                .build();
        MovieSchedule movieSchedule = MovieSchedule.builder()
                .id(movieScheduleId)
                .price(movieScheduleDto.getPrice())
                .movie(movie)
                .room(room)
                .tickets(new ArrayList<>())
                .build();
        movieScheduleRepository.save(movieSchedule);
        log.info("Movie scheduled successfully on " + date + " at " + hour);
    }
}
