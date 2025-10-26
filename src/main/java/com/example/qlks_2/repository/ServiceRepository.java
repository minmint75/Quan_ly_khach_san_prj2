package com.example.qlks_2.repository;

import com.example.qlks_2.entity.ServiceEntity;
import com.example.qlks_2.entity.ServiceEntity.ServiceType;
import com.example.qlks_2.entity.ServiceEntity.ServiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    Page<ServiceEntity> findByTenDichVuContainingIgnoreCase(String ten, Pageable pageable);

    @Query("SELECT s FROM ServiceEntity s ORDER BY s.tenDichVu ASC")
    Page<ServiceEntity> sortByNameAsc(Pageable pageable);

    @Query("SELECT s FROM ServiceEntity s ORDER BY s.gia ASC")
    Page<ServiceEntity> sortByPriceAsc(Pageable pageable);

    @Query("SELECT s FROM ServiceEntity s")
    Page<ServiceEntity> findAllPageAble(Pageable pageable);

    @Query("SELECT s FROM ServiceEntity s WHERE " +
            "(:keyword IS NULL OR LOWER(s.tenDichVu) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(s.moTa) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "(:loaiDichVu IS NULL OR s.loaiDichVu = :loaiDichVu) AND " +
            "(:trangThai IS NULL OR s.trangThai = :trangThai) " +
            "ORDER BY s.serviceId DESC")
    Page<ServiceEntity> findByFiltersPageable(
            @Param("keyword") String keyword,
            @Param("loaiDichVu") ServiceType loaiDichVu,
            @Param("trangThai") ServiceStatus trangThai,
            Pageable pageable);

    Optional<ServiceEntity> findById(Long serviceId);

    List<ServiceEntity> findByLoaiDichVu(ServiceType loaiDichVu);

    List<ServiceEntity> findByTrangThai(ServiceStatus trangThai);
}