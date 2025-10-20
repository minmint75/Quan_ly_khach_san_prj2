package com.example.qlks_2.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.example.qlks_2.entity.Booking;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingSearchRequest {

    private Long roomId;
    private Long customerId;
    private Booking.BookingStatus status;

    private int page = 0;
    private int pageSize = 10;

    private String sort = "roomId";
    private String direction = "DESC";

    public boolean hasFilters() {
        return (roomId != null || customerId != null || status != null);
    }

    public String getSortDirection() {
        return "asc".equalsIgnoreCase(direction) ? "asc" : "desc";
    }

    public boolean isAscending() {
        return "asc".equalsIgnoreCase(direction);
    }
}
