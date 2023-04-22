package com.unibuc.fmi.mycinemamvc.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class MovieScheduleDto {

    @NotNull(message = "The movie must be provided!")
    private Long movieId;

    @NotNull(message = "The room must be provided!")
    private Long roomId;

    @NotNull(message = "Schedule date can't be null!")
    @FutureOrPresent(message = "Schedule date can't be a past date!")
    private LocalDate date;

    @NotNull(message = "Schedule hour can't be null!")
    private LocalTime hour;

    @NotNull(message = "Movie price must be provided!")
    @Min(value = 1, message = "Movie price must be a positive value!")
    private Integer price;
}
