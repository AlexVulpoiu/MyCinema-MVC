package com.unibuc.fmi.mycinemamvc.controllers;

import com.unibuc.fmi.mycinemamvc.domain.Movie;
import com.unibuc.fmi.mycinemamvc.enums.EGenre;
import com.unibuc.fmi.mycinemamvc.exceptions.ResourceNotFoundException;
import com.unibuc.fmi.mycinemamvc.services.ActorService;
import com.unibuc.fmi.mycinemamvc.services.MovieService;
import com.unibuc.fmi.mycinemamvc.services.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieControllerTest {

    @Mock
    private Model model;
    @Mock
    private ActorService actorService;
    @Mock
    private MovieService movieService;
    @Mock
    private RoomService roomService;
    private MovieController movieController;
    @Captor
    private ArgumentCaptor<Movie> argumentCaptor;

    @BeforeEach
    public void setup() {
        movieController = new MovieController(movieService, actorService, roomService);
    }

    @Test
    public void getByIdSuccessTest() {
        Movie movie = Movie.builder()
                .id(1L)
                .name("Movie name")
                .description("Movie description")
                .duration(200)
                .genre(EGenre.COMEDY)
                .schedules(new ArrayList<>())
                .build();

        when(movieService.findById(1L)).thenReturn(movie);

        String viewName = movieController.getMovie(1L, model);
        assertEquals("movieDetails", viewName);
        verify(movieService, times(1)).findById(1L);
        verify(model, times(1)).addAttribute(eq("movie"), argumentCaptor.capture());
        Movie movieArgument = argumentCaptor.getValue();
        assertEquals(movie.getId(), movieArgument.getId());
    }

    @Test
    public void getByIdNotFoundTest() {
        when(movieService.findById(1L)).thenThrow(new ResourceNotFoundException("Movie with id 1 not found!"));
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> movieController.getMovie(1L, model));
        assertEquals("Movie with id 1 not found!", exception.getMessage());
    }
}
