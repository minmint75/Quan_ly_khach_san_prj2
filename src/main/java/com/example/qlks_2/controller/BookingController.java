package com.example.qlks_2.controller;

import com.example.qlks_2.dto.BookingRequest;
import com.example.qlks_2.entity.Booking;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.qlks_2.service.BookingService;

import java.util.List;
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

    // === ADD HANDLER ===
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

    // === EDIT HANDLER ===
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

    // === DELETE ===
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

    @ResponseBody
    @RequestMapping("/api/test")
        public String test() {
            return "Backend is running!";

    }

}
