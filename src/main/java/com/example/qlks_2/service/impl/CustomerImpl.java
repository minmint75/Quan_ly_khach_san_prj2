package com.example.qlks_2.service.impl;

import com.example.qlks_2.entity.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.qlks_2.repository.CustomerRepository;
import com.example.qlks_2.service.CustomerService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomerImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        log.info("Lấy danh sách tất cả khách hàng");
        return customerRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Customer> getCustomerById(Long customerId) {
        log.info("Lấy thông tin khách hàng với ID: {}", customerId);
        return customerRepository.findById(customerId);
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        log.info("Thêm khách hàng mới: {}", customer);
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Long customerId, Customer updatedCustomer) {
        log.info("Cập nhật khách hàng có ID: {}", customerId);

        return customerRepository.findById(customerId)
                .map(existing -> {
                    updatedCustomer.setCustomerId(existing.getCustomerId()); // giữ nguyên ID cũ
                    return customerRepository.saveAndFlush(updatedCustomer);
                })
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + customerId));
    }

    @Override
    public void deleteCustomerById(Long customerId) {
        log.info("Xóa khách hàng với ID: {}", customerId);
        customerRepository.deleteById(customerId);
    }

    @Override
    public List<Customer> searchCustomers(String name, int identification, int phoneNumber) {
        return List.of();
    }

    @Override
    public Page<Customer> searchCustomerPageable(String name, int identification, int phoneNumber, Pageable pageable) {
        return null;
    }

    @Override
    public List<Customer> searchCustomersPageable(String name, int identification, int phoneNumber) {
        return List.of();
    }


    @Transactional(readOnly = true)
    @Override
    public Page<Customer> searchCustomerPageable(String name, Integer identification, Integer phoneNumber, Pageable pageable) {
        String normalizedName = (name != null && !name.trim().isEmpty()) ? name.trim() : null;
        Integer normalizedIdentification = (identification != null && identification > 0) ? identification : null;
        Integer normalizedPhoneNumber = (phoneNumber != null && phoneNumber > 0) ? phoneNumber : null;

        log.info("Tìm kiếm khách hàng - name: {}, identification: {}, phoneNumber: {}, phân trang: {}",
                normalizedName, normalizedIdentification, normalizedPhoneNumber, pageable);

        return customerRepository.findByFiltersPageable(normalizedName, normalizedIdentification, normalizedPhoneNumber, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Customer> searchCustomers(String name, Integer identification, Integer phoneNumber) {
        String normalizedName = (name != null && !name.trim().isEmpty()) ? name.trim() : null;
        Integer normalizedIdentification = (identification != null && identification > 0) ? identification : null;
        Integer normalizedPhoneNumber = (phoneNumber != null && phoneNumber > 0) ? phoneNumber : null;

        log.info("Tìm kiếm khách hàng - name: {}, identification: {}, phoneNumber: {}",
                normalizedName, normalizedIdentification, normalizedPhoneNumber);

        return customerRepository.findByFilters(normalizedName, normalizedIdentification, normalizedPhoneNumber);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Customer> getAllCustomersPageable(Pageable pageable) {
        log.info("Lấy danh sách khách hàng có phân trang: {}", pageable);
        return customerRepository.findAll(pageable);
    }
}
