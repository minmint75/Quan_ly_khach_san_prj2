package service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import entity.Room;

import java.util.List;
import java.util.Optional;

public interface RoomService {
    List<Room> getAllRooms();
    Optional<Room> getRoomById(String roomId);
    Room saveRoom(Room room);
    Room updateRoom(String roomId, Room updateRoom);
    void deleteRoomById(String roomId);
    List<Room> searchRooms(String roomNumber, String roomType, int roomFloor);
    List<Room> getAvailableRooms();
    Page<Room> getAllRooms(Pageable pageable);
    Page<Room> searchRooms(Pageable pageable, String roomNumber, String roomType, int roomFloor);
}
