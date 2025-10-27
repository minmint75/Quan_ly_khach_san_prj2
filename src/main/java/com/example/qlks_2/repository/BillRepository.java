package com.example.qlks_2.repository;

import com.example.qlks_2.entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findByBookingId(Long bookingId);
    List<Bill> findByCreatedAt(LocalDateTime createdAt);

    @Query("SELECT b FROM Bill b WHERE b.createdAt BETWEEN :start AND :end ORDER BY b.createdAt DESC")
    List<Bill> findByCreatedAtBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT b FROM Bill b WHERE " +
            "(:bookingId IS NULL OR b.bookingId = :bookingId) AND " +
            "(:status IS NULL OR b.status = :status) AND " +
            "(:startDate IS NULL OR b.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR b.createdAt <= :endDate) " +
            "ORDER BY b.createdAt DESC")
    Page<Bill> findByFiltersPageable(@Param("bookingId") Long bookingId,
                             @Param("status") Bill.BillStatus status,
                             @Param("startDate") LocalDateTime startDate,
                             @Param("endDate") LocalDateTime endDate,
                             Pageable pageable);

    @Query("SELECT b FROM Bill b WHERE " +
            "(:bookingId IS NULL OR b.bookingId = :bookingId) AND " +
            "(:status IS NULL OR b.status = :status) AND " +
            "(:startDate IS NULL OR b.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR b.createdAt <= :endDate) " +
            "ORDER BY b.createdAt DESC")
    List<Bill> findByFilters(@Param("bookingId") Long bookingId,
                                     @Param("status") Bill.BillStatus status,
                                     @Param("startDate") LocalDateTime startDate,
                                     @Param("endDate") LocalDateTime endDate);

    @Query("SELECT b FROM Bill b ORDER BY b.createdAt DESC")
    Page<Bill> findAllPageable(Pageable pageable);

    void deleteById(Long billId);



}
