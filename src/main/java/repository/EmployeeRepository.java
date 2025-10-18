package repository;

import entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Optional<Employee> findByRole(String role);

    @Query("SELECT new entity.Employee(e.employeeId, e.name, e.role, e.phoneNumber, e.email, e.shift, e.salary, e.employeeStatus) " +
            "FROM Employee e WHERE " +
            "(:name IS NULL OR :name = '' OR e.name = :name) AND " +
            "(:role IS NULL OR :role = '' OR e.role = :role) AND " +
            "(:shift IS NULL OR :shift = '' OR e.shift = :shift) AND " +
            "(:employeeStatus IS NULL OR :employeeStatus = '' OR e.employeeStatus = :employeeStatus) AND " +
            "(:phoneNumber IS NULL OR :phoneNumber = '' OR e.phoneNumber = :phoneNumber) " +
            "ORDER BY e.name ASC")
    List<Employee> findByFilters(@Param("name") String name,
                                 @Param("role") String role,
                                 @Param("shift") String shift,
                                 @Param("employeeStatus") String employeeStatus,
                                 @Param("phoneNumber") int phoneNumber);


    @Query("SELECT new entity.Employee(e.employeeId, e.name, e.role, e.phoneNumber, e.email, e.shift, e.salary, e.employeeStatus) " +
            "FROM Employee e WHERE " +
            "(:name IS NULL OR e.name = :name) AND " +
            "(:role IS NULL OR e.role = :role) AND " +
            "(:shift IS NULL OR e.shift = :shift) AND " +
            "(:employeeStatus IS NULL OR e.employeeStatus = :employeeStatus) AND " +
            "(:phoneNumber IS NULL OR e.phoneNumber = :phoneNumber)")
    Page<Employee> findByFiltersPageable(@Param("name") String name,
                                         @Param("role") String role,
                                         @Param("shift") String shift,
                                         @Param("employeeStatus") String employeeStatus,
                                         @Param("phoneNumber") int phoneNumber,
                                         Pageable pageable);
}
