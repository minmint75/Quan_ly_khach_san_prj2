package com.example.qlks_2.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import com.example.qlks_2.entity.ServiceEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequest {
    private Long serviceId;
    private String tenDichVu;
    private String moTa;
    private BigDecimal gia;
    private ServiceEntity.ServiceType loaiDichVu;
    private ServiceEntity.ServiceStatus trangThai;

    public ServiceEntity toEntity() {
        ServiceEntity serviceEntity = new ServiceEntity();
        serviceEntity.setServiceId(this.serviceId);
        serviceEntity.setTenDichVu(this.tenDichVu);
        serviceEntity.setMoTa(this.moTa);
        serviceEntity.setGia(this.gia);
        serviceEntity.setLoaiDichVu(this.loaiDichVu);
        serviceEntity.setTrangThai(this.trangThai);
        return serviceEntity;
    }

    public static  ServiceRequest fromEntity(ServiceEntity serviceEntity) {
        ServiceRequest request = new ServiceRequest();
        request.setServiceId(serviceEntity.getServiceId());
        request.setTenDichVu(serviceEntity.getTenDichVu());
        request.setMoTa(serviceEntity.getMoTa());
        request.setGia(serviceEntity.getGia());
        request.setLoaiDichVu(serviceEntity.getLoaiDichVu());
        request.setTrangThai(serviceEntity.getTrangThai());
        return request;
    }

    public boolean hasRoomPrice() {
        return gia != null
                && gia.compareTo(BigDecimal.ZERO) > 0
                && gia.compareTo(new BigDecimal("999999.99")) <= 0;
    }

}
