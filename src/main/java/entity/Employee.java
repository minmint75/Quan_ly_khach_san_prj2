package entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id", nullable = false, unique = true)
    @NotBlank(message = "Mã nhân viên không được để trống")
    @Size(min = 3, max = 10, message = "Mã nhân viên phải có từ 3 đến 10 ký tự")
    private String employeeId;

    @Column(name = "name", nullable = false, length = 100)
    @NotBlank(message = "Tên nhân viên không được để trống")
    private String name;

    @Column(name = "role", nullable = false, length = 50)
    @NotBlank(message = "Chức vụ không được để trống")
    private String role;

    @Column(name = "phone_number", nullable = false, length = 10)
    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(0[0-9]{9})$", message = "Số điện thoại phải có 10 số và bắt đầu bằng 0")
    private int phoneNumber;

    @Column(name = "email", nullable = false, length = 100)
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @Column(name = "shift", nullable = false, length = 50)
    @NotBlank(message = "Ca làm việc không được để trống")
    private String shift;

    @Column(name = "salary", nullable = false, precision = 15, scale = 2)
    @DecimalMin(value = "0.0", inclusive = false, message = "Lương phải lớn hơn 0")
    private BigDecimal salary;

    @Column(name = "employee_status", length = 50)
    private String employeeStatus;

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
