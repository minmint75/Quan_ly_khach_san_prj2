package repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import entity.Booking;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>{
    List<Booking> findByBookingIdIgnoreCase(String bookingId);

    List<Booking> findByCustomerIdIgnoreCase(String customerId);

    List<Booking> findByBookingStatus(Booking.BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE " +
            "(:bookingId IS NULL OR b.bookingId = :bookingId) AND " +
            "(:customerId IS NULL OR b.customerId = :customerId) AND " +
            "(:BookingStatus IS NULL OR b.status = :status) " +
            "ORDER BY b.bookingId DESC")
    List<Booking> findByFilters(@Param("bookingId") String bookingId,
                                @Param("customerId") String customerId,
                                @Param("status") Booking.BookingStatus status);

    @Query("SELECT b FROM Booking b ORDER BY b.bookingId DESC")
    List<Booking> findAllOrderByIdDesc();

    Page<Booking> findAll(Pageable pageable);


    @Query("SELECT b FROM Booking b WHERE " +
            "(:bookingId IS NULL OR b.bookingId = :bookingId) AND " +
            "(:customerId IS NULL OR b.customerId = :customerId) AND " +
            "(:BookingStatus IS NULL OR b.status = :status)")
    Page<Booking> findByFiltersPageable(@Param("bookingId") String bookingId,
                                        @Param("customerId") String customerId,
                                        @Param("status") Booking.BookingStatus status,
                                        Pageable pageable);

    void deleteById(String bookingId);
}
