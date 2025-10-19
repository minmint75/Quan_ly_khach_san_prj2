package service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import entity.Booking;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    List<Booking> getAllBookings();
    Optional<Booking> getBookingById(String bookingId);
    Booking saveBooking(Booking bookingId);
    Booking updateBooking(String bookingId, Booking updateBooking);
    void deleteBookingById(String bookingId);
    List<Booking> searchBooking(String bookingId, String customerId, Booking.BookingStatus status);
    Page<Booking> searchBookingsPageable(String roomId, String customerId, Booking.BookingStatus status, Pageable pageable);
    Page<Booking> getAllBookingsPageable(Pageable pageable);
}
