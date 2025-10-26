package com.example.qlks_2.dto;

import com.example.qlks_2.entity.ServiceUsage;
import com.example.qlks_2.entity.Service;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceUsageRequest {
    private Long id;
    private Long bookingId;
    private Long serviceId;
    private Integer soLuong;
    private LocalDate ngaySuDung;

    public ServiceUsage toEntity(Service service) {
        return ServiceUsage.builder()
                .id(this.id)
                .bookingId(this.bookingId)
                .service(service)
                .soLuong(this.soLuong)
                .ngaySuDung(this.ngaySuDung != null ? this.ngaySuDung : LocalDate.now())
                .build();
    }

    public static ServiceUsageRequest fromEntity(ServiceUsage usage) {
        return ServiceUsageRequest.builder()
                .id(usage.getId())
                .bookingId(usage.getBookingId())
                .serviceId(usage.getService().getId())
                .soLuong(usage.getSoLuong())
                .ngaySuDung(usage.getNgaySuDung())
                .build();
    }

    public void validateData() {
        if (bookingId == null) throw new IllegalArgumentException("Mã booking không được để trống");
        if (serviceId == null) throw new IllegalArgumentException("Mã dịch vụ không được để trống");
        if (soLuong == null || soLuong <= 0) throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
    }
}