package controller;

import dto.RoomRequest;
import entity.Room;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.FileUploadService;
import service.RoomService;

import java.util.List;
import java.util.Optional;

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
    public String viewRoom(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
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
    public Room getRoomApi(@PathVariable String id) {
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
    public String addRoom(@ModelAttribute("roomRequest") RoomRequest roomRequest,
                          @RequestParam("imageFile") MultipartFile imageFile,
                          BindingResult bindingResult,
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
            if (!imageFile.isEmpty()) {
                String imageUrl = fileUploadService.storeFile(imageFile);
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
    public String showEditForm(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
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
    public String updateRoom(@PathVariable String id,
                             @ModelAttribute("roomRequest") RoomRequest roomRequest,
                             @RequestParam("imageFile") MultipartFile imageFile,
                             BindingResult bindingResult,
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
            if (!imageFile.isEmpty()) {
                String imageUrl = fileUploadService.storeFile(imageFile);
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
    public String deleteRoom(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            roomService.deleteRoomById(id);
            redirectAttributes.addFlashAttribute("success", "Xóa phòng thành công!");
        } catch (Exception e) {
            log.error("Lỗi khi xóa phòng ID {}: ", id, e);
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa phòng!");
        }
        return "redirect:/rooms";
    }
}