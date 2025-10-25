package com.example.qlks_2.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "service_usage")
public class ServiceUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_id", nullable = false)
    private Long bookingId;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @Column(name = "so_luong", nullable = false)
    private Integer soLuong;

    @Column(name = "ngay_su_dung")
    private LocalDate ngaySuDung;

    // Getters và Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public Service getService() { return service; }
    public void setService(Service service) { this.service = service; }

    public Integer getSoLuong() { return soLuong; }
    public void setSoLuong(Integer soLuong) { this.soLuong = soLuong; }

    public LocalDate getNgaySuDung() { return ngaySuDung; }
    public void setNgaySuDung(LocalDate ngaySuDung) { this.ngaySuDung = ngaySuDung; }
}
