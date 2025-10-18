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
    public List<Booking> getBookingById(String bookingId) {
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

    @Override
    @Transactional(readOnly = true)
    public List<Booking> searchBookings(String bookingId, String customerId, Booking.BookingStatus status) {
        String normalizedBookingId = (bookingId != null && !bookingId.trim().isEmpty()) ? bookingId.trim() : null;
        String normalizedCustomerId = (customerId != null && !customerId.trim().isEmpty()) ? customerId.trim() : null;

        log.info("Tìm kiếm đặt phòng - bookingId: {}, customerId: {}, BookingStatus: {}",
                normalizedBookingId, normalizedCustomerId, status);

        return bookingRepository.findByFilters(normalizedBookingId, normalizedCustomerId, status);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Booking> getAllBookings(Pageable pageable) {
        log.info("Lấy danh sách đặt phòng với phân trang: {}", pageable);
        return bookingRepository.findAll(pageable);
    }
}

