package com.example.qlks_2.controller;

import com.example.qlks_2.dto.BookingRequest;
import com.example.qlks_2.entity.Booking;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import com.example.qlks_2.service.BookingService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // === API LIST ===
    @GetMapping("/api/list")
    @ResponseBody
    public List<Booking> getBookingApi(
            @RequestParam(value = "roomId", required = false) Long roomId,
            @RequestParam(value = "customerId", required = false) Long customerId,
            @RequestParam(value = "status", required = false) Booking.BookingStatus status) {

        if (roomId != null || customerId != null || status != null) {
            return bookingService.searchBooking(roomId, customerId, status);
        }
        return bookingService.getAllBookings();
    }

    // === LIST VIEW ===
    @GetMapping
    public String listBookings(Model model) {
        List<Booking> bookings = bookingService.getAllBookings();
        model.addAttribute("bookings", bookings);
        model.addAttribute("statuses", Booking.BookingStatus.values());
        return "booking/list";
    }

    // === VIEW DETAIL ===
    @GetMapping("/{id}")
    public String viewBooking(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Booking> booking = bookingService.getBookingById(id);

        if (booking.isPresent()) {
            model.addAttribute("booking", booking.get());
            return "booking/detail";
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy booking với ID: " + id);
            return "redirect:/booking";
        }
    }

    // === API DETAIL ===
    @GetMapping("/api/{id}")
    @ResponseBody
    public Booking getBookingApi(@PathVariable Long id) {
        return bookingService.getBookingById(id).orElse(null);
    }

    // === ADD FORM ===
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("bookingRequest", new BookingRequest());
        model.addAttribute("statuses", Booking.BookingStatus.values());
        return "booking/form";
    }

    // === ADD HANDLER (Web Form) ===
    @PostMapping("/add")
    public String addBooking(@ModelAttribute BookingRequest bookingRequest,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        log.info("Thêm booking mới: {}", bookingRequest);

        // Validate ngày
        if (bookingRequest.getCheckIn() == null || bookingRequest.getCheckOut() == null) {
            bindingResult.rejectValue("checkIn", "error.checkIn", "Ngày check-in và check-out không được để trống");
        } else if (!bookingRequest.getCheckOut().isAfter(bookingRequest.getCheckIn())) {
            bindingResult.rejectValue("checkOut", "error.checkOut", "Ngày check-out phải sau ngày check-in");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", Booking.BookingStatus.values());
            return "booking/form";
        }

        try {
            Booking booking = bookingRequest.toEntity();
            bookingService.saveBooking(booking);
            redirectAttributes.addFlashAttribute("success", "Thêm booking thành công!");
            return "redirect:/booking";
        } catch (Exception e) {
            log.error("Lỗi khi thêm booking: ", e);
            model.addAttribute("error", "Có lỗi xảy ra khi thêm booking!");
            model.addAttribute("statuses", Booking.BookingStatus.values());
            return "booking/form";
        }
    }

    // === ADD API HANDLER (JSON) ===
    @PostMapping("/api/add")
    @ResponseBody
    public ResponseEntity<?> addBookingApi(@RequestBody BookingRequest bookingRequest) {
        try {
            log.info("API: Nhận request thêm booking mới: {}", bookingRequest);

            // Validate required fields
            if (bookingRequest.getCustomerId() == null || bookingRequest.getCustomerId() <= 0) {
                log.error("CustomerId không hợp lệ: {}", bookingRequest.getCustomerId());
                Map<String, String> error = new HashMap<>();
                error.put("error", "Validation failed");
                error.put("message", "Mã khách hàng không hợp lệ");
                return ResponseEntity.badRequest().body(error);
            }
            
            if (bookingRequest.getRoomId() == null || bookingRequest.getRoomId() <= 0) {
                log.error("RoomId không hợp lệ: {}", bookingRequest.getRoomId());
                Map<String, String> error = new HashMap<>();
                error.put("error", "Validation failed");
                error.put("message", "Mã phòng không hợp lệ");
                return ResponseEntity.badRequest().body(error);
            }

            // Validate ngày
            if (bookingRequest.getCheckIn() == null || bookingRequest.getCheckOut() == null) {
                log.error("Ngày check-in hoặc check-out bị null: checkIn={}, checkOut={}", 
                         bookingRequest.getCheckIn(), bookingRequest.getCheckOut());
                Map<String, String> error = new HashMap<>();
                error.put("error", "Validation failed");
                error.put("message", "Ngày check-in và check-out không được để trống");
                return ResponseEntity.badRequest().body(error);
            }
            
            if (!bookingRequest.getCheckOut().isAfter(bookingRequest.getCheckIn())) {
                log.error("Ngày check-out không hợp lệ: checkIn={}, checkOut={}", 
                         bookingRequest.getCheckIn(), bookingRequest.getCheckOut());
                Map<String, String> error = new HashMap<>();
                error.put("error", "Validation failed");
                error.put("message", "Ngày check-out phải sau ngày check-in");
                return ResponseEntity.badRequest().body(error);
            }

            // Convert to entity and save
            Booking booking = bookingRequest.toEntity();
            log.info("Entity được tạo: {}", booking);
            
            Booking savedBooking = bookingService.saveBooking(booking);
            log.info("Booking đã được lưu thành công với ID: {}", savedBooking.getBookingId());
            return ResponseEntity.ok(savedBooking);
            
        } catch (Exception e) {
            log.error("Lỗi khi thêm booking qua API: ", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Internal server error");
            error.put("message", "Có lỗi xảy ra khi thêm booking: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // === EDIT FORM ===
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Booking> booking = bookingService.getBookingById(id);

        if (booking.isPresent()) {
            BookingRequest bookingRequest = BookingRequest.fromEntity(booking.get());
            model.addAttribute("bookingRequest", bookingRequest);
            model.addAttribute("statuses", Booking.BookingStatus.values());
            return "booking/form";
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy booking với ID: " + id);
            return "redirect:/booking";
        }
    }

    // === EDIT HANDLER (Web Form) ===
    @PostMapping("/edit/{id}")
    public String updateBooking(@PathVariable Long id,
                                @ModelAttribute BookingRequest bookingRequest,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        log.info("Cập nhật booking ID {}: {}", id, bookingRequest);

        if (bookingRequest.getCheckIn() == null || bookingRequest.getCheckOut() == null) {
            bindingResult.rejectValue("checkIn", "error.checkIn", "Ngày check-in và check-out không được để trống");
        } else if (!bookingRequest.getCheckOut().isAfter(bookingRequest.getCheckIn())) {
            bindingResult.rejectValue("checkOut", "error.checkOut", "Ngày check-out phải sau ngày check-in");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", Booking.BookingStatus.values());
            return "booking/form";
        }

        try {
            Booking booking = bookingRequest.toEntity();
            booking.setBookingId(id);
            bookingService.saveBooking(booking);
            redirectAttributes.addFlashAttribute("success", "Cập nhật booking thành công!");
            return "redirect:/booking";
        } catch (Exception e) {
            log.error("Lỗi khi cập nhật booking ID {}: ", id, e);
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật booking!");
            model.addAttribute("statuses", Booking.BookingStatus.values());
            return "booking/form";
        }
    }

    // === EDIT API HANDLER (JSON) ===
    @PutMapping("/api/{id}")
    @ResponseBody
    public Booking updateBookingApi(@PathVariable Long id, @RequestBody BookingRequest bookingRequest) {
        log.info("API: Cập nhật booking ID {}: {}", id, bookingRequest);

        // Validate ngày
        if (bookingRequest.getCheckIn() == null || bookingRequest.getCheckOut() == null) {
            throw new IllegalArgumentException("Ngày check-in và check-out không được để trống");
        }
        if (!bookingRequest.getCheckOut().isAfter(bookingRequest.getCheckIn())) {
            throw new IllegalArgumentException("Ngày check-out phải sau ngày check-in");
        }

        try {
            Booking booking = bookingRequest.toEntity();
            booking.setBookingId(id);
            Booking updatedBooking = bookingService.saveBooking(booking);
            log.info("Booking ID {} đã được cập nhật", id);
            return updatedBooking;
        } catch (Exception e) {
            log.error("Lỗi khi cập nhật booking ID {} qua API: ", id, e);
            throw new RuntimeException("Có lỗi xảy ra khi cập nhật booking: " + e.getMessage());
        }
    }

    // === DELETE (Web Form) ===
    @PostMapping("/delete/{id}")
    public String deleteBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookingService.deleteBookingById(id);
            redirectAttributes.addFlashAttribute("success", "Xóa booking thành công!");
        } catch (Exception e) {
            log.error("Lỗi khi xóa booking ID {}: ", id, e);
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa booking!");
        }
        return "redirect:/booking";
    }

    // === DELETE API ===
    @DeleteMapping("/api/delete/{id}")
    @ResponseBody
    public void deleteBookingApi(@PathVariable Long id) {
        try {
            log.info("API: Xóa booking ID: {}", id);
            bookingService.deleteBookingById(id);
            log.info("Booking ID {} đã được xóa thành công", id);
        } catch (Exception e) {
            log.error("Lỗi khi xóa booking ID {} qua API: ", id, e);
            throw new RuntimeException("Có lỗi xảy ra khi xóa booking: " + e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping("/api/test")
        public String test() {
            return "Backend is running!";

    }

}
