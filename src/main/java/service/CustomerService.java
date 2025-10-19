package service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import entity.Customer;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    List<Customer> getAllCustomers();
    Optional<Customer> getCustomerById(String customerId);
    Customer saveCustomer(Customer customer);
    Customer updateCustomer(String customerId, Customer updateCustomer);
    void deleteCustomerById(String customerId);
    List<Customer> searchCustomers(String name, int identification, int phoneNumber);
    Page<Customer> searchCustomersPageable(String name, int identification, int phoneNumber, Pageable pageable);
    Page<Customer> getAllCustomers(Pageable pageable);

    List<Customer> searchCustomers(String name, String identification, int phoneNumber);

    @Transactional(readOnly = true)
    List<Customer> searchCustomersPageable(String name, int identification, int phoneNumber);

    Page<Customer> getAllCustomersPageable(Pageable pageable);

    List<Customer> searchCustomer(String name, int email, int phone);

}