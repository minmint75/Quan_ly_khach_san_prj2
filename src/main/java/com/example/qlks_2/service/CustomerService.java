package com.example.qlks_2.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.qlks_2.entity.Customer;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    List<Customer> getAllCustomers();
    Optional<Customer> getCustomerById(Long customerId);
    Customer saveCustomer(Customer customer);
    Customer updateCustomer(Long customerId, Customer updateCustomer);
    void deleteCustomerById(Long customerId);
    List<Customer> searchCustomers(String name, int identification, int phoneNumber);
    Page<Customer> searchCustomerPageable(String name, int identification, int phoneNumber, Pageable pageable);
    List<Customer> searchCustomersPageable(String name, int identification, int phoneNumber);

    @Transactional(readOnly = true)
    Page<Customer> searchCustomerPageable(String name, Integer identification, Integer phoneNumber, Pageable pageable);

    @Transactional(readOnly = true)
    List<Customer> searchCustomers(String name, Integer identification, Integer phoneNumber);

    @Transactional(readOnly = true)
    Page<Customer> getAllCustomersPageable(Pageable pageable);
}