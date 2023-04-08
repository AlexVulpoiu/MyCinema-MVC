package com.unibuc.fmi.mycinemamvc.composed_id;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Embeddable
public class MovieScheduleId implements Serializable {

    private Long movieId;

    private Long roomId;

    private LocalDate date;

    private LocalTime hour;
}
