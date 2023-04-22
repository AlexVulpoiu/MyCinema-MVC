package com.unibuc.fmi.mycinemamvc.services.impl;

import com.unibuc.fmi.mycinemamvc.domain.Room;
import com.unibuc.fmi.mycinemamvc.exceptions.ResourceNotFoundException;
import com.unibuc.fmi.mycinemamvc.exceptions.UniqueConstraintException;
import com.unibuc.fmi.mycinemamvc.repositories.RoomRepository;
import com.unibuc.fmi.mycinemamvc.services.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
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
            log.warn("Room with id " + id + " not found");
            throw new ResourceNotFoundException("Room with id " + id + " not found!");
        }
        return optionalRoom.get();
    }

    @Override
    public void save(Room room) {
        Optional<Room> optionalRoom = roomRepository.findByName(room.getName());
        if(optionalRoom.isPresent() && !optionalRoom.get().getId().equals(room.getId())) {
            log.warn("Room name " + room.getName() + " already used");
            throw new UniqueConstraintException("There is already a room with the same name!");
        }
        roomRepository.save(room);
        log.info("Room saved");
    }
}
