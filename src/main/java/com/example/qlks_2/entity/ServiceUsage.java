package com.example.qlks_2.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "service_usage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceUsageId;

    @Column(name = "booking_id", nullable = false)
    private Long bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceEntity service;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "usage_date")
    private LocalDate usageDate;
}
