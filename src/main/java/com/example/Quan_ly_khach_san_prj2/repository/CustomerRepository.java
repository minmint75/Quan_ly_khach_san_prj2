package com.example.Quan_ly_khach_san_prj2.repository;

import entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByCustomerID(String customerID);

    Optional<Customer> findByCitizenID(String citizenID);

    Optional<Customer> findByEmail(String email);
}
