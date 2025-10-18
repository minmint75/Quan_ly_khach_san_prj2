package dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import entity.Employee;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {
    private String employeeId;
    private String name;
    private String position;
    private String phoneNumber;
    private String email;
    private String shift;
    private BigDecimal salary;
    private String employeeStatus;

    //  Chuyển DTO → Entity
    public Employee toEntity() {
        Employee employee = new Employee();
        employee.setEmployeeId(this.employeeId);
        employee.setName(this.name);
        employee.setRole(this.position);
        employee.setPhoneNumber(this.phoneNumber);
        employee.setEmail(this.email);
        employee.setShift(this.shift);
        employee.setSalary(this.salary);
        employee.setEmployeeStatus(this.employeeStatus);
        return employee;
    }

    //  Chuyển Entity → DTO
    public static EmployeeRequest fromEntity(Employee employee) {
        EmployeeRequest request = new EmployeeRequest();
        request.setEmployeeId(employee.getEmployeeId());
        request.setName(employee.getName());
        request.setPosition(employee.getRole());
        request.setPhoneNumber(employee.getPhoneNumber());
        request.setEmail(employee.getEmail());
        request.setShift(employee.getShift());
        request.setSalary(employee.getSalary());
        request.setEmployeeStatus(employee.getEmployeeStatus());
        return request;
    }

    //  Kiểm tra hợp lệ
    public void validate() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên nhân viên không được để trống.");
        }
        if (salary != null && salary.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Lương phải lớn hơn hoặc bằng 0.");
        }
    }
}
