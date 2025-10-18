package dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSearchRequest {
    private String employeeId;
    private String name;
    private String role;
    private String email;
    private String phoneNumber;
    private String shift;
    private Double salary;
    private String employeeStatus;

    // --- Phân trang ---
    private int page = 0;
    private int pageSize = 10;

    // --- Sắp xếp ---
    private String sort = "employeeId";
    private String direction = "ASC";

    // --- Kiểm tra có điều kiện lọc không ---
    public boolean hasFilters() {
        return ((name != null && !name.trim().isEmpty()) ||
                (role != null && !role.trim().isEmpty()) ||
                (email != null && !email.trim().isEmpty()));
    }

    // --- Lấy hướng sắp xếp ---
    public String getSortDirection() {
        return "asc".equalsIgnoreCase(direction) ? "ASC" : "DESC";
    }

    public boolean isAscending() {
        return "asc".equalsIgnoreCase(direction);
    }
}
