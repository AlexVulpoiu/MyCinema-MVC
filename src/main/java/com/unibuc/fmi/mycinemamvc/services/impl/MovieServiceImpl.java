package com.unibuc.fmi.mycinemamvc.services.impl;

import com.unibuc.fmi.mycinemamvc.domain.Actor;
import com.unibuc.fmi.mycinemamvc.domain.Movie;
import com.unibuc.fmi.mycinemamvc.dto.MovieDto;
import com.unibuc.fmi.mycinemamvc.exceptions.ResourceNotFoundException;
import com.unibuc.fmi.mycinemamvc.exceptions.UniqueConstraintException;
import com.unibuc.fmi.mycinemamvc.mapper.MovieMapper;
import com.unibuc.fmi.mycinemamvc.repositories.ActorRepository;
import com.unibuc.fmi.mycinemamvc.repositories.MovieRepository;
import com.unibuc.fmi.mycinemamvc.services.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final MovieMapper movieMapper;

    @Override
    public Page<Movie> getMoviesPaginated(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    @Override
    public Movie findById(Long id) {
        Optional<Movie> optionalMovie = movieRepository.findById(id);
        if(optionalMovie.isEmpty()) {
            throw new ResourceNotFoundException("Movie with id " + id + " not found!");
        }
        return optionalMovie.get();
    }

    @Override
    public Movie save(MovieDto movieDto) {
        Optional<Movie> optionalMovie = movieRepository.findByName(movieDto.getName());
        if(optionalMovie.isPresent() && !optionalMovie.get().getId().equals(movieDto.getId())) {
            throw new UniqueConstraintException("There is already a movie with the same name!");
        }

        List<Actor> actors = new ArrayList<>();
        for(Long actorId : movieDto.getActors()) {
            Optional<Actor> optionalActor = actorRepository.findById(actorId);
            if(optionalActor.isEmpty()) {
                throw new ResourceNotFoundException("Actor with id " + actorId + " not found!");
            }
            actors.add(optionalActor.get());
        }
        Movie movie = movieMapper.mapToMovie(movieDto, actors);

        return movieRepository.save(movie);
    }
}
