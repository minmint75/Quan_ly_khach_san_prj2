package com.example.qlks_2.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "service")
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_dich_vu", nullable = false)
    private String tenDichVu;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "gia", nullable = false)
    private Double gia;

    @Column(name = "loai_dich_vu")
    private String loaiDichVu;

    @Column(name = "trang_thai")
    private String trangThai;

    // Getters và Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTenDichVu() { return tenDichVu; }
    public void setTenDichVu(String tenDichVu) { this.tenDichVu = tenDichVu; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public Double getGia() { return gia; }
    public void setGia(Double gia) { this.gia = gia; }

    public String getLoaiDichVu() { return loaiDichVu; }
    public void setLoaiDichVu(String loaiDichVu) { this.loaiDichVu = loaiDichVu; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
