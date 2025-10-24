package com.example.qlks_2.service;

import com.example.qlks_2.entity.Service;
import com.example.qlks_2.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    // 1️⃣ Lấy danh sách dịch vụ + phân trang
    public Page<Service> getAllServices(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return serviceRepository.findAll(pageable);
    }

    // 2️⃣ Thêm dịch vụ mới
    public Service addService(Service service) {
        return serviceRepository.save(service);
    }

    // 3️⃣ Chỉnh sửa dịch vụ
    public Optional<Service> updateService(Long id, Service updatedService) {
        return serviceRepository.findById(id).map(existing -> {
            existing.setTenDichVu(updatedService.getTenDichVu());
            existing.setMoTa(updatedService.getMoTa());
            existing.setGia(updatedService.getGia());
            existing.setLoaiDichVu(updatedService.getLoaiDichVu());
            existing.setTrangThai(updatedService.getTrangThai());
            return serviceRepository.save(existing);
        });
    }

    // 4️⃣ Xóa dịch vụ
    public boolean deleteService(Long id) {
        if (serviceRepository.existsById(id)) {
            serviceRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // 5️⃣ Tìm kiếm dịch vụ theo tên hoặc loại
    public Page<Service> searchServices(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return serviceRepository.findByTenDichVuContainingIgnoreCaseOrLoaiDichVuContainingIgnoreCase(keyword, keyword, pageable);
    }

    // 6️⃣ Sắp xếp
    public Page<Service> sortByNameAsc(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return serviceRepository.sortByNameAsc(pageable);
    }

    public Page<Service> sortByPriceAsc(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return serviceRepository.sortByPriceAsc(pageable);
    }
}
