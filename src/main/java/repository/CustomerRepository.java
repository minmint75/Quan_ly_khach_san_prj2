package repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import entity.Customer;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByName(String name);

    List<Customer> findByIdentification(int identification);

    List<Customer> findByPhoneNumber(int phoneNumber);

    @Query("SELECT c FROM Customer c WHERE " +
            "(:name IS NULL OR :name = '' OR LOWER(c.name) LIKE CONCAT('%', LOWER(:name), '%')) AND " +
            "(:identification IS NULL OR :identification = '' OR c.identification = :identification) AND " +
            "(:phoneNumber IS NULL OR :phoneNumber = '' OR c.phoneNumber = :phoneNumber) " +
            "ORDER BY c.name DESC")
    List<Customer> findByFilters(@Param("name") String name,
                                 @Param("identification") int identification,
                                 @Param("phoneNumber") int phoneNumber
                                 );

    Optional<Customer> findFirstByOrderByCustomerIdDesc();

    Page<Customer> findAllPageAble(Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE " +
            "(:name IS NULL OR :name = '' OR c.name = :name) AND " +
            "(:identification IS NULL OR :identification = '' OR c.identification = :identification) AND " +
            "(:phoneNumber IS NULL OR :phoneNumber = '' OR c.phoneNumber = :phoneNumber)")
    Page<Customer> findByFiltersPageable(@Param("name") String name,
                                         @Param("identification") int identification,
                                         @Param("phoneNumber") int phoneNumber,
                                         Pageable pageable);

    void deleteById(String customerId);
    List<Customer> findAll();

    Optional<Customer> findById(String customerId);

}