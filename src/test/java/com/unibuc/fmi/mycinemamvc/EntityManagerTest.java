package com.unibuc.fmi.mycinemamvc;

import com.unibuc.fmi.mycinemamvc.domain.Actor;
import com.unibuc.fmi.mycinemamvc.domain.Room;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("h2")
public class EntityManagerTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    public void findRoomTest() {
        Room room = entityManager.find(Room.class, 1L);
        assertEquals("Room 1", room.getName());
    }

    @Test
    public void updateRoomTest() {
        Room room = entityManager.find(Room.class, 1L);
        room.setName("Room 1 edit");
        entityManager.persist(room);
        room = entityManager.find(Room.class, 1L);
        assertEquals("Room 1 edit", room.getName());
        entityManager.flush();
    }

    @Test
    public void findActorTest() {
        Actor actor = entityManager.find(Actor.class, 2L);
        assertEquals("Actor two", actor.getName());
    }

    @Test
    public void updateActorTest() {
        Actor actor = entityManager.find(Actor.class, 2L);
        actor.setName("Actor two edit");
        entityManager.persist(actor);
        assertEquals("Actor two edit", actor.getName());
        entityManager.flush();
    }
}
