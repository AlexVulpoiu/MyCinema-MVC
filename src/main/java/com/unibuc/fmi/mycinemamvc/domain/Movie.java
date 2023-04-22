package com.unibuc.fmi.mycinemamvc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.unibuc.fmi.mycinemamvc.enums.EGenre;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movies")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Movie name can't be blank!")
    @Column(unique = true)
    private String name;

    @NotBlank(message = "Description can't be empty!")
    private String description;

    @NotNull(message = "Movie duration must be provided!")
    @Min(value = 1, message = "Movie duration must be a positive number!")
    private Integer duration;

    @NotNull(message = "Movie genre must be provided!")
    @Enumerated(EnumType.STRING)
    private EGenre genre;

    @JsonIgnoreProperties("movies")
    @NotNull(message = "Actors list must be provided!")
    @Size(min = 1, message = "Actors list can't be empty!")
    @ManyToMany
    @JoinTable(
            name = "actors_movies",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private List<Actor> actors = new ArrayList<>();

    @JsonIgnoreProperties("movie")
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovieSchedule> schedules = new ArrayList<>();
}
