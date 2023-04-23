package com.unibuc.fmi.mycinemamvc.services;

import com.unibuc.fmi.mycinemamvc.domain.Actor;
import com.unibuc.fmi.mycinemamvc.repositories.ActorRepository;
import com.unibuc.fmi.mycinemamvc.services.impl.ActorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActorServiceTest {

    @Mock
    private ActorRepository actorRepository;
    @InjectMocks
    private ActorServiceImpl actorService;

    @Test
    public void findActorsTest() {
        Actor actor = new Actor();
        actor.setId(1L);
        actor.setName("Test actor");
        List<Actor> actors = List.of(actor);

        when(actorRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))).thenReturn(actors);

        List<Actor> returnedActors = actorService.getActors();

        assertEquals(1, returnedActors.size());
        verify(actorRepository, times(1)).findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Test
    public void findActorByIdTest() {
        Actor actor = new Actor();
        actor.setId(1L);
        actor.setName("Test actor");
        Optional<Actor> optionalActor = Optional.of(actor);

        when(actorRepository.findById(1L)).thenReturn(optionalActor);

        Actor returnedActor = actorService.findById(1L);

        assertEquals(actor, returnedActor);
        verify(actorRepository, times(1)).findById(1L);
    }

    @Test
    public void deleteActorByIdTest() {
        Actor actor = new Actor();
        actor.setId(1L);
        actor.setName("Test actor");
        Optional<Actor> optionalActor = Optional.of(actor);

        when(actorRepository.findById(1L)).thenReturn(optionalActor);

        actorService.deleteById(1L);

        verify(actorRepository, times(1)).findById(1L);
        verify(actorRepository, times(1)).deleteById(1L);
    }
}
