package com.example.qlks_2.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.qlks_2.entity.ServiceEntity;
import com.example.qlks_2.repository.ServiceRepository;
import com.example.qlks_2.service.ServiceService;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@Transactional
public class ServiceImpl implements ServiceService {
    private final ServiceRepository serviceRepository;

    public ServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceEntity> getAllServices() {
        return serviceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ServiceEntity> getServiceById(Long id) {
        return serviceRepository.findById(id);
    }

    @Override
    public ServiceEntity saveService(ServiceEntity service) {
        return serviceRepository.save(service);
    }

    @Override
    public ServiceEntity updateService(Long serviceId, ServiceEntity updateService) {
        return serviceRepository.saveAndFlush(updateService);
    }

    @Override
    public void deleteService(Long serviceId) {
        serviceRepository.deleteById(serviceId);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ServiceEntity> searchServicesPageable(String tenDichVu, ServiceEntity.ServiceType loaiDichVu, ServiceEntity.ServiceStatus status, Pageable pageable) {
        log.info("Tìm kiếm dịch vụ - tenDichVu: {}, loaiDichVu: {}",  tenDichVu, loaiDichVu);
        return serviceRepository.findByFiltersPageable(tenDichVu, loaiDichVu, status, pageable);
    }
}
