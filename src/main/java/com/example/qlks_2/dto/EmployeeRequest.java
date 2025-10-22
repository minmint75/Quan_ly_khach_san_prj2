package com.example.qlks_2.dto;

import com.example.qlks_2.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {

    private Long employeeId;
    private String name;
    private Employee.EmployeeRole position; // Changed from String
    private int phoneNumber;
    private String email;
    private Employee.EmployeeShift shift; // Changed from String
    private BigDecimal salary;
    private Employee.EmployeeStatus employeeStatus; // Changed from String

    public Employee toEntity() {
        Employee employee = new Employee();
        employee.setEmployeeId(this.employeeId);
        employee.setName(this.name);
        employee.setRole(this.position); // Maps position to role
        employee.setPhoneNumber(this.phoneNumber);
        employee.setEmail(this.email);
        employee.setShift(this.shift);
        employee.setSalary(this.salary);
        employee.setEmployeeStatus(this.employeeStatus);
        return employee;
    }

    public static EmployeeRequest fromEntity(Employee employee) {
        if (employee == null) return null;

        EmployeeRequest request = new EmployeeRequest();
        request.setEmployeeId(employee.getEmployeeId());
        request.setName(employee.getName());
        request.setPosition(employee.getRole()); // Maps role to position
        request.setPhoneNumber(employee.getPhoneNumber());
        request.setEmail(employee.getEmail());
        request.setShift(employee.getShift());
        request.setSalary(employee.getSalary());
        request.setEmployeeStatus(employee.getEmployeeStatus());
        return request;
    }

    public void validate() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên nhân viên không được để trống.");
        }
        if (salary != null && salary.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Lương phải lớn hơn hoặc bằng 0.");
        }
    }
}
