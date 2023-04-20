package com.unibuc.fmi.mycinemamvc.services;

import com.unibuc.fmi.mycinemamvc.domain.Movie;
import com.unibuc.fmi.mycinemamvc.dto.MovieDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieService {

    Page<Movie> getMoviesPaginated(Pageable pageable);
    Movie findById(Long id);
    Movie save(MovieDto movieDto);
}
