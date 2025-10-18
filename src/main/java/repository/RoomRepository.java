package repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {

    List<Room> findByRoomNumberIgnoreCase(String roomNumber);

    List<Room> findByRoomTypeIgnoreCase(String roomType);

    List<Room> findByRoomFloor(int roomFloor);

    @Query("SELECT r FROM Room r WHERE r.status = 'AVAILABLE' ORDER BY r.roomId, r.roomNumber")
    List <Room> findAvailableRoom();

    @Query("SELECT r FROM Room r WHERE " +
            "(:roomNumber IS NULL OR r.roomNumber = :roomNumber) AND " +
            "(:roomType IS NULL OR r.roomType = :roomType) AND " +
            "(:roomFloor IS NULL OR r.roomFloor = :roomFloor) " +
            "ORDER BY r.roomId DESC")
    List<Room> findByFilters(@Param("roomNumber") String roomNumber,
                             @Param("roomType") String roomType,
                             @Param("roomFloor") Integer roomFloor);

    @Query("SELECT r FROM Room r ORDER BY r.roomId DESC")
    List<Room> findAllRoomsByIdDesc();

    Page<Room> findAll(Pageable pageable);

    @Query("SELECT r FROM Room r WHERE " +
            "(:roomNumber IS NULL OR r.roomNumber = :roomNumber) AND " +
            "(:roomType IS NULL OR r.roomType = :roomType) AND " +
            "(:roomFloor IS NULL OR r.roomFloor = :roomFloor)")
    Page<Room> findByFiltersPageable(@Param("roomNumber") String roomNumber,
                                     @Param("roomType") String roomType,
                                     @Param("roomFloor") Integer roomFloor,
                                     Pageable pageable);                               

}
