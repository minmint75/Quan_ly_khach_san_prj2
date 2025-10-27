package com.example.qlks_2.repository;

import com.example.qlks_2.entity.ServiceUsage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ServiceUsageRepository extends JpaRepository<ServiceUsage, Long> {

    List<ServiceUsage> findByBookingId(Long bookingId);

    @Query("SELECT su FROM ServiceUsage su WHERE su.service.serviceId = :serviceId")
    List<ServiceUsage> findByServiceId(@Param("serviceId") Long serviceId);

    List<ServiceUsage> findByUsageDate(LocalDate UsageDate);

    @Query("SELECT su FROM ServiceUsage su WHERE su.usageDate BETWEEN :start AND :end ORDER BY su.usageDate DESC")
    List<ServiceUsage> findByUsageDateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("""
        SELECT su FROM ServiceUsage su
        WHERE (:bookingId IS NULL OR su.bookingId = :bookingId)
        AND (:serviceId IS NULL OR su.service.serviceId = :serviceId)
        AND (:startDate IS NULL OR su.usageDate >= :startDate)
        AND (:endDate IS NULL OR su.usageDate <= :endDate)
        ORDER BY su.usageDate DESC
    """)
    List<ServiceUsage> findByFilters(
            @Param("bookingId") Long bookingId,
            @Param("serviceId") Long serviceId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
        SELECT su FROM ServiceUsage su
        WHERE (:bookingId IS NULL OR su.bookingId = :bookingId)
        AND (:serviceId IS NULL OR su.service.serviceId = :serviceId)
        AND (:startDate IS NULL OR su.usageDate >= :startDate)
        AND (:endDate IS NULL OR su.usageDate <= :endDate)
        ORDER BY su.usageDate DESC
    """)
    Page<ServiceUsage> findByFiltersPageable(
            @Param("bookingId") Long bookingId,
            @Param("serviceId") Long serviceId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    @Query("SELECT su FROM ServiceUsage su ORDER BY su.usageDate DESC")
    Page<ServiceUsage> findAllPageable(Pageable pageable);
}