package com.example.qlks_2.entity;

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
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "name", nullable = false, length = 100)
    @NotBlank(message = "Tên khách hàng không được để trống")
    private String name;

    @Column(name = "email", length = 100)
    @Email(message = "Email không hợp lệ")
    private String email;

    @Column(name = "identification", nullable = false)
    @NotNull(message = "CCCD không được để trống")
    private long identification;

    @Column(name = "phone_number", nullable = false)
    @NotNull(message = "Số điện thoại không được để trống")
    private long phoneNumber;

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
