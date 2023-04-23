package com.unibuc.fmi.mycinemamvc.composed_id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class MovieScheduleId implements Serializable {

    @NotNull(message = "The movie must be provided!")
    private Long movieId;

    @NotNull(message = "The room must be provided!")
    private Long roomId;

    @NotNull(message = "Schedule date can't be null!")
    @Column(name = "movie_date")
    private LocalDate date;

    @NotNull(message = "Schedule hour can't be null!")
    @Column(name = "movie_hour")
    private LocalTime hour;
}
