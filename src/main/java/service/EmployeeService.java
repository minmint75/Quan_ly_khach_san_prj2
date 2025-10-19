package service;

import entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployees();
    Employee getEmployeeById(Long id);
    Employee saveEmployee(Employee employee);
    Employee updateEmployee(Long id, Employee updatedEmployee);
    void deleteEmployeeById(Long id);
    List<Employee> searchEmployees(String name, String role, String email);
    Page<Employee> getAllEmployeesPageable(Pageable pageable);
    Page<Employee> sortEmployees(String sortBy, boolean ascending, Pageable pageable);
}
