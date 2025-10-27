package com.example.qlks_2.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.qlks_2.entity.Room;

public interface RoomService {
    List<Room> getAllRooms();
    Optional<Room> getRoomById(Long roomId);
    Room saveRoom(Room room);
    Room updateRoom(Long roomId, Room updateRoom);
    void deleteRoomById(Long roomId);
    List<Room> searchRooms(String roomNumber, Room.RoomType roomType, int roomFloor);
    List<Room> getAvailableRooms();
    Page<Room> getAllRooms(Pageable pageable);
    Page<Room> searchRooms(Pageable pageable, String roomNumber, Room.RoomType roomType, int roomFloor);
}
