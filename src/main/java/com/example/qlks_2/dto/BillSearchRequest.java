package com.example.qlks_2.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillSearchRequest {

    private Long bookingId;
    private String createdAt;

    private int page = 0;
    private int pageSize = 10;

    private String sort = "createdDate";
    private String direction = "DESC";

    public boolean hasFilters() {
        return (bookingId != null || (createdAt != null && !createdAt.isEmpty()));
    }

    public String getSortDirection() {
        return "asc".equalsIgnoreCase(direction) ? "asc" : "desc";
    }

    public boolean isAscending() {
        return "asc".equalsIgnoreCase(direction);
    }
}
