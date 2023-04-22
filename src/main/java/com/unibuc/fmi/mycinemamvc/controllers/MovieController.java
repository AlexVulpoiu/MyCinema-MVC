package com.unibuc.fmi.mycinemamvc.controllers;

import com.unibuc.fmi.mycinemamvc.domain.Movie;
import com.unibuc.fmi.mycinemamvc.domain.MovieSchedule;
import com.unibuc.fmi.mycinemamvc.dto.MovieDto;
import com.unibuc.fmi.mycinemamvc.dto.MovieScheduleDto;
import com.unibuc.fmi.mycinemamvc.services.ActorService;
import com.unibuc.fmi.mycinemamvc.services.MovieService;
import com.unibuc.fmi.mycinemamvc.services.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final ActorService actorService;
    private final RoomService roomService;

    @GetMapping({"", "/", "/movies"})
    public String getMovies(Model model,
                            @RequestParam(name = "page") Optional<Integer> page,
                            @RequestParam(name = "size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<Movie> moviePage = movieService.getMoviesPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("movies", moviePage);
        return "moviesPaginated";
    }

    @GetMapping("/movies/{id}")
    public String getMovie(@PathVariable Long id, Model model) {
        Movie movie = movieService.findById(id);
        List<MovieSchedule> movieSchedules = movie.getSchedules().stream()
                .sorted(Comparator.comparing(s -> s.getId().getDate())).collect(Collectors.toList());
        model.addAttribute("movie", movie);
        model.addAttribute("movieSchedules", movieSchedules);
        return "movieDetails";
    }

    @GetMapping("/movies/add")
    public String addMovieForm(Model model) {
        model.addAttribute("movie", new MovieDto());
        model.addAttribute("actors", actorService.getActors());
        return "movieForm";
    }

    @PostMapping("/movies")
    public String saveOrUpdate(@Valid @ModelAttribute("movie") MovieDto movieDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "movieForm";
        }
        Movie savedMovie = movieService.save(movieDto);
        return "redirect:/movies/" + savedMovie.getId();
    }

    @GetMapping("/movies/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("movie", movieService.findById(id));
        model.addAttribute("actors", actorService.getActors());
        return "movieForm";
    }

    @GetMapping("/movies/schedule/{id}")
    public String scheduleMovieForm(@PathVariable Long id, Model model) {
        Movie movie = movieService.findById(id);
        model.addAttribute("movieScheduleDto", MovieScheduleDto.builder().movieId(movie.getId()).build());
        model.addAttribute("movieName", movie.getName());
        model.addAttribute("rooms", roomService.getRooms());
        return "movieScheduleForm";
    }

    @PostMapping("/movies/schedule")
    public String scheduleMovie(@Valid @ModelAttribute MovieScheduleDto movieScheduleDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "movieScheduleForm";
        }
        movieService.scheduleMovie(movieScheduleDto);
        return "redirect:/movies/" + movieScheduleDto.getMovieId();
    }
}
