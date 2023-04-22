package com.unibuc.fmi.mycinemamvc.repositories;

import com.unibuc.fmi.mycinemamvc.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

}
