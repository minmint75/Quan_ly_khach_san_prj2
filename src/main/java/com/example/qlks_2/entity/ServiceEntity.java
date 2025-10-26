package com.example.qlks_2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "service")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;

    @Column(name = "ten_dich_vu", nullable = false, length = 100)
    @NotNull(message = "Tên dịch vụ không được để trống")
    @Size(min = 2, max = 100, message = "Tên dịch vụ phải có ít nhất 2 kí tự")
    private String tenDichVu;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "gia", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Giá dịch vụ không được để trống")
    @Positive(message = "Giá dịch vụ phải là số dương")
    private BigDecimal gia;

    @Enumerated(EnumType.STRING)
    @Column(name = "loai_dich_vu", nullable = false)
    @NotNull(message = "Loại dịch vụ không được để trống")
    private ServiceType loaiDichVu;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false)
    private ServiceStatus trangThai;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (trangThai == null) {
            trangThai = ServiceStatus.ACTIVE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum ServiceType {
        PER_USE("Tính theo lần"),
        PER_HOUR("Tính theo giờ"),
        PER_DAY("Tính theo ngày"),
        CONSUMABLE("Tiêu hao (Ví dụ: Đồ ăn/Nước uống)");

        private final String displayName;

        ServiceType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }


    public enum ServiceStatus {
        ACTIVE("Đang hoạt động"),
        INACTIVE("Ngừng hoạt động"),
        TEMPORARY_OUT("Tạm thời hết");

        private final String displayName;

        ServiceStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}