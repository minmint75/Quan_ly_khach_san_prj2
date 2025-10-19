package service;

import entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    List<Employee> getAllEmployees();
    Employee getEmployeeById(String id);
    Optional getEmployeeByIdOptional(String id);
    Employee saveEmployee(Employee employee);
    Employee updateEmployee(String id, Employee updatedEmployee);
    void deleteEmployeeById(String id);
    List<Employee> searchEmployees(String name, String role, String email);
    Page<Employee> getAllEmployeesPageable(Pageable pageable);
    Page<Employee> sortEmployees(String sortBy, boolean ascending, Pageable pageable);
}
