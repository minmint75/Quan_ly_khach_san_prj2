package entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @Column(name = "customer_id", nullable = false, length = 10)
    @NotBlank(message = "Mã khách hàng không được để trống")
    @Size(min = 3, max = 10, message = "Mã khách hàng phải có từ 3 đến 10 ký tự")
    private Long customerId;

    @Column(name = "name", nullable = false, length = 100)
    @NotBlank(message = "Tên khách hàng không được để trống")
    private String name;

    @Column(name = "email", nullable = false, length = 100)
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @Column(name = "identification", nullable = false, length = 12)
    @NotBlank(message = "CCCD không được để trống")
    @Pattern(regexp = "^[0-9]{9,12}$", message = "CCCD phải là 9-12 chữ số")
    private String identification;

    @Column(name = "phone_number", nullable = false, length = 10)
    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(0[0-9]{9})$", message = "Số điện thoại Việt Nam phải có 10 số và bắt đầu bằng 0")
    private String phoneNumber;

    @Column(name = "nationality", length = 50)
    private String nationality;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
