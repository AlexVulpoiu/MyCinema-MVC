package com.unibuc.fmi.mycinemamvc.services;

import com.unibuc.fmi.mycinemamvc.domain.Order;
import com.unibuc.fmi.mycinemamvc.dto.OrderDto;

import java.util.List;

public interface OrderService {
    void save(OrderDto orderDto);
    List<Order> findOrdersForUser(String username);
}
