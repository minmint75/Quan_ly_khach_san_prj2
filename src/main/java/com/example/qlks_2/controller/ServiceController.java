package com.example.qlks_2.controller;

import com.example.qlks_2.dto.ServiceRequest;
import com.example.qlks_2.dto.ServiceSearchRequest;
import com.example.qlks_2.entity.ServiceEntity;
import com.example.qlks_2.entity.ServiceEntity.ServiceStatus;
import com.example.qlks_2.entity.ServiceEntity.ServiceType;
import com.example.qlks_2.service.ServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/service")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    // Helper: Lấy danh sách các giá trị Enum hiển thị (Đã dịch)
    private List<String> getServiceTypeDisplayNames() {
        return Arrays.stream(ServiceType.values())
                .map(ServiceType::getDisplayName)
                .collect(Collectors.toList());
    }

    private List<String> getServiceStatusDisplayNames() {
        return Arrays.stream(ServiceStatus.values())
                .map(ServiceStatus::getDisplayName)
                .collect(Collectors.toList());
    }

    // === API LIST (Dùng phân trang và tìm kiếm) ===
    @GetMapping("/api/list")
    @ResponseBody
    public Page<ServiceEntity> getServiceApi(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "loaiDichVu", required = false) ServiceType loaiDichVu,
            @RequestParam(value = "status", required = false) ServiceStatus status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "serviceId") String sortBy,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction) {

        log.info("API: Tìm kiếm dịch vụ - Keyword: {}, Loại: {}, Trạng thái: {}, Page: {}", keyword, loaiDichVu, status, page);

        // Tạo Pageable
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        // Gọi service để tìm kiếm/lọc
        return serviceService.searchServicesPageable(keyword, loaiDichVu, status, pageable);
    }

    // === LIST VIEW (Dùng cho giao diện Thymeleaf, nếu có) ===
    @GetMapping
    public String listServices(Model model) {
        // Tải trang đầu tiên
        Pageable pageable = PageRequest.of(0, 10, Sort.by("serviceId").descending());
        Page<ServiceEntity> servicesPage = serviceService.searchServicesPageable(null, null, null, pageable);

        model.addAttribute("servicePage", servicesPage);
        model.addAttribute("statuses", ServiceStatus.values());
        model.addAttribute("types", ServiceType.values());
        model.addAttribute("searchRequest", new ServiceSearchRequest());
        return "service/list";
    }

    // === VIEW DETAIL ===
    @GetMapping("/{id}")
    public String viewService(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<ServiceEntity> service = serviceService.getServiceById(id);

        if (service.isPresent()) {
            model.addAttribute("service", service.get());
            return "service/detail";
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy dịch vụ với ID: " + id);
            return "redirect:/service";
        }
    }

    // === API DETAIL ===
    @GetMapping("/api/{id}")
    @ResponseBody
    public ServiceEntity getServiceApi(@PathVariable Long id) {
        return serviceService.getServiceById(id).orElse(null);
    }

    // === ADD FORM ===
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("serviceRequest", new ServiceRequest());
        model.addAttribute("statuses", ServiceStatus.values());
        model.addAttribute("types", ServiceType.values());
        return "service/form";
    }

    // === ADD HANDLER ===
    @PostMapping("/add")
    public String addService(@ModelAttribute ServiceRequest serviceRequest,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        log.info("Thêm dịch vụ mới: {}", serviceRequest.getTenDichVu());

        // Validation thủ công (theo mẫu BookingController và Entity Validation)
        if (serviceRequest.getTenDichVu() == null || serviceRequest.getTenDichVu().trim().isEmpty()) {
            bindingResult.rejectValue("tenDichVu", "error.tenDichVu", "Tên dịch vụ không được để trống");
        }
        if (serviceRequest.getGia() == null || serviceRequest.getGia().compareTo(BigDecimal.ZERO) < 0) {
            bindingResult.rejectValue("gia", "error.gia", "Giá dịch vụ phải là số dương");
        }
        if (serviceRequest.getLoaiDichVu() == null) {
            bindingResult.rejectValue("loaiDichVu", "error.loaiDichVu", "Loại dịch vụ không được để trống");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", ServiceStatus.values());
            model.addAttribute("types", ServiceType.values());
            return "service/form";
        }

        try {
            ServiceEntity service = serviceRequest.toEntity();
            serviceService.saveService(service);
            redirectAttributes.addFlashAttribute("success", "Thêm dịch vụ thành công!");
            return "redirect:/service";
        } catch (Exception e) {
            log.error("Lỗi khi thêm dịch vụ: ", e);
            model.addAttribute("error", "Có lỗi xảy ra khi thêm dịch vụ!");
            model.addAttribute("statuses", ServiceStatus.values());
            model.addAttribute("types", ServiceType.values());
            return "service/form";
        }
    }

    // === EDIT FORM ===
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<ServiceEntity> service = serviceService.getServiceById(id);

        if (service.isPresent()) {
            ServiceRequest serviceRequest = ServiceRequest.fromEntity(service.get());
            model.addAttribute("serviceRequest", serviceRequest);
            model.addAttribute("statuses", ServiceStatus.values());
            model.addAttribute("types", ServiceType.values());
            return "service/form";
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy dịch vụ với ID: " + id);
            return "redirect:/service";
        }
    }

    // === EDIT HANDLER ===
    @PostMapping("/edit/{id}")
    public String updateService(@PathVariable Long id,
                                @ModelAttribute ServiceRequest serviceRequest,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        log.info("Cập nhật dịch vụ ID {}: {}", id, serviceRequest.getTenDichVu());

        // Validation thủ công
        if (serviceRequest.getTenDichVu() == null || serviceRequest.getTenDichVu().trim().isEmpty()) {
            bindingResult.rejectValue("tenDichVu", "error.tenDichVu", "Tên dịch vụ không được để trống");
        }
        if (serviceRequest.getGia() == null || serviceRequest.getGia().compareTo(BigDecimal.ZERO) < 0) {
            bindingResult.rejectValue("gia", "error.gia", "Giá dịch vụ phải là số dương");
        }
        if (serviceRequest.getLoaiDichVu() == null) {
            bindingResult.rejectValue("loaiDichVu", "error.loaiDichVu", "Loại dịch vụ không được để trống");
        }


        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", ServiceStatus.values());
            model.addAttribute("types", ServiceType.values());
            return "service/form";
        }

        try {
            ServiceEntity service = serviceRequest.toEntity();
            service.setServiceId(id); // Đảm bảo ID được thiết lập cho việc cập nhật
            serviceService.updateService(id, service); // Sử dụng updateService
            redirectAttributes.addFlashAttribute("success", "Cập nhật dịch vụ thành công!");
            return "redirect:/service";
        } catch (Exception e) {
            log.error("Lỗi khi cập nhật dịch vụ ID {}: ", id, e);
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật dịch vụ!");
            model.addAttribute("statuses", ServiceStatus.values());
            model.addAttribute("types", ServiceType.values());
            return "service/form";
        }
    }

    // === DELETE ===
    @PostMapping("/delete/{id}")
    public String deleteService(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            serviceService.deleteService(id);
            redirectAttributes.addFlashAttribute("success", "Xóa dịch vụ thành công!");
        } catch (Exception e) {
            log.error("Lỗi khi xóa dịch vụ ID {}: ", id, e);
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa dịch vụ!");
        }
        return "redirect:/service";
    }

    @ResponseBody
    @RequestMapping("/api/test")
    public String test() {
        return "Service Backend is running!";
    }
}