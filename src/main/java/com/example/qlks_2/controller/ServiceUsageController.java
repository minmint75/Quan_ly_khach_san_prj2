package com.example.qlks_2.controller;

import com.example.qlks_2.dto.ServiceUsageRequest;
import com.example.qlks_2.entity.ServiceUsage;
import com.example.qlks_2.entity.Service;
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
    public List<ServiceUsage> getAllApi(
            @RequestParam(value = "keyword", required = false) String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return serviceUsageService.searchByServiceName(keyword);
        }
        return serviceUsageService.getAll();
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
        model.addAttribute("services", serviceService.getAllServices());
        return "service_usage/form";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute ServiceUsageRequest req,
                      RedirectAttributes redirect) {
        try {
            Service service = serviceService.getServiceById(req.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found"));
            ServiceUsage su = req.toEntity(service);
            serviceUsageService.save(su);
            redirect.addFlashAttribute("success", "Thêm sử dụng dịch vụ thành công!");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi khi thêm: " + e.getMessage());
        }
        return "redirect:/service-usage";
    }

    @GetMapping("/edit/[id]")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        Optional<ServiceUsage> usage = serviceUsageService.getById(id);
        if (usage.isPresent()) {
            model.addAttribute("usage", usage.get());
            model.addAttribute("services", serviceService.getAllServices());
            return "service_usage/form";
        } else {
            redirect.addFlashAttribute("error", "Không tìm thấy bản ghi!");
            return "redirect:/service-usage";
        }
    }

    @PostMapping("/edit/[id]")
    public String edit(@PathVariable Long id,
                       @ModelAttribute ServiceUsageRequest req,
                       RedirectAttributes redirect) {
        try {
            Service service = serviceService.getServiceById(req.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found"));
            ServiceUsage su = req.toEntity(service);
            serviceUsageService.update(id, su);
            redirect.addFlashAttribute("success", "Cập nhật thành công!");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi khi cập nhật: " + e.getMessage());
        }
        return "redirect:/service-usage";
    }

    @PostMapping("/delete/[id]")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            serviceUsageService.delete(id);
            redirect.addFlashAttribute("success", "Xóa thành công!");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi khi xóa: " + e.getMessage());
        }
        return "redirect:/service-usage";
    }
}