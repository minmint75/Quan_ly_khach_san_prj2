package com.example.qlks_2.repository;

import com.example.qlks_2.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface ServiceRepository extends JpaRepository<Service, Long> {

    // Tìm kiếm theo tên dịch vụ hoặc loại dịch vụ
    Page<Service> findByTenDichVuContainingIgnoreCaseOrLoaiDichVuContainingIgnoreCase(String ten, String loai, Pageable pageable);

    // Sắp xếp theo tên
    @Query("SELECT s FROM Service s ORDER BY s.tenDichVu ASC")
    Page<Service> sortByNameAsc(Pageable pageable);

    // Sắp xếp theo giá
    @Query("SELECT s FROM Service s ORDER BY s.gia ASC")
    Page<Service> sortByPriceAsc(Pageable pageable);
}
