package dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import entity.Customer;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {

    private String customerId;
    private String name;
    private String identification;
    private String phoneNumber;
    private String email;
    private String nationality;
    private String address;


    public Customer toEntity() {
        // Khởi tạo Entity Customer mới
        Customer customer = new Customer();

        // Ánh xạ các trường từ DTO sang Entity
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