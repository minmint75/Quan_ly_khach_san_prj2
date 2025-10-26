package com.example.qlks_2.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceUsageSearchRequest {
    private Long bookingId;
    private Long serviceId;

    private int page = 0;
    private int pageSize = 10;

    private String sort = "ngaySuDung";
    private String direction = "DESC";

    public boolean hasFilters() {
        return bookingId != null || serviceId != null;
    }

    public boolean isAscending() {
        return "asc".equalsIgnoreCase(direction);
    }
}