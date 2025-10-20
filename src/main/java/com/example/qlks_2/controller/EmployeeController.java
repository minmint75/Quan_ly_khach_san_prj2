package com.example.qlks_2.controller;

import com.example.qlks_2.dto.EmployeeRequest;
import com.example.qlks_2.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.qlks_2.service.EmployeeService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // === API LIST (chỉ tìm kiếm theo tên, chức vụ, email) ===
    @GetMapping("/api/list")
    @ResponseBody
    public List<Employee> getEmployeeApi(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "email", required = false) String email) {

        if (name != null || role != null || email != null) {
            return employeeService.searchEmployees(name, role, email);
        }
        return employeeService.getAllEmployees();
    }

    // Trả về giao diện HTML
    @GetMapping
    public String listEmployees(Model model) {
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "employee/list";
    }

    // Xem chi tiết 1 nhân viên
    @GetMapping("/{id}")
    public String viewEmployee(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Employee> employee = employeeService.getEmployeeByIdOptional(id);

        if (employee.isPresent()) {
            model.addAttribute("employee", employee.get());
            return "employee/detail";
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy nhân viên với ID: " + id);
            return "redirect:/employee";
        }
    }

    // === API DETAIL ===
    @GetMapping("/api/{id}")
    @ResponseBody
    public Employee getEmployeeApi(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    // === ADD FORM ===
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employeeRequest", new EmployeeRequest());
        return "employee/form";
    }

    // Xử lý khi người dùng bấm “Lưu”
    @PostMapping("/add")
    public String addEmployee(@ModelAttribute EmployeeRequest employeeRequest,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {

        log.info("Xử lý thêm nhân viên mới: {}", employeeRequest);

        // Validate logic
        if (employeeRequest.getName() == null || employeeRequest.getName().trim().isEmpty()) {
            bindingResult.rejectValue("name", "error.name", "Tên nhân viên không được để trống");
        }

        if (bindingResult.hasErrors()) {
            return "employee/form";
        }

        try {
            Employee employee = employeeRequest.toEntity();
            employeeService.saveEmployee(employee);
            redirectAttributes.addFlashAttribute("success", "Thêm nhân viên thành công!");
            return "redirect:/employee";
        } catch (Exception e) {
            log.error("Lỗi khi thêm nhân viên: ", e);
            model.addAttribute("error", "Có lỗi xảy ra khi thêm nhân viên!");
            return "employee/form";
        }
    }

    // Hiển thị form sửa nhân viên
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Employee> employee = employeeService.getEmployeeByIdOptional(id);

        if (employee.isPresent()) {
            EmployeeRequest employeeRequest = EmployeeRequest.fromEntity(employee.get());
            model.addAttribute("employeeRequest", employeeRequest);
            return "employee/form";
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy nhân viên với ID: " + id);
            return "redirect:/employee";
        }
    }

    // Xử lý khi nhấn lưu sau khi sửa
    @PostMapping("/edit/{id}")
    public String updateEmployee(@PathVariable Long id,
                                 @ModelAttribute EmployeeRequest employeeRequest,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        log.info("Cập nhật nhân viên ID {}: {}", id, employeeRequest);

        if (employeeRequest.getName() == null || employeeRequest.getName().trim().isEmpty()) {
            bindingResult.rejectValue("name", "error.name", "Tên nhân viên không được để trống");
        }

        if (bindingResult.hasErrors()) {
            return "employee/form";
        }

        try {
            Employee employee = employeeRequest.toEntity();
            employee.setEmployeeId(id);
            employeeService.saveEmployee(employee);
            redirectAttributes.addFlashAttribute("success", "Cập nhật nhân viên thành công!");
            return "redirect:/employee";
        } catch (Exception e) {
            log.error("Lỗi khi cập nhật nhân viên ID {}: ", id, e);
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật nhân viên!");
            return "employee/form";
        }
    }

    // Xóa nhân viên
    @PostMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            employeeService.deleteEmployeeById(id);
            redirectAttributes.addFlashAttribute("success", "Xóa nhân viên thành công!");
        } catch (Exception e) {
            log.error("Lỗi khi xóa nhân viên ID {}: ", id, e);
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa nhân viên!");
        }
        return "redirect:/employee";
    }
}
