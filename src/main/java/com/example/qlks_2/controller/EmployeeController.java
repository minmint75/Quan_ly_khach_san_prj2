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

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("roles", Employee.EmployeeRole.values());
        model.addAttribute("shifts", Employee.EmployeeShift.values());
        model.addAttribute("statuses", Employee.EmployeeStatus.values());
    }

    @GetMapping("/api/list")
    @ResponseBody
    public List<Employee> getEmployeeApi(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "role", required = false) Employee.EmployeeRole role,
            @RequestParam(value = "shift", required = false) Employee.EmployeeShift shift,
            @RequestParam(value = "status", required = false) Employee.EmployeeStatus status,
            @RequestParam(value = "email", required = false) String email) {

        if (name != null || role != null || shift != null || status != null || email != null) {
            return employeeService.searchEmployees(name, role, shift, status, email);
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

    // === API ADD ===
    @PostMapping("/api/add")
    @ResponseBody
    public Employee addEmployeeApi(@RequestBody EmployeeRequest employeeRequest) {
        log.info("API: Thêm nhân viên mới: {}", employeeRequest);
        
        if (employeeRequest.getName() == null || employeeRequest.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên nhân viên không được để trống");
        }
        
        Employee employee = employeeRequest.toEntity();
        return employeeService.saveEmployee(employee);
    }

    // === API UPDATE ===
    @PutMapping("/api/{id}")
    @ResponseBody
    public Employee updateEmployeeApi(@PathVariable Long id, @RequestBody EmployeeRequest employeeRequest) {
        log.info("API: Cập nhật nhân viên ID {}: {}", id, employeeRequest);
        
        if (employeeRequest.getName() == null || employeeRequest.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên nhân viên không được để trống");
        }
        
        Employee employee = employeeRequest.toEntity();
        employee.setEmployeeId(id);
        return employeeService.saveEmployee(employee);
    }

    // === API DELETE ===
    @DeleteMapping("/api/{id}")
    @ResponseBody
    public void deleteEmployeeApi(@PathVariable Long id) {
        log.info("API: Xóa nhân viên ID {}", id);
        employeeService.deleteEmployeeById(id);
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
