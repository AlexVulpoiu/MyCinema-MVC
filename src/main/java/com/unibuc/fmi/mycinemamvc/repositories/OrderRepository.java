package com.unibuc.fmi.mycinemamvc.repositories;

import com.unibuc.fmi.mycinemamvc.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
