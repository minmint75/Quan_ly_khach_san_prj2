package com.example.qlks_2.repository;

import com.example.qlks_2.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    // Tìm kiếm theo tên dịch vụ hoặc loại dịch vụ
    Page<ServiceEntity> findByTenDichVuContainingIgnoreCaseOrLoaiDichVuContainingIgnoreCase(String ten, String loai, Pageable pageable);

    // Sắp xếp theo tên
    @Query("SELECT s FROM ServiceEntity s ORDER BY s.tenDichVu ASC")
    Page<ServiceEntity> sortByNameAsc(Pageable pageable);

    // Sắp xếp theo giá
    @Query("SELECT s FROM ServiceEntity s ORDER BY s.gia ASC")
    Page<ServiceEntity> sortByPriceAsc(Pageable pageable);
}
