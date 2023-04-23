package com.unibuc.fmi.mycinemamvc.controllers;

import com.unibuc.fmi.mycinemamvc.domain.Movie;
import com.unibuc.fmi.mycinemamvc.enums.EGenre;
import com.unibuc.fmi.mycinemamvc.exceptions.ResourceNotFoundException;
import com.unibuc.fmi.mycinemamvc.services.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MovieControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MovieService movieService;

    @Test
    @WithMockUser(username = "guest", password = "12345", roles = "GUEST")
    public void getByIdSuccessTest() throws Exception {
        Movie movie = Movie.builder()
                .id(1L)
                .name("Movie name")
                .description("Movie description")
                .duration(200)
                .genre(EGenre.COMEDY)
                .schedules(new ArrayList<>())
                .build();

        when(movieService.findById(1L)).thenReturn(movie);

        mockMvc.perform(get("/movies/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("movieDetails"))
                .andExpect(model().attribute("movie", movie))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "guest", password = "12345", roles = "GUEST")
    public void getByIdNotFoundTest() throws Exception {
        when(movieService.findById(5L)).thenThrow(new ResourceNotFoundException("Movie with id 5 not found!"));

        mockMvc.perform(get("/movies/{id}", "5"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("notFoundException"));
    }
}
