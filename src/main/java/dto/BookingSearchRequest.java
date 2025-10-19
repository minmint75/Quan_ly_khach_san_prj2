package dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import entity.Booking;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingSearchRequest {
    private String roomId;
    private String customerId;
    private Booking.BookingStatus status;

    private int page = 0;
    private int pageSize = 10;

    private String sort = "roomId";
    private String direction = "DESC";

    public boolean hasFilers(){
        return (roomId != null && !roomId.trim().isEmpty()) && (customerId != null && !customerId.trim().isEmpty()) &&
        status != null;
    }

    public String getSortDirection() {
        return "asc".equalsIgnoreCase(direction) ? "asc" : "desc";
    }

    public boolean isAscending() {
        return "asc".equalsIgnoreCase(direction);
    }
}
