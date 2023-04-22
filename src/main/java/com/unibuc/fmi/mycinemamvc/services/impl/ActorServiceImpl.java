package com.unibuc.fmi.mycinemamvc.services.impl;

import com.unibuc.fmi.mycinemamvc.domain.Actor;
import com.unibuc.fmi.mycinemamvc.exceptions.BadRequestException;
import com.unibuc.fmi.mycinemamvc.exceptions.ResourceNotFoundException;
import com.unibuc.fmi.mycinemamvc.exceptions.UniqueConstraintException;
import com.unibuc.fmi.mycinemamvc.repositories.ActorRepository;
import com.unibuc.fmi.mycinemamvc.services.ActorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActorServiceImpl implements ActorService {

    private final ActorRepository actorRepository;

    @Override
    public List<Actor> getActors() {
        return actorRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    public Actor findById(Long id) {
        Optional<Actor> optionalActor = actorRepository.findById(id);
        if(optionalActor.isEmpty()) {
            log.warn("Actor with id " + id + " not found");
            throw new ResourceNotFoundException("Actor with id " + id + " not found!");
        }
        log.info("Actor with id " + id + " found");
        return optionalActor.get();
    }

    @Override
    public void save(Actor actor) {
        Optional<Actor> optionalActor = actorRepository.findByName(actor.getName());
        if(optionalActor.isPresent() && !optionalActor.get().getId().equals(actor.getId())) {
            log.warn("There is already an actor with the same name");
            throw new UniqueConstraintException("There is already an actor with the same name!");
        }
        actorRepository.save(actor);
        log.info("Actor saved");
    }

    @Override
    public void deleteById(Long id) {
        Optional<Actor> optionalActor = actorRepository.findById(id);
        if(optionalActor.isEmpty()) {
            log.warn("Actor with id " + id + " not found");
            throw new ResourceNotFoundException("Actor with id " + id + " not found!");
        }
        Actor actor = optionalActor.get();
        if(!actor.getMovies().isEmpty()) {
            log.warn("Actor with id " + id + " has roles in some movies");
            throw new BadRequestException("This actor can't be deleted because he has roles in some movies!");
        }

        actorRepository.deleteById(id);
        log.info("Actor deleted");
    }
}
