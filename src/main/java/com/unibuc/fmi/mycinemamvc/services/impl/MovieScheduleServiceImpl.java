package com.unibuc.fmi.mycinemamvc.services.impl;

import com.unibuc.fmi.mycinemamvc.composed_id.MovieScheduleId;
import com.unibuc.fmi.mycinemamvc.domain.MovieSchedule;
import com.unibuc.fmi.mycinemamvc.exceptions.BadRequestException;
import com.unibuc.fmi.mycinemamvc.repositories.MovieScheduleRepository;
import com.unibuc.fmi.mycinemamvc.services.MovieScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieScheduleServiceImpl implements MovieScheduleService {

    private final MovieScheduleRepository movieScheduleRepository;

    @Override
    public MovieSchedule findById(MovieScheduleId id) {
        Optional<MovieSchedule> optionalMovieSchedule = movieScheduleRepository.findById(id);
        if(optionalMovieSchedule.isEmpty()) {
            log.warn("Movie schedule not found");
            throw new BadRequestException("The selected movie doesn't exist or it's not scheduled at the requested time!");
        }
        return optionalMovieSchedule.get();
    }
}
