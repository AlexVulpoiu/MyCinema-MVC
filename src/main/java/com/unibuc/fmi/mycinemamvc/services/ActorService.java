package com.unibuc.fmi.mycinemamvc.services;

import com.unibuc.fmi.mycinemamvc.domain.Actor;

import java.util.List;

public interface ActorService {
    List<Actor> getActors();
    Actor findById(Long id);
    void save(Actor actor);
    void deleteById(Long id);
}
