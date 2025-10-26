package com.example.qlks_2.service.impl;

import com.example.qlks_2.entity.ServiceUsage;
import com.example.qlks_2.repository.ServiceUsageRepository;
import com.example.qlks_2.service.ServiceUsageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ServiceUsageServiceImpl implements ServiceUsageService {

    private final ServiceUsageRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<ServiceUsage> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ServiceUsage> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public ServiceUsage save(ServiceUsage usage) {
        log.info("Thêm sử dụng dịch vụ mới: {}", usage);
        return repository.save(usage);
    }

    @Override
    public ServiceUsage update(Long id, ServiceUsage updateData) {
        ServiceUsage existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bản ghi ID: " + id));
        existing.setBookingId(updateData.getBookingId());
        existing.setService(updateData.getService());
        existing.setSoLuong(updateData.getSoLuong());
        existing.setNgaySuDung(updateData.getNgaySuDung());
        return repository.saveAndFlush(existing);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy bản ghi ID: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<ServiceUsage> searchByServiceName(String keyword) {
        log.info("Tìm kiếm theo tên dịch vụ: {}", keyword);
        return repository.findAll().stream()
                .filter(su -> su.getService().getName().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }

    @Override
    public List<ServiceUsage> findByFilters(Long bookingId, Long serviceId, LocalDate startDate, LocalDate endDate) {
        return repository.findByFilters(bookingId, serviceId, startDate, endDate);
    }

    @Override
    public Page<ServiceUsage> findByFiltersPageable(Long bookingId, Long serviceId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return repository.findByFiltersPageable(bookingId, serviceId, startDate, endDate, pageable);
    }
}