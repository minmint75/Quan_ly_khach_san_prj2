package com.example.qlks_2.dto;

import com.example.qlks_2.entity.ServiceEntity;
import com.example.qlks_2.entity.ServiceUsage;
import com.example.qlks_2.entity.ServiceEntity;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceUsageRequest {
    private Long serviceUsageId;
    private Long bookingId;
    private Long serviceId;
    private Integer quantity;
    private LocalDate usageDate;

    public ServiceUsage toEntity(ServiceEntity service) {
        return ServiceUsage.builder()
                .serviceUsageId(this.serviceUsageId)
                .bookingId(this.bookingId)
                .service(service)
                .quantity(this.quantity)
                .usageDate(this.usageDate != null ? this.usageDate : LocalDate.now())
                .build();
    }

    public static ServiceUsageRequest fromEntity(ServiceUsage usage) {
        return ServiceUsageRequest.builder()
                .serviceUsageId(usage.getServiceUsageId())
                .bookingId(usage.getBookingId())
                .serviceId(usage.getService().getServiceId())
                .quantity(usage.getQuantity())
                .usageDate(usage.getUsageDate())
                .build();
    }

    public void validateData() {
        if (bookingId == null) throw new IllegalArgumentException("Mã booking không được để trống");
        if (serviceId == null) throw new IllegalArgumentException("Mã dịch vụ không được để trống");
        if (quantity == null || quantity <= 0) throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
    }
}