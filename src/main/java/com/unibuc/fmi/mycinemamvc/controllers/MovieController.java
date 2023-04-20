package com.unibuc.fmi.mycinemamvc.controllers;

import com.unibuc.fmi.mycinemamvc.domain.Movie;
import com.unibuc.fmi.mycinemamvc.dto.MovieDto;
import com.unibuc.fmi.mycinemamvc.services.ActorService;
import com.unibuc.fmi.mycinemamvc.services.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final ActorService actorService;

    @GetMapping
    public String getMovies(Model model,
                            @RequestParam(name = "page") Optional<Integer> page,
                            @RequestParam(name = "size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<Movie> moviePage = movieService.getMoviesPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("movies", moviePage);
        return "moviesPaginated";
    }

    @GetMapping("/{id}")
    public String getMovie(@PathVariable Long id, Model model) {
        Movie movie = movieService.findById(id);
        model.addAttribute("movie", movie);
        return "movieDetails";
    }

    @GetMapping("/add")
    public String addMovieForm(Model model) {
        model.addAttribute("movie", new MovieDto());
        model.addAttribute("actors", actorService.getActors());
        return "movieForm";
    }

    @PostMapping
    public String saveOrUpdate(@Valid @ModelAttribute("movie") MovieDto movieDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "movieForm";
        }
        Movie savedMovie = movieService.save(movieDto);
        return "redirect:/movies/" + savedMovie.getId();
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("movie", movieService.findById(id));
        model.addAttribute("actors", actorService.getActors());
        return "movieForm";
    }
}
