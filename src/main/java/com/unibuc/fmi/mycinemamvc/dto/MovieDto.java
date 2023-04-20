package com.unibuc.fmi.mycinemamvc.dto;

import com.unibuc.fmi.mycinemamvc.enums.EGenre;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class MovieDto {

    private Long id;

    @NotBlank(message = "Movie name can't be blank!")
    private String name;

    @NotBlank(message = "Description can't be empty!")
    private String description;

    @NotNull(message = "Movie duration must be provided!")
    @Min(value = 1, message = "Movie duration must be a positive number!")
    private Integer duration;

    @NotNull(message = "Movie genre must be provided!")
    private EGenre genre;

    @NotNull(message = "Actors list must be provided!")
    @Size(min = 1, message = "Actors list can't be empty!")
    private List<Long> actors;
}
