package com.example.qlks_2.repository;

import com.example.qlks_2.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.qlks_2.entity.Customer;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByName(String name);

    List<Customer> findByIdentification(int identification);

    List<Customer> findByPhoneNumber(int phoneNumber);

    @Query("SELECT c FROM Customer c WHERE " +
            "(:name IS NULL OR c.name LIKE %:name%) AND " +
            "(:identification = 0 OR c.identification = :identification) AND " +
            "(:phoneNumber = 0 OR c.phoneNumber = :phoneNumber)")
    List<Customer> findByFilters(@Param("name") String name,
                                 @Param("identification") int identification,
                                 @Param("phoneNumber") int phoneNumber);


    Optional<Customer> findFirstByOrderByCustomerIdDesc();

    @Query("SELECT c FROM Customer c")

    Page<Customer> findAllPageAble(Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE " +
            "(:name IS NULL OR :name = '' OR c.name LIKE %:name%) AND " +
            "(:identification = 0 OR c.identification = :identification) AND " +
            "(:phoneNumber = 0 OR c.phoneNumber = :phoneNumber)")
    Page<Customer> findByFiltersPageable(@Param("name") String name,
                                         @Param("identification") int identification,
                                         @Param("phoneNumber") int phoneNumber,
                                         Pageable pageable);

    void deleteById(Long customerId);
    List<Customer> findByNameAndIdentificationAndPhoneNumber(String name, Integer indentification, Integer phoneNumber);

    Optional<Customer> findById(Long customerId);

}