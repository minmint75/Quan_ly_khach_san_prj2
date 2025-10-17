package entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Booking")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String bookingId;

    @Column(name ="customer_id", nullable = false, length = 6)
    @NotNull(message = "Mã khách hàng không được để trống")
    @Size(min = 3, max = 10, message = "Mã khách hàng có 3 kí tự trở lên, tối đa 10 kí tự")
    private String customerId;

    @Column(name ="room_id", nullable = false, length = 6)
    @NotNull(message = "Mã phòng không được để trống")
    @Size(min = 3, max = 10, message = "Mã phòng có 3 kí tự trở lên, tối đa 10 kí tự")
    private String roomId;

    @Column(name = "check_in", nullable = false)
    @NotNull(message = "Ngày nhận phòng không được để trống")
    private LocalDateTime checkIn;

    @Column(name = "check_out", nullable = false)
    @NotNull(message = "Ngày trả phòng không được để trống")
    private LocalDateTime checkOut;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = BookingStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum BookingStatus {
        PENDING("Đang chờ xác nhận"),
        CONFIRMED("Đã xác nhận"),
        CANCELLED("Đã hủy");

        private final String displayName;

        BookingStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
