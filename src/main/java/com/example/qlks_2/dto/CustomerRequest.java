package com.example.qlks_2.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.example.qlks_2.entity.Customer;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {

    private Long customerId;
    private String name;
    private long identification;
    private long phoneNumber;
    private String email;
    private String nationality;
    private String address;

    public static CustomerRequest fromEntity(Customer customer) {
        if (customer == null) {
            return null;
        }
        return new CustomerRequest(
                customer.getCustomerId(),
                customer.getName(),
                customer.getIdentification(),
                customer.getPhoneNumber(),
                customer.getEmail(),
                customer.getNationality(),
                customer.getAddress()
        );
    }

    public Customer toEntity() {
        Customer customer = new Customer();
        customer.setCustomerId(this.customerId);
        customer.setName(this.name);
        customer.setIdentification(this.identification);
        customer.setPhoneNumber(this.phoneNumber);
        customer.setEmail(this.email);
        customer.setNationality(this.nationality);
        customer.setAddress(this.address);
        return customer;
    }
}
