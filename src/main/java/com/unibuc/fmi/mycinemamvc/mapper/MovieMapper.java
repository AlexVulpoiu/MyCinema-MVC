package com.unibuc.fmi.mycinemamvc.mapper;

import com.unibuc.fmi.mycinemamvc.domain.Actor;
import com.unibuc.fmi.mycinemamvc.domain.Movie;
import com.unibuc.fmi.mycinemamvc.dto.MovieDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovieMapper {

    public Movie mapToMovie(MovieDto movieDto, List<Actor> actors) {
        return Movie.builder()
                .id(movieDto.getId())
                .name(movieDto.getName())
                .description(movieDto.getDescription())
                .duration(movieDto.getDuration())
                .genre(movieDto.getGenre())
                .actors(actors)
                .build();
    }
}
