package com.example.qlks_2.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.qlks_2.entity.Room;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.qlks_2.repository.RoomRepository;
import com.example.qlks_2.service.RoomService;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional

public class RoomServiceImpl implements RoomService {
    
    private final RoomRepository roomRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Room> getAllRooms() {
        log.info("Lấy danh sách tất cả phòng");
        return roomRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Room> getRoomById(Long roomId) {
        log.info("Lấy thông tin phòng với ID: {}", roomId);
        return roomRepository.findById(roomId);
    }

    @Override
    public Room saveRoom(Room room) {
        log.info("Thêm phòng mới: {}", room);
        // ensure unique roomNumber
        if (room.getRoomNumber() != null && !room.getRoomNumber().trim().isEmpty()) {
            boolean exists = !roomRepository.findByRoomNumberIgnoreCase(room.getRoomNumber().trim()).isEmpty();
            if (exists) {
                throw new DataIntegrityViolationException("Room number already exists");
            }
        }
        return roomRepository.save(room);
    }

    @Override
    public Room updateRoom(Long roomId, Room updateRoom) {
        log.info("Cập nhật phòng có ID: " + roomId);
        return roomRepository.saveAndFlush(updateRoom);
    }

    @Override
    public void deleteRoomById(Long roomId) {
        log.info("Xóa phòng với ID: {}", roomId);
        roomRepository.deleteById(roomId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Room> searchRooms(String roomNumber, Room.RoomType roomType, int roomFloor) {
        String normalizedRoomNumber = (roomNumber != null && !roomNumber.trim().isEmpty()) ? roomNumber.trim() : null;

        log.info("Tìm kiếm phòng - roomNumber: {}, roomType: {}, roomFloor: {}", normalizedRoomNumber, roomType, roomFloor);
        return roomRepository.findByFilters(normalizedRoomNumber, roomType, roomFloor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Room> getAvailableRooms() {
        log.info("Lấy danh sách phòng trống");
        return roomRepository.findAvailableRoom();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Room> getAllRooms(Pageable pageable) {
        log.info("Lấy danh sách phòng với phân trang: {}", pageable);
        return roomRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Room> searchRooms(Pageable pageable, String roomNumber, Room.RoomType roomType, int roomFloor) {
        String normalizedRoomNumber = (roomNumber != null && !roomNumber.trim().isEmpty()) ? roomNumber.trim() : null;

        log.info("Tìm kiếm phòng với phân trang - Số phòng: {}, Loại phòng: {}, Tầng phòng: {}, Phân trang: {}",
                normalizedRoomNumber, roomType, roomFloor, pageable);
        return roomRepository.findByFiltersPageable(normalizedRoomNumber, roomType, roomFloor, pageable);
    }
}
