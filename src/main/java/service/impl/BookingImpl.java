package service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import entity.Booking;
import repository.BookingRepository;
import service.BookingService;

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
    public Optional<Booking> getBookingById(String bookingId) {
        log.info("Lấy thông tin đặt phòng với ID: " + bookingId);
        return bookingRepository.findAllOrderByIdDesc();
    }
    @Override
    public Booking saveBooking(Booking bookingId) {
        log.info("Thêm đặt phòng mới với mã: " + bookingId );
        return bookingRepository.save(bookingId);
    }

    @Override
    public Booking updateBooking(String bookingId, Booking updateBooking) {
        log.info("Cập nhật phòng có mã: " + bookingId);
        return bookingRepository.saveAndFlush(updateBooking);
    }

    @Override
    public void deleteBookingById(String bookingId) {
        log.info("Xóa đặt phòng với mã: {}", bookingId);
        bookingRepository.deleteById(bookingId);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Booking> searchBookingsPageable(String roomId, String customerId, Booking.BookingStatus status, Pageable pageable) {
        String normalizedroomId = (roomId != null && !roomId.trim().isEmpty()) ? roomId.trim() : null;
        String normalizedCustomerId = (customerId != null && !customerId.trim().isEmpty()) ? customerId.trim() : null;

        log.info("Tìm kiếm đặt phòng - roomId: {}, customerId: {}, BookingStatus: {}, phân trang: {}",
                normalizedroomId, normalizedCustomerId, status, pageable);

        return bookingRepository.findByFilters(normalizedroomId, normalizedCustomerId, status, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Booking> searchBooking(String roomId, String customerId, Booking.BookingStatus status) {
        String normalizedroomId = (roomId != null && !roomId.trim().isEmpty()) ? roomId.trim() : null;
        String normalizedCustomerId = (customerId != null && !customerId.trim().isEmpty()) ? customerId.trim() : null;

        log.info("Tìm kiếm đặt phòng - roomId: {}, customerId: {}, BookingStatus: {}",
                normalizedroomId, normalizedCustomerId, status);

        return bookingRepository.findAll(normalizedroomId, normalizedCustomerId, status);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Booking> getAllBookingsPageable(Pageable pageable) {
        log.info("Lấy danh sách đặt phòng với phân trang: {}", pageable);
        return bookingRepository.findAll(pageable);
    }
}

