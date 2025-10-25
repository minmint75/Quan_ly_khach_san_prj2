package com.example.qlks_2.controller;

import com.example.qlks_2.dto.CustomerRequest;
import com.example.qlks_2.entity.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.qlks_2.service.CustomerService;

import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.HashMap;

@Slf4j
@Controller
@RequestMapping("/customer")
@CrossOrigin(origins = "http://localhost:3000")

public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // === DTO CLASSES FOR JSON API ===
    
    // DTO for frontend JSON requests
    public static class CustomerJsonRequest {
        @JsonProperty("fullName")
        private String fullName;

        @JsonProperty("nationalId")
        private String nationalId;

        @JsonProperty("phone")
        private String phone;

        @JsonProperty("email")
        private String email;

        @JsonProperty("nationality")
        private String nationality;

        @JsonProperty("address")
        private String address;

        // Getters and setters
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }

        public String getNationalId() { return nationalId; }
        public void setNationalId(String nationalId) { this.nationalId = nationalId; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getNationality() { return nationality; }
        public void setNationality(String nationality) { this.nationality = nationality; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }

        public CustomerRequest toCustomerRequest() {
            CustomerRequest request = new CustomerRequest();
            request.setName(this.fullName);
            try {
                request.setIdentification(Long.parseLong(this.nationalId));
            } catch (NumberFormatException e) {
                request.setIdentification(0L);
            }
            try {
                request.setPhoneNumber(Long.parseLong(this.phone));
            } catch (NumberFormatException e) {
                request.setPhoneNumber(0L);
            }
            request.setEmail(this.email);
            request.setNationality(this.nationality);
            request.setAddress(this.address);
            return request;
        }
    }

    // DTO for frontend JSON responses
    public static class CustomerJsonResponse {
        @JsonProperty("customerId")
        private Long customerId;

        @JsonProperty("fullName")
        private String fullName;

        @JsonProperty("nationalId")
        private String nationalId;

        @JsonProperty("phone")
        private String phone;

        @JsonProperty("email")
        private String email;

        @JsonProperty("nationality")
        private String nationality;

        @JsonProperty("address")
        private String address;

        public CustomerJsonResponse(Customer customer) {
            this.customerId = customer.getCustomerId();
            this.fullName = customer.getName();
            this.nationalId = String.valueOf(customer.getIdentification());
            this.phone = String.valueOf(customer.getPhoneNumber());
            this.email = customer.getEmail();
            this.nationality = customer.getNationality();
            this.address = customer.getAddress();
        }

        // Getters and setters
        public Long getCustomerId() { return customerId; }
        public void setCustomerId(Long customerId) { this.customerId = customerId; }

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }

        public String getNationalId() { return nationalId; }
        public void setNationalId(String nationalId) { this.nationalId = nationalId; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getNationality() { return nationality; }
        public void setNationality(String nationality) { this.nationality = nationality; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
    }

    // === API LIST ===
    @GetMapping("/api/list")
    @ResponseBody
    public List<CustomerJsonResponse> getCustomerApi(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "identification", required = false) Integer identification,
            @RequestParam(value = "phoneNumber", required = false) Integer phoneNumber) {

        List<Customer> customers;
        if ((name != null && !name.trim().isEmpty()) || 
            (identification != null && identification > 0) || 
            (phoneNumber != null && phoneNumber > 0)) {
            customers = customerService.searchCustomers(name, 
                identification != null ? identification : 0, 
                phoneNumber != null ? phoneNumber : 0);
        } else {
            customers = customerService.getAllCustomers();
        }
        
        return customers.stream()
                .map(CustomerJsonResponse::new)
                .collect(java.util.stream.Collectors.toList());
    }

    // === LIST VIEW ===
    @GetMapping
    public String listCustomers(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "customer/list";
    }

    // === VIEW DETAIL ===
    @GetMapping("/{id}")
    public String viewCustomer(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Customer> customer = customerService.getCustomerById(id);

        if (customer.isPresent()) {
            model.addAttribute("customer", customer.get());
            return "customer/detail";
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy khách hàng với ID: " + id);
            return "redirect:/customer";
        }
    }

    // === API DETAIL ===
    @GetMapping("/api/{id}")
    @ResponseBody
    public Customer getCustomerApi(@PathVariable Long id) {
        return customerService.getCustomerById(id).orElse(null);
    }


    // === ADD FORM ===
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("customerRequest", new CustomerRequest());
        return "customer/form";
    }

    // === ADD HANDLER ===
    @PostMapping("/add")
    public String addCustomer(@ModelAttribute CustomerRequest customerRequest,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {

        log.info("Xử lý thêm khách hàng mới: {}", customerRequest);
        // Validate logic
        if (customerRequest.getName() == null || customerRequest.getName().trim().isEmpty()) {
            bindingResult.rejectValue("name", "error.name", "Tên khách hàng không được để trống");
        }

        if (customerRequest.getIdentification() <= 0) {
            bindingResult.rejectValue("identification", "error.identification", "Số CCCD không được để trống");
        }

        if (customerRequest.getPhoneNumber() <= 0) {
            bindingResult.rejectValue("phoneNumber", "error.phoneNumber", "Số điện thoại không được để trống");
        }


        if (bindingResult.hasErrors()) {
            return "customer/form";
        }

        try {
            Customer customer = customerRequest.toEntity();
            customerService.saveCustomer(customer);
            redirectAttributes.addFlashAttribute("success", "Thêm khách hàng thành công!");
            return "redirect:/customer";
        } catch (Exception e) {
            log.error("Lỗi khi thêm khách hàng: ", e);
            model.addAttribute("error", "Có lỗi xảy ra khi thêm khách hàng!");
            return "customer/form";
        }
    }

    // === EDIT FORM ===
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Customer> customer = customerService.getCustomerById(id);

        if (customer.isPresent()) {
            CustomerRequest customerRequest = CustomerRequest.fromEntity(customer.get());
            model.addAttribute("customerRequest", customerRequest);
            return "customer/form";
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy khách hàng với ID: " + id);
            return "redirect:/customer";
        }
    }

    // === EDIT HANDLER ===
    @PostMapping("/edit/{id}")
    public String updateCustomer(@PathVariable Long id,
                                 @ModelAttribute CustomerRequest customerRequest,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        log.info("Cập nhật khách hàng ID {}: {}", id, customerRequest);

        // === Validation Thủ Công (Chỉ kiểm tra tính bắt buộc) ===
        if (customerRequest.getName() == null || customerRequest.getName().trim().isEmpty()) {
            bindingResult.rejectValue("name", "error.name", "Tên khách hàng không được để trống");
        }

        // Identification Validation (Chỉ kiểm tra bắt buộc)
        if (customerRequest.getIdentification() <= 0) {
            bindingResult.rejectValue("identification", "error.identification", "Số căn cước/hộ chiếu không được để trống");
        }

        // PhoneNumber Validation (Chỉ kiểm tra bắt buộc)
        if (customerRequest.getPhoneNumber() <= 0) {
            bindingResult.rejectValue("phoneNumber", "error.phoneNumber", "Số điện thoại không được để trống");
        }

        if (bindingResult.hasErrors()) {
            return "customer/form";
        }

        try {
            Customer customer = customerRequest.toEntity();
            customer.setCustomerId(id);
            customerService.saveCustomer(customer);
            redirectAttributes.addFlashAttribute("success", "Cập nhật khách hàng thành công!");
            return "redirect:/customer";
        } catch (Exception e) {
            log.error("Lỗi khi cập nhật khách hàng ID {}: ", id, e);
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật khách hàng!");
            return "customer/form";
        }
    }

    // === DELETE ===
    @PostMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            customerService.deleteCustomerById(id);
            redirectAttributes.addFlashAttribute("success", "Xóa khách hàng thành công!");
        } catch (Exception e) {
            log.error("Lỗi khi xóa khách hàng ID {}: ", id, e);
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa khách hàng!");
        }
        return "redirect:/customer";
    }

    // === JSON API ENDPOINTS FOR FRONTEND ===
    
    // === TEST ENDPOINT ===
    @GetMapping("/api/test")
    @ResponseBody
    public ResponseEntity<String> testApi() {
        return ResponseEntity.ok("API is working!");
    }

    // === API ADD (JSON) ===
    @PostMapping("/api/add")
    @ResponseBody
    public ResponseEntity<?> addCustomerApi(@RequestBody CustomerJsonRequest jsonRequest) {
        log.info("API: Thêm khách hàng mới: {}", jsonRequest != null ? jsonRequest.getFullName() : "null");

        try {
            if (jsonRequest == null) {
                return ResponseEntity.badRequest().body("Dữ liệu không hợp lệ");
            }

            log.info("Request data - Name: {}, CCCD: {}, Phone: {}", 
                jsonRequest.getFullName(), jsonRequest.getNationalId(), jsonRequest.getPhone());

            CustomerRequest customerRequest = jsonRequest.toCustomerRequest();

            // Validation
            if (customerRequest.getName() == null || customerRequest.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Tên khách hàng không được để trống");
            }
            if (customerRequest.getIdentification() <= 0) {
                return ResponseEntity.badRequest().body("Số CCCD không được để trống");
            }
            if (customerRequest.getPhoneNumber() <= 0) {
                return ResponseEntity.badRequest().body("Số điện thoại không được để trống");
            }

            log.info("Creating customer entity...");
            Customer customer = customerRequest.toEntity();
            log.info("Saving customer...");
            Customer savedCustomer = customerService.saveCustomer(customer);
            log.info("Customer saved successfully with ID: {}", savedCustomer.getCustomerId());

            return ResponseEntity.ok(new CustomerJsonResponse(savedCustomer));
        } catch (Exception e) {
            log.error("Lỗi khi thêm khách hàng qua API: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Có lỗi xảy ra khi thêm khách hàng: " + e.getMessage());
        }
    }

    // === API DETAIL (JSON) ===
    @GetMapping("/api/detail/{id}")
    @ResponseBody
    public ResponseEntity<?> getCustomerDetailApi(@PathVariable Long id) {
        Optional<Customer> customer = customerService.getCustomerById(id);

        if (customer.isPresent()) {
            return ResponseEntity.ok(new CustomerJsonResponse(customer.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // === API UPDATE (JSON) ===
    @PostMapping("/api/update/{id}")
    @ResponseBody
    public ResponseEntity<?> updateCustomerApi(@PathVariable Long id, @RequestBody CustomerJsonRequest jsonRequest) {
        log.info("API: Cập nhật khách hàng ID {}: {}", id, jsonRequest.getFullName());

        try {
            CustomerRequest customerRequest = jsonRequest.toCustomerRequest();

            // Validation
            if (customerRequest.getName() == null || customerRequest.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Tên khách hàng không được để trống");
            }
            if (customerRequest.getIdentification() <= 0) {
                return ResponseEntity.badRequest().body("Số CCCD không được để trống");
            }
            if (customerRequest.getPhoneNumber() <= 0) {
                return ResponseEntity.badRequest().body("Số điện thoại không được để trống");
            }

            Customer customer = customerRequest.toEntity();
            customer.setCustomerId(id);
            Customer updatedCustomer = customerService.saveCustomer(customer);

            return ResponseEntity.ok(new CustomerJsonResponse(updatedCustomer));
        } catch (Exception e) {
            log.error("Lỗi khi cập nhật khách hàng ID {} qua API: ", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Có lỗi xảy ra khi cập nhật khách hàng!");
        }
    }

    // === API DELETE (JSON) ===
    @PostMapping("/api/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteCustomerApi(@PathVariable Long id) {
        try {
            customerService.deleteCustomerById(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Xóa khách hàng thành công!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Lỗi khi xóa khách hàng ID {} qua API: ", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Có lỗi xảy ra khi xóa khách hàng!");
        }
    }
}