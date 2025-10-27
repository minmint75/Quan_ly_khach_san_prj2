package com.example.qlks_2.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.example.qlks_2.entity.ServiceEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceSearchRequest {
    private String serviceName;
    private ServiceEntity.ServiceType loaiDichVu;
    private ServiceEntity.ServiceStatus status;

    private int page= 0;
    private int size= 10;

    private String sortBy= "tenDichVu";
    private String direction= "DESC";

    public String getSortDirection() {
        return "asc".equalsIgnoreCase(direction) ? "asc" : "desc";
    }
    public boolean isAscending() {
        return "asc".equalsIgnoreCase(direction);
    }

}
