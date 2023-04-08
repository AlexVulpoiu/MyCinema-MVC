package com.unibuc.fmi.mycinemamvc.domain;

import com.unibuc.fmi.mycinemamvc.composed_id.MovieScheduleId;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "movies_schedule")
public class MovieSchedule {

    @EmbeddedId
    private MovieScheduleId id;

    private Integer price;

    @MapsId("movieId")
    @ManyToOne
    private Movie movie;

    @MapsId("roomId")
    @ManyToOne
    private Room room;

    @OneToMany(mappedBy = "movieSchedule", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Ticket> tickets = new ArrayList<>();
}
