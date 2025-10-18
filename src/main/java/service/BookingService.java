package service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import entity.Booking;

import java.util.List;

public interface BookingService {
    List<Booking> getAllBookings();
    List<Booking> getBookingById(String bookingId);
    Booking saveBooking(Booking bookingId);
    Booking updateBooking(String bookingId, Booking updateBooking);
    void deleteBookingById(String bookingId);
    List<Booking> searchBookings(String bookingId, String customerId, Booking.BookingStatus status);
    Page<Booking> getAllBookings(Pageable pageable);
}
