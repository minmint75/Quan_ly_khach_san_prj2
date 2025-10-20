package com.example.qlks_2.service;

import com.example.qlks_2.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    List<Employee> getAllEmployees();
    Employee getEmployeeById(Long id);
    Optional getEmployeeByIdOptional(Long id);
    Employee saveEmployee(Employee employee);
    Employee updateEmployee(Long id, Employee updatedEmployee);
    void deleteEmployeeById(Long id);
    List<Employee> searchEmployees(String name, String role, String email);
    Page<Employee> getAllEmployeesPageable(Pageable pageable);
    Page<Employee> sortEmployees(String sortBy, boolean ascending, Pageable pageable);
}
