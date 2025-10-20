package com.example.qlks_2.dto;

import java.math.BigDecimal;
import org.springframework.web.multipart.MultipartFile;

import com.example.qlks_2.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequest {

    private Long roomId;
    private String roomNumber;
    private Room.RoomType roomType;
    private int roomFloor;
    private BigDecimal roomPrice;
    private String roomAmenities;
    private Room.RoomStatus roomStatus;
    private MultipartFile imageFile;

    public Room toEntity() {
        Room room = new Room();
        room.setRoomId(this.roomId);
        room.setRoomNumber(this.roomNumber);
        room.setRoomType(this.roomType);
        room.setRoomFloor(this.roomFloor);
        room.setRoomPrice(this.roomPrice);
        room.setRoomAmenities(this.roomAmenities);
        room.setRoomStatus(this.roomStatus);
        return room;
    }

    public static RoomRequest fromEntity(Room room) {
        if (room == null) return null;

        RoomRequest request = new RoomRequest();
        request.setRoomId(room.getRoomId());
        request.setRoomNumber(room.getRoomNumber());
        request.setRoomType(room.getRoomType());
        request.setRoomFloor(room.getRoomFloor());
        request.setRoomPrice(room.getRoomPrice());
        request.setRoomAmenities(room.getRoomAmenities());
        request.setRoomStatus(room.getRoomStatus());
        return request;
    }

    public boolean hasImageFile() {
        return imageFile != null && !imageFile.isEmpty();
    }

    public boolean hasRoomId() {
        return roomId != null && roomId > 0;
    }

    public boolean hasRoomNumber() {
        return roomNumber != null && !roomNumber.trim().isEmpty();
    }

    public boolean hasRoomType() {
        return roomType != null;
    }

    public boolean hasRoomStatus() {
        return roomStatus != null;
    }

    public boolean hasRoomFloor() {
        return roomFloor > 0;
    }

    public boolean hasRoomPrice() {
        return roomPrice != null
                && roomPrice.compareTo(BigDecimal.ZERO) > 0
                && roomPrice.compareTo(new BigDecimal("999999.99")) <= 0;
    }
}
