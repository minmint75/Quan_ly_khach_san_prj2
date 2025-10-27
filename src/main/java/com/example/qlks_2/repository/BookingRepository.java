package com.example.qlks_2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.qlks_2.entity.Booking;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>{
    List<Booking> findByRoomId(Long roomId);

    List<Booking> findByCustomerId(Long customerId);

    List<Booking> findByStatus(Booking.BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE " +
            "(:roomId IS NULL OR b.roomId = :roomId) AND " +
            "(:customerId IS NULL OR b.customerId = :customerId) AND " +
            "(:BookingStatus IS NULL OR b.status = :status) " +
            "ORDER BY b.roomId DESC")
    Page<Booking> findByFilters(@Param("roomId") Long roomId,
                                @Param("customerId") Long customerId,
                                @Param("status") Booking.BookingStatus status, Pageable pageable);

    @Query("SELECT b FROM Booking b ORDER BY b.roomId DESC")
    Optional<Booking> findAllOrderByIdDesc();

    @Query("SELECT b FROM Booking b")
    Page<Booking> findAllPageAble(Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE " +
            "(:roomId IS NULL OR b.roomId = :roomId) AND " +
            "(:customerId IS NULL OR b.customerId = :customerId) AND " +
            "(:BookingStatus IS NULL OR b.status = :status)")
    Page<Booking> findByFiltersPageable(@Param("roomId") Long roomId,
                                        @Param("customerId") Long customerId,
                                        @Param("status") Booking.BookingStatus status,
                                        Pageable pageable);

    void deleteById(Long roomId);

    List<Booking> findByRoomIdAndCustomerIdAndStatus(Long roomId, Long customerId, Booking.BookingStatus status);
}
