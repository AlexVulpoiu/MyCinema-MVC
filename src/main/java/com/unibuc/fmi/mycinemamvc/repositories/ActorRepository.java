package com.unibuc.fmi.mycinemamvc.repositories;

import com.unibuc.fmi.mycinemamvc.domain.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {

    Optional<Actor> findByName(String name);
}
