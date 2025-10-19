package repository;

import entity.Booking;
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
public interface EmployeeRepository extends JpaRepository<Employee, String> {

    // --- Các phương thức tìm kiếm cơ bản ---
    Optional<Employee> findByName(String name);

    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByRole(String role);


    // --- Tìm kiếm nhân viên theo các tiêu chí ---
    @Query("SELECT e FROM Employee e WHERE " +
            "(:name IS NULL OR e.name = :name) AND " +
            "(:role IS NULL OR e.role = :role) AND " +
            "(:email IS NULL OR e.email = :email) ")
    List<Employee> findByFilters(@Param("name") String name,
                                @Param("role") String role,
                                @Param("email") String email
    );



}
