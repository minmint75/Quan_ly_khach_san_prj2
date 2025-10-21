package com.example.qlks_2.repository;

import com.example.qlks_2.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // --- Các phương thức tìm kiếm cơ bản ---
    Optional<Employee> findByName(String name);

    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByRole(Employee.EmployeeRole role);


    // --- Tìm kiếm nhân viên theo các tiêu chí ---
    @Query("SELECT e FROM Employee e WHERE " +
            "(:name IS NULL OR e.name LIKE %:name%) AND " +
            "(:role IS NULL OR e.role = :role) AND " +
            "(:shift IS NULL OR e.shift = :shift) AND " +
            "(:status IS NULL OR e.employeeStatus = :status) AND " +
            "(:email IS NULL OR e.email LIKE %:email%)")
    List<Employee> findByFilters(@Param("name") String name,
                                 @Param("role") Employee.EmployeeRole role,
                                 @Param("shift") Employee.EmployeeShift shift,
                                 @Param("status") Employee.EmployeeStatus status,
                                 @Param("email") String email);



}
