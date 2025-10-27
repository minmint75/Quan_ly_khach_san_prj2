package com.example.qlks_2.service;

import com.example.qlks_2.entity.ServiceUsage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ServiceUsageService {
    List<ServiceUsage> getAll();
    Optional<ServiceUsage> getById(Long id);
    ServiceUsage save(ServiceUsage usage);
    ServiceUsage update(Long id, ServiceUsage usage);
    void delete(Long id);

    List<ServiceUsage> searchByServiceName(String keyword);
    List<ServiceUsage> findByFilters(Long bookingId, Long serviceId, LocalDate startDate, LocalDate endDate);
    Page<ServiceUsage> findByFiltersPageable(Long bookingId, Long serviceId, LocalDate startDate, LocalDate endDate, Pageable pageable);
}