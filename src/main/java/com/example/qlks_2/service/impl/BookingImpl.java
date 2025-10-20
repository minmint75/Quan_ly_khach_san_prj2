package com.example.qlks_2.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.qlks_2.entity.Booking;
import com.example.qlks_2.repository.BookingRepository;
import com.example.qlks_2.service.BookingService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookingImpl implements BookingService {

    private final BookingRepository bookingRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getAllBookings() {
        log.info("Lấy danh sách tất cả đặt phòng");
        return bookingRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Booking> getBookingById(Long bookingId) {
        log.info("Lấy thông tin đặt phòng với ID: {}", bookingId);
        return bookingRepository.findById(bookingId);
    }

    @Override
    public Booking saveBooking(Booking booking) {
        log.info("Thêm đặt phòng mới: {}", booking);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking updateBooking(Long bookingId, Booking updatedBooking) {
        log.info("Cập nhật đặt phòng có ID: {}", bookingId);

        return bookingRepository.findById(bookingId)
                .map(existing -> {
                    updatedBooking.setBookingId(existing.getBookingId());
                    return bookingRepository.saveAndFlush(updatedBooking);
                })
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đặt phòng với ID: " + bookingId));
    }

    @Override
    public void deleteBookingById(Long bookingId) {
        log.info("Xóa đặt phòng với ID: {}", bookingId);
        bookingRepository.deleteById(bookingId);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Booking> searchBookingsPageable(Long roomId, Long customerId, Booking.BookingStatus status, Pageable pageable) {
        log.info("Tìm kiếm đặt phòng - roomId: {}, customerId: {}, status: {}, phân trang: {}",
                roomId, customerId, status, pageable);

        return bookingRepository.findByFilters(roomId, customerId, status, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Booking> searchBooking(Long roomId, Long customerId, Booking.BookingStatus status) {
        log.info("Tìm kiếm đặt phòng - roomId: {}, customerId: {}, status: {}", roomId, customerId, status);
        return bookingRepository.findByRoomIdAndCustomerIdAndStatus(roomId, customerId, status);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Booking> getAllBookingsPageable(Pageable pageable) {
        log.info("Lấy danh sách đặt phòng có phân trang: {}", pageable);
        return bookingRepository.findAll(pageable);
    }
}
