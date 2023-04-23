package com.unibuc.fmi.mycinemamvc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.unibuc.fmi.mycinemamvc.validators.OnlyLetters;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "actors")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Actor name must contain at least one letter!")
    @OnlyLetters
    @Column(unique = true)
    private String name;

    @JsonIgnoreProperties("actors")
    @ManyToMany(mappedBy = "actors")
    private List<Movie> movies = new ArrayList<>();
}
