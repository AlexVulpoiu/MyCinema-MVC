package com.unibuc.fmi.mycinemamvc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.unibuc.fmi.mycinemamvc.validators.OnlyLettersAndDigits;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "rooms")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Room name must contain at least one letter or digit!")
    @OnlyLettersAndDigits
    @Column(unique = true)
    private String name;

    @NotNull
    @Min(value = 1, message = "The cinema room must have minimum 1 row!")
    private Integer numberOfRows;

    @NotNull
    @Min(value = 1, message = "Each row must have minimum 1 seat!")
    private Integer seatsPerRow;

    @JsonIgnoreProperties("room")
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovieSchedule> schedules = new ArrayList<>();
}
