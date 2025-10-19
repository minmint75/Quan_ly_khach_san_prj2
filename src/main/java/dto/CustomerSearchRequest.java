package dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSearchRequest {

    // Search filters
    private String name;
    private int identification;
    private int phoneNumber;

    // Pagination
    private int page = 0;
    private int size = 10;

    // Sorting
    private String sort = "name";
    private String direction = "asc"; //

    // Helper methods
    public boolean hasFilters() {
        return (name != null && !name.trim().isEmpty()) ||
                phoneNumber > 0 ||
                identification > 0;
    }

    public String getSortDirection() {
        return "asc".equalsIgnoreCase(direction) ? "asc" : "desc";
    }

    public boolean isAscending() {
        return "asc".equalsIgnoreCase(direction);
    }
}