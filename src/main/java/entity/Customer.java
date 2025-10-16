package entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @Column(name = "full_name", nullable = false, length = 50)
    @NotNull(message = "Tên khách hàng không được để trống")
    @Size(min = 2, max = 50, message = "Tên khách hàng phải từ 2 đến 50 ký tự")
    private String fullName;

    @Column(name = "phone_number", nullable = false, length = 15)
    @NotNull(message = "Số điện thoại không được để trống")
    @Size(min = 9, max = 15, message = "Số điện thoại phải từ 9 đến 15 ký tự")
    private String phoneNumber;

    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Email không hợp lệ")
    private String email;

    @Column(name = "identity_number", nullable = false, unique = true, length = 12)
    @NotNull(message = "Số CMND/CCCD không được để trống")
    @Size(min = 9, max = 12, message = "CMND/CCCD phải có từ 9 đến 12 ký tự")
    private String identityNumber;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CustomerStatus status;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = CustomerStatus.ACTIVE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum CustomerStatus {
        ACTIVE("Đang hoạt động"),
        INACTIVE("Ngừng hoạt động"),
        BLACKLISTED("Bị cấm");

        private final String displayName;

        CustomerStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}

