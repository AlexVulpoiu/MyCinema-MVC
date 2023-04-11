package com.unibuc.fmi.mycinemamvc.services.impl;

import com.unibuc.fmi.mycinemamvc.domain.Room;
import com.unibuc.fmi.mycinemamvc.exceptions.ResourceNotFoundException;
import com.unibuc.fmi.mycinemamvc.repositories.RoomRepository;
import com.unibuc.fmi.mycinemamvc.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public List<Room> getRooms() {
        return roomRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    public Room findById(Long id) {
        Optional<Room> optionalRoom = roomRepository.findById(id);
        if(optionalRoom.isEmpty()) {
            throw new ResourceNotFoundException("Room with id " + id + " not found!");
        }
        return optionalRoom.get();
    }

    @Override
    public void save(Room room) {
        roomRepository.save(room);
    }
}
