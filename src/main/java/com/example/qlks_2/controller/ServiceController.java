package com.example.qlks_2.controller;

import com.example.qlks_2.entity.Service;
import com.example.qlks_2.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "*")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    // 1️⃣ Lấy danh sách dịch vụ (phân trang)
    @GetMapping
    public Page<Service> getAllServices(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        return serviceService.getAllServices(page, size);
    }

    // 2️⃣ Thêm dịch vụ mới
    @PostMapping
    public Service addService(@RequestBody Service service) {
        return serviceService.addService(service);
    }

    // 3️⃣ Cập nhật dịch vụ
    @PutMapping("/{id}")
    public Optional<Service> updateService(@PathVariable Long id, @RequestBody Service updatedService) {
        return serviceService.updateService(id, updatedService);
    }

    // 4️⃣ Xóa dịch vụ
    @DeleteMapping("/{id}")
    public String deleteService(@PathVariable Long id) {
        return serviceService.deleteService(id) ? "Deleted successfully" : "Service not found";
    }

    // 5️⃣ Tìm kiếm dịch vụ
    @GetMapping("/search")
    public Page<Service> searchServices(@RequestParam String keyword,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        return serviceService.searchServices(keyword, page, size);
    }

    // 6️⃣ Sắp xếp theo tên
    @GetMapping("/sort/name")
    public Page<Service> sortByName(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        return serviceService.sortByNameAsc(page, size);
    }

    // 7️⃣ Sắp xếp theo giá
    @GetMapping("/sort/price")
    public Page<Service> sortByPrice(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        return serviceService.sortByPriceAsc(page, size);
    }
}
