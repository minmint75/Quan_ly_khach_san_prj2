package com.example.qlks_2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Bill")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billId;

    // Mã booking liên kết (chỉ lưu ID, không khóa ngoại)
    @Column(name = "booking_id", nullable = false)
    @NotNull(message = "Mã booking không được để trống")
    private Long bookingId;

    @Column(name = "room_fee", nullable = false)
    @NotNull(message = "Tiền phòng không được để trống")
    private BigDecimal roomFee;

    @Column(name = "service_fee", nullable = false)
    @NotNull(message = "Tiền dịch vụ không được để trống")
    private BigDecimal serviceFee;

    @Column(name = "tax", nullable = false)
    @NotNull(message = "Thuế không được để trống")
    private BigDecimal tax;

    @Column(name = "total", nullable = false)
    @NotNull(message = "Tổng tiền không được để trống")
    private BigDecimal total;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BillStatus status;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = BillStatus.UNPAID;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum BillStatus {
        UNPAID("Chưa thanh toán"),
        PAID("Đã thanh toán"),
        CANCELLED("Đã hủy");

        private final String displayName;

        BillStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
