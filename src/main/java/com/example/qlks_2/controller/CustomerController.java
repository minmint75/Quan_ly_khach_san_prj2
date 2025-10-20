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

@Slf4j
@Controller
@RequestMapping("/customer")

public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // === API LIST ===
    @GetMapping("/api/list")
    @ResponseBody
    public List<Customer> getCustomerApi(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "identification", required = false, defaultValue = "0") Integer identification,
            @RequestParam(value = "phoneNumber", required = false, defaultValue = "0") Integer phoneNumber) {

        if ((name != null && !name.trim().isEmpty()) || identification > 0 || phoneNumber > 0) {
            return customerService.searchCustomersPageable(name, identification, phoneNumber);
        }
        return customerService.getAllCustomers();
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
}