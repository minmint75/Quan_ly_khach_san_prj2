package service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import entity.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingService {
    List<Booking> getAllBookings();
    Optional<Booking> getBookingById(String bookingId);
    Booking saveBooking(Booking booking);
    Booking updateBooking(String BookingId, Booking updateBooking);
    void deleteBookingById(String bookingId);
    List<Booking> searchBookings(String bookingId, String customerId, String roomId);
    List<Booking> findByFilters(String bookingId, String customerId, String roomId, String status);
    List<Booking> getAvailableBookings();
    Page<Booking> getAllBookings(Pageable pageable);
}
