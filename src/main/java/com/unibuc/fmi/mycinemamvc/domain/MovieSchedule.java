package com.unibuc.fmi.mycinemamvc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.unibuc.fmi.mycinemamvc.composed_id.MovieScheduleId;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "movies_schedule")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MovieSchedule {

    @EmbeddedId
    private MovieScheduleId id;

    @NotNull(message = "Movie price must be provided!")
    @Min(value = 1, message = "Movie price must be a positive value!")
    private Integer price;

    @MapsId("movieId")
    @ManyToOne
    private Movie movie;

    @MapsId("roomId")
    @ManyToOne
    private Room room;

    @JsonIgnoreProperties("movieSchedule")
    @OneToMany(mappedBy = "movieSchedule", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Ticket> tickets = new ArrayList<>();
}
