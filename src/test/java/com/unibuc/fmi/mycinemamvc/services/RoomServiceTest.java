package com.unibuc.fmi.mycinemamvc.services;

import com.unibuc.fmi.mycinemamvc.domain.Room;
import com.unibuc.fmi.mycinemamvc.repositories.RoomRepository;
import com.unibuc.fmi.mycinemamvc.services.impl.RoomServiceImpl;
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
public class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;
    @InjectMocks
    private RoomServiceImpl roomService;

    @Test
    public void findRoomsTest() {
        Room room = new Room();
        room.setId(1L);
        room.setName("Test room");
        room.setNumberOfRows(20);
        room.setSeatsPerRow(20);
        List<Room> rooms = List.of(room);

        when(roomRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))).thenReturn(rooms);

        List<Room> returnedRooms = roomService.getRooms();

        assertEquals(1, returnedRooms.size());
        verify(roomRepository, times(1)).findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Test
    public void findRoomByIdTest() {
        Room room = new Room();
        room.setId(1L);
        room.setName("Test room");
        room.setNumberOfRows(20);
        room.setSeatsPerRow(20);
        Optional<Room> optionalRoom = Optional.of(room);

        when(roomRepository.findById(1L)).thenReturn(optionalRoom);

        Room returnedRoom = roomService.findById(1L);

        assertEquals(room, returnedRoom);
        verify(roomRepository, times(1)).findById(1L);
    }
}
