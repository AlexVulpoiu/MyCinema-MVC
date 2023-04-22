package com.unibuc.fmi.mycinemamvc.services;

import com.unibuc.fmi.mycinemamvc.composed_id.MovieScheduleId;
import com.unibuc.fmi.mycinemamvc.domain.MovieSchedule;

public interface MovieScheduleService {
    MovieSchedule findById(MovieScheduleId id);
}
