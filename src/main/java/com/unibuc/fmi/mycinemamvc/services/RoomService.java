package com.unibuc.fmi.mycinemamvc.services;

import com.unibuc.fmi.mycinemamvc.domain.Room;

import java.util.List;

public interface RoomService {
    List<Room> getRooms();
    Room findById(Long id);
    void save(Room room);
}
