package com.example.qlks_2.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.qlks_2.dto.RoomRequest;
import com.example.qlks_2.entity.Room;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.qlks_2.service.FileUploadService;
import com.example.qlks_2.service.RoomService;

@Slf4j
@Controller
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final FileUploadService fileUploadService;

    // === API LIST ===
    @GetMapping("/api/list")
    @ResponseBody
    public List<Room> getRoomApi(
            @RequestParam(value = "roomNumber", required = false) String roomNumber,
            @RequestParam(value = "roomType", required = false) Room.RoomType roomType,
            @RequestParam(value = "roomFloor", required = false, defaultValue = "0") int roomFloor) {

        if (roomNumber != null || roomType != null || roomFloor > 0) {
            return roomService.searchRooms(roomNumber, roomType, roomFloor);
        }
        return roomService.getAllRooms();
    }

    // === LIST VIEW ===
    @GetMapping
    public String listRooms(Model model) {
        List<Room> rooms = roomService.getAllRooms();
        model.addAttribute("rooms", rooms);
        model.addAttribute("roomTypes", Room.RoomType.values());
        model.addAttribute("roomStatuses", Room.RoomStatus.values());
        return "room/list"; // Thymeleaf view name
    }

    // === VIEW DETAIL ===
    @GetMapping("/{id}")
    public String viewRoom(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Room> room = roomService.getRoomById(id);

        if (room.isPresent()) {
            model.addAttribute("room", room.get());
            return "room/detail"; // Thymeleaf view name
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy phòng với ID: " + id);
            return "redirect:/rooms";
        }
    }
    
    // === API DETAIL ===
    @GetMapping("/api/{id}")
    @ResponseBody
    public Room getRoomApi(@PathVariable Long id) {
        return roomService.getRoomById(id).orElse(null);
    }


    // === ADD FORM ===
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("roomRequest", new RoomRequest());
        model.addAttribute("roomTypes", Room.RoomType.values());
        model.addAttribute("roomStatuses", Room.RoomStatus.values());
        return "room/form"; // Thymeleaf view name
    }

    // === ADD HANDLER ===
    @PostMapping("/add")
    public String addRoom(@Valid @ModelAttribute("roomRequest") RoomRequest roomRequest,
                          BindingResult bindingResult,
                          @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                          Model model,
                          RedirectAttributes redirectAttributes) {

        log.info("Xử lý thêm phòng mới: {}", roomRequest);

        if (bindingResult.hasErrors()) {
            model.addAttribute("roomTypes", Room.RoomType.values());
            model.addAttribute("roomStatuses", Room.RoomStatus.values());
            return "room/form";
        }

        try {
            Room room = roomRequest.toEntity();

            // Handle file upload
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = fileUploadService.uploadFile(imageFile);
                room.setImageUrl(imageUrl);
            }

            roomService.saveRoom(room);
            redirectAttributes.addFlashAttribute("success", "Thêm phòng thành công!");
            return "redirect:/rooms";
        } catch (Exception e) {
            log.error("Lỗi khi thêm phòng: ", e);
            model.addAttribute("error", "Có lỗi xảy ra khi thêm phòng!");
            model.addAttribute("roomTypes", Room.RoomType.values());
            model.addAttribute("roomStatuses", Room.RoomStatus.values());
            return "room/form";
        }
    }

    // === EDIT FORM ===
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Room> room = roomService.getRoomById(id);

        if (room.isPresent()) {
            RoomRequest roomRequest = RoomRequest.fromEntity(room.get());
            model.addAttribute("roomRequest", roomRequest);
            model.addAttribute("roomTypes", Room.RoomType.values());
            model.addAttribute("roomStatuses", Room.RoomStatus.values());
            return "room/form";
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy phòng với ID: " + id);
            return "redirect:/rooms";
        }
    }

    // === EDIT HANDLER ===
    @PostMapping("/edit/{id}")
    public String updateRoom(@PathVariable Long id,
                             @Valid @ModelAttribute("roomRequest") RoomRequest roomRequest,
                             BindingResult bindingResult,
                             @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        log.info("Cập nhật phòng ID {}: {}", id, roomRequest);

        if (bindingResult.hasErrors()) {
            model.addAttribute("roomTypes", Room.RoomType.values());
            model.addAttribute("roomStatuses", Room.RoomStatus.values());
            return "room/form";
        }

        try {
            Room room = roomRequest.toEntity();
            room.setRoomId(id);

            // Handle file upload
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = fileUploadService.uploadFile(imageFile);
                room.setImageUrl(imageUrl);
            } else {
                // Keep the old image if no new one is uploaded
                roomService.getRoomById(id).ifPresent(existingRoom -> room.setImageUrl(existingRoom.getImageUrl()));
            }

            roomService.updateRoom(id, room);
            redirectAttributes.addFlashAttribute("success", "Cập nhật phòng thành công!");
            return "redirect:/rooms";
        } catch (Exception e) {
            log.error("Lỗi khi cập nhật phòng ID {}: ", id, e);
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật phòng!");
            model.addAttribute("roomTypes", Room.RoomType.values());
            model.addAttribute("roomStatuses", Room.RoomStatus.values());
            return "room/form";
        }
    }

    // === DELETE ===
    @PostMapping("/delete/{id}")
    public String deleteRoom(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            roomService.deleteRoomById(id);
            redirectAttributes.addFlashAttribute("success", "Xóa phòng thành công!");
        } catch (Exception e) {
            log.error("Lỗi khi xóa phòng ID {}: ", id, e);
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa phòng!");
        }
        return "redirect:/rooms";
    }

    // === API ADD (JSON/multipart) ===
    @PostMapping(value = "/api/add", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @ResponseBody
    public ResponseEntity<?> apiAddRoom(
            @ModelAttribute RoomRequest roomRequest,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            Room room = roomRequest.toEntity();
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = fileUploadService.uploadFile(imageFile);
                room.setImageUrl(imageUrl);
            }
            Room saved = roomService.saveRoom(room);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            log.warn("Duplicate roomNumber", e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Room number already exists");
        } catch (Exception e) {
            log.error("API add room error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add room");
        }
    }

    // === API EDIT (JSON/multipart) ===
    @PostMapping(value = "/api/edit/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @ResponseBody
    public ResponseEntity<?> apiEditRoom(
            @PathVariable Long id,
            @ModelAttribute RoomRequest roomRequest,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            Room room = roomRequest.toEntity();
            room.setRoomId(id);
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = fileUploadService.uploadFile(imageFile);
                room.setImageUrl(imageUrl);
            } else {
                roomService.getRoomById(id).ifPresent(existing -> room.setImageUrl(existing.getImageUrl()));
            }
            Room saved = roomService.updateRoom(id, room);
            return ResponseEntity.ok(saved);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            log.warn("Duplicate roomNumber on update", e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Room number already exists");
        } catch (Exception e) {
            log.error("API edit room error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update room");
        }
    }

    // === API DELETE ===
    @PostMapping("/api/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> apiDeleteRoom(@PathVariable Long id) {
        try {
            roomService.deleteRoomById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("API delete room error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete room");
        }
    }
}
