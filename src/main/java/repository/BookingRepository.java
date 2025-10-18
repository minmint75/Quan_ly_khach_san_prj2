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
    List<Booking> findByroomIdIgnoreCase(String roomId);

    List<Booking> findByCustomerIdIgnoreCase(String customerId);

    List<Booking> findByBookingStatus(Booking.BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE " +
            "(:roomId IS NULL OR b.roomId = :roomId) AND " +
            "(:customerId IS NULL OR b.customerId = :customerId) AND " +
            "(:BookingStatus IS NULL OR b.status = :status) " +
            "ORDER BY b.roomId DESC")
    List<Booking> findByFilters(@Param("roomId") String roomId,
                                @Param("customerId") String customerId,
                                @Param("status") Booking.BookingStatus status);

    @Query("SELECT b FROM Booking b ORDER BY b.roomId DESC")
    List<Booking> findAllOrderByIdDesc();

    Page<Booking> findAll(Pageable pageable);


    @Query("SELECT b FROM Booking b WHERE " +
            "(:roomId IS NULL OR b.roomId = :roomId) AND " +
            "(:customerId IS NULL OR b.customerId = :customerId) AND " +
            "(:BookingStatus IS NULL OR b.status = :status)")
    Page<Booking> findByFiltersPageable(@Param("roomId") String roomId,
                                        @Param("customerId") String customerId,
                                        @Param("status") Booking.BookingStatus status,
                                        Pageable pageable);

    void deleteById(String roomId);
}
