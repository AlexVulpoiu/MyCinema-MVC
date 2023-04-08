package com.unibuc.fmi.mycinemamvc.domain;

import com.unibuc.fmi.mycinemamvc.enums.EGenre;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String description;

    private Integer duration;

    @Enumerated(EnumType.STRING)
    private EGenre genre;

    @ManyToMany
    @JoinTable(
            name = "actors_movies",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private List<Actor> actors = new ArrayList<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovieSchedule> schedules = new ArrayList<>();
}
