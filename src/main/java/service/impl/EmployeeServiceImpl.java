package service.impl;

import entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import repository.EmployeeRepository;
import service.EmployeeService;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    // 1. Lấy tất cả nhân viên
    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // 2. Lấy nhân viên theo ID
    @Override
    public Employee getEmployeeById(String id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public Optional getEmployeeByIdOptional(String id) {
        return employeeRepository.findById(id);
    }


    // 3. Thêm mới nhân viên
    @Override
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    // 4. Cập nhật nhân viên
    @Override
    public Employee updateEmployee(String id, Employee updatedEmployee) {
        Optional<Employee> existing = employeeRepository.findById(id);
        if (existing.isPresent()) {
            Employee e = existing.get();
            e.setName(updatedEmployee.getName());
            e.setRole(updatedEmployee.getRole());
            e.setPhoneNumber(updatedEmployee.getPhoneNumber());
            e.setEmail(updatedEmployee.getEmail());
            e.setShift(updatedEmployee.getShift());
            e.setSalary(updatedEmployee.getSalary());
            e.setEmployeeStatus(updatedEmployee.getEmployeeStatus());
            return employeeRepository.save(e);
        }
        return null;
    }

    // 5. Xóa nhân viên theo ID
    @Override
    public void deleteEmployeeById(String id) {
        employeeRepository.deleteById(id);
    }

    // 6. Tìm kiếm nhân viên theo Tên, Chức vụ, Email
    @Override
    public List<Employee> searchEmployees(String name, String role, String email) {
        return employeeRepository.findByFilters(name, role, email);
    }

    // 7. Phân trang danh sách nhân viên
    @Override
    public Page<Employee> getAllEmployeesPageable(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    // 8. Sắp xếp danh sách nhân viên
    @Override
    public Page<Employee> sortEmployees(String sortBy, boolean ascending, Pageable pageable) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return employeeRepository.findAll(sortedPageable);
    }
}
