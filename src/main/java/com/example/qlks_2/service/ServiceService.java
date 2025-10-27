package com.example.qlks_2.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.qlks_2.entity.ServiceEntity;

import java.util.List;
import java.util.Optional;

public interface ServiceService {
    List<ServiceEntity> getAllServices();
    Optional<ServiceEntity> getServiceById(Long id);
    ServiceEntity saveService(ServiceEntity serviceId);
    ServiceEntity updateService(Long serviceId, ServiceEntity updateService);
    void deleteService(Long serviceId);
    Page<ServiceEntity> searchServicesPageable(String tenDichVu, ServiceEntity.ServiceType loaiDichVu, ServiceEntity.ServiceStatus trangThai, Pageable pageable);

}
