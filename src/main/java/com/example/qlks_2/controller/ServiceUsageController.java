package com.example.qlks_2.controller;

import com.example.qlks_2.dto.ServiceUsageRequest;
import com.example.qlks_2.entity.ServiceEntity;
import com.example.qlks_2.entity.ServiceUsage;
import com.example.qlks_2.service.ServiceUsageService;
import com.example.qlks_2.service.ServiceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/service-usage")
@RequiredArgsConstructor
public class ServiceUsageController {

    private final ServiceUsageService serviceUsageService;
    private final ServiceService serviceService;

    @GetMapping("/api/list")
    @ResponseBody
    public ResponseEntity<?> getAllApi(
            @RequestParam(value = "keyword", required = false) String keyword) {
        try {
            if (keyword != null && !keyword.isEmpty()) {
                return ResponseEntity.ok(serviceUsageService.searchByServiceName(keyword));
            }
            return ResponseEntity.ok(serviceUsageService.getAll());
        } catch (Exception e) {
            log.error("Error getting service usage list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi: " + e.getMessage());
        }
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<?> getByIdApi(@PathVariable Long id) {
        Optional<ServiceUsage> usage = serviceUsageService.getById(id);
        if (usage.isPresent()) {
            return ResponseEntity.ok(usage.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy bản ghi!");
    }

    @PostMapping("/api/add")
    @ResponseBody
    public ResponseEntity<?> addApi(@RequestBody ServiceUsageRequest req) {
        try {
            req.validateData();
            ServiceEntity serviceEntity = serviceService.getServiceById(req.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found"));
            ServiceUsage su = req.toEntity(serviceEntity);
            ServiceUsage saved = serviceUsageService.save(su);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            log.error("Error adding service usage", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/api/edit/{id}")
    @ResponseBody
    public ResponseEntity<?> editApi(@PathVariable Long id, @RequestBody ServiceUsageRequest req) {
        try {
            req.validateData();
            ServiceEntity serviceEntity = serviceService.getServiceById(req.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found"));
            ServiceUsage su = req.toEntity(serviceEntity);
            ServiceUsage updated = serviceUsageService.update(id, su);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("Error updating service usage with id {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/api/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteApi(@PathVariable Long id) {
        try {
            serviceUsageService.delete(id);
            return ResponseEntity.ok("Xóa thành công!");
        } catch (Exception e) {
            log.error("Error deleting service usage with id {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public String list(Model model) {
        List<ServiceUsage> usages = serviceUsageService.getAll();
        model.addAttribute("usages", usages);
        return "service_usage/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        Optional<ServiceUsage> usage = serviceUsageService.getById(id);
        if (usage.isPresent()) {
            model.addAttribute("usage", usage.get());
            return "service_usage/detail";
        } else {
            redirect.addFlashAttribute("error", "Không tìm thấy bản ghi!");
            return "redirect:/service-usage";
        }
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("usageRequest", new ServiceUsageRequest());
        // Giả định serviceService.getAllServices() trả về List<ServiceEntity>
        model.addAttribute("services", serviceService.getAllServices());
        return "service_usage/form";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute ServiceUsageRequest req,
                      RedirectAttributes redirect) {
        try {
            ServiceEntity serviceEntity = serviceService.getServiceById(req.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found"));
            ServiceUsage su = req.toEntity(serviceEntity);
            serviceUsageService.save(su);
            redirect.addFlashAttribute("success", "Thêm sử dụng dịch vụ thành công!");
        } catch (Exception e) {
            log.error("Error adding service usage", e);
            redirect.addFlashAttribute("error", "Lỗi khi thêm: " + e.getMessage());
        }
        return "redirect:/service-usage";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        Optional<ServiceUsage> usage = serviceUsageService.getById(id);
        if (usage.isPresent()) {
            model.addAttribute("usage", usage.get());
            // Giả định serviceService.getAllServices() trả về List<ServiceEntity>
            model.addAttribute("services", serviceService.getAllServices());
            return "service_usage/form";
        } else {
            redirect.addFlashAttribute("error", "Không tìm thấy bản ghi!");
            return "redirect:/service-usage";
        }
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @ModelAttribute ServiceUsageRequest req,
                       RedirectAttributes redirect) {
        try {
            ServiceEntity serviceEntity = serviceService.getServiceById(req.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found"));
            ServiceUsage su = req.toEntity(serviceEntity);
            serviceUsageService.update(id, su);
            redirect.addFlashAttribute("success", "Cập nhật thành công!");
        } catch (Exception e) {
            log.error("Error updating service usage with id {}", id, e);
            redirect.addFlashAttribute("error", "Lỗi khi cập nhật: " + e.getMessage());
        }
        return "redirect:/service-usage";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            serviceUsageService.delete(id);
            redirect.addFlashAttribute("success", "Xóa thành công!");
        } catch (Exception e) {
            log.error("Error deleting service usage with id {}", id, e);
            redirect.addFlashAttribute("error", "Lỗi khi xóa: " + e.getMessage());
        }
        return "redirect:/service-usage";
    }
}
