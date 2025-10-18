package service.impl;

import entity.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.CustomerRepository;
import service.CustomerService;

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
    public Optional<Customer> getCustomerById(String customerId) {
        log.info("Lấy thông tin khách hàng với ID: " + customerId);
        return customerRepository.findById(customerId);
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        log.info("Thêm đặt khách hàng mới với mã: " + customer.getId());
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(String customerId, Customer updateCustomer) {
        log.info("Cập nhật khách hàng có mã: " + customerId);
        return customerRepository.saveAndFlush(updateCustomer);
    }

    @Override
    public void deleteCustomerById(String customerId) {
        log.info("Xóa khách hàng với mã: {}", customerId);
        customerRepository.deleteById(customerId);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Customer> searchCustomersPageable(String name, String identification, String phoneNumber, Pageable pageable) {
        String normalizedName = (name != null && !name.trim().isEmpty()) ? name.trim() : null;
        String normalizedIdentification = (identification != null && !identification.trim().isEmpty()) ? identification.trim() : null;
        String normalizedPhoneNumber = (phoneNumber != null && !phoneNumber.trim().isEmpty()) ? phoneNumber.trim() : null;

        log.info("Tìm kiếm khách hàng - name: {}, identification: {}, phoneNumber: {}, phân trang: {}",
                normalizedName, normalizedIdentification, normalizedPhoneNumber, pageable);

        return customerRepository.findByFilters(normalizedName, normalizedIdentification, normalizedPhoneNumber, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Customer> searchCustomer(String name, String identification, String phoneNumber) {
        String normalizedName = (name != null && !name.trim().isEmpty()) ? name.trim() : null;
        String normalizedIdentification = (identification != null && !identification.trim().isEmpty()) ? identification.trim() : null;
        String normalizedPhoneNumber = (phoneNumber != null && !phoneNumber.trim().isEmpty()) ? phoneNumber.trim() : null;

        log.info("Tìm kiếm khách hàng - name: {}, identification: {}, phoneNumber: {}",
                normalizedName, normalizedIdentification, normalizedPhoneNumber);

        return customerRepository.findByFilters(normalizedName, normalizedIdentification, normalizedPhoneNumber);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Customer> getAllCustomersPageable(Pageable pageable) {
        log.info("Lấy danh sách khách hàng với phân trang: {}", pageable);
        return customerRepository.findAll(pageable);
    }
}
