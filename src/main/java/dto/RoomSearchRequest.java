package dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import entity.Room;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomSearchRequest {
    //search filters
    private String roomNumber;
    private String roomType;
    private Int roomFloor;

    // pagination
    private int page= 0;
    private int size= 10;

    // sorting
    private String sortBy= "roomId";
    private String direction= "DESC";

    public boolean hasFilters(){
        return (roomNumber != null && !roomNumber.trim().isEmpty()) ||
               (roomType != null && !roomType.trim().isEmpty()) ||
               (roomFloor != null);
    }

    public String getSortDirection() {
        return "asc".equalsIgnoreCase(direction) ? "asc" : "desc";
    }

    // helper methods
    public boolean hasFilters(){
        return (roomNumber != null && !roomNumber.trim().isEmpty()) ||
               (roomType != null && !roomType.trim().isEmpty()) ||
               (roomFloor != null);
    }

    public String getSortDirection() {
        return "asc".equalsIgnoreCase(direction) ? "asc" : "desc";
    }

    public boolean isAscending() {
        return "asc".equalsIgnoreCase(direction);
    }
}
