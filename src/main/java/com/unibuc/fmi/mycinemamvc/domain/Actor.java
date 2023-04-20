package com.unibuc.fmi.mycinemamvc.domain;

import com.unibuc.fmi.mycinemamvc.validators.OnlyLetters;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "actors")
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Actor name must contain at least one letter!")
    @OnlyLetters
    @Column(unique = true)
    private String name;
}
