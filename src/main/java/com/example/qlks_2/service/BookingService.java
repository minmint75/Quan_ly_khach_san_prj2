package com.example.qlks_2.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.qlks_2.entity.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    List<Booking> getAllBookings();
    Optional<Booking> getBookingById(Long bookingId);
    Booking saveBooking(Booking bookingId);
    Booking updateBooking(Long bookingId, Booking updateBooking);
    void deleteBookingById(Long bookingId);
    List<Booking> searchBooking(Long bookingId, Long customerId, Booking.BookingStatus status);
    Page<Booking> searchBookingsPageable(Long roomId, Long customerId, Booking.BookingStatus status, Pageable pageable);
    Page<Booking> getAllBookingsPageable(Pageable pageable);
}
