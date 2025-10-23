package com.example.qlks_2.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.qlks_2.dto.BillRequest;
import com.example.qlks_2.entity.Bill;
import com.example.qlks_2.entity.Bill.BillStatus;
import com.example.qlks_2.service.BillService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/bills")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    @GetMapping("/api/list")
    @ResponseBody
    public Page<Bill> getBillApi(
            @RequestParam(value = "bookingId", required = false) Long bookingId,
            @RequestParam(value = "startDate", required = false) LocalDateTime startDate,
            @RequestParam(value = "endDate", required = false) LocalDateTime endDate,
            @RequestParam(value = "status", required = false) BillStatus status,

            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "sortDir", required = false, defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return billService.searchBillPageable(bookingId, startDate, endDate, status, pageable);
    }


    @GetMapping("/api/{id:[0-9]+}")
    @ResponseBody
    public ResponseEntity<Bill> getBillApiDetail(@PathVariable Long id) {
        Optional<Bill> bill = billService.getAllBills().stream()
                .filter(b -> b.getBillId().equals(id))
                .findFirst();

        return bill.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === API: ADD (CREATE) ===

    @PostMapping("/api/add")
    @ResponseBody
    public ResponseEntity<?> apiAddBill(@Valid @RequestBody BillRequest billRequest) {
        try {
            Bill bill = billRequest.toEntity();
            Bill saved = billService.saveBill(bill);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            log.warn("Lỗi validation khi thêm hóa đơn: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("API add bill error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add bill");
        }
    }

    // === API: EDIT (UPDATE) ===

    @PutMapping("/api/edit/{id}")
    @ResponseBody
    public ResponseEntity<?> apiUpdateBill(@PathVariable Long id, @Valid @RequestBody BillRequest billRequest) {
        try {
            Bill billToUpdate = billRequest.toEntity();
            billToUpdate.setBillId(id); // Đảm bảo ID được thiết lập cho hàm saveAndFlush/update trong Service
            Bill updated = billService.updateBill(id, billToUpdate);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("API update bill error for ID {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update bill");
        }
    }

    // === API: DELETE BY ID ===

    // Dùng @DeleteMapping cho logic Delete
    @DeleteMapping("/api/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> apiDeleteBill(@PathVariable Long id) {
        try {
            billService.deleteBillById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("API delete bill error for ID {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete bill");
        }
    }


    // === LIST VIEW ===
    @GetMapping
    public String listBills(Model model) {
        List<Bill> bills = billService.getAllBills();
        model.addAttribute("bills", bills);
        model.addAttribute("statuses", BillStatus.values());

        return "bill/list";
    }

    // === DELETE MVC ===
    @PostMapping("/delete/{id}")
    public String deleteBillMvc(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            billService.deleteBillById(id);
            redirectAttributes.addFlashAttribute("success", "Xóa hóa đơn thành công!");
        } catch (Exception e) {
            log.error("Lỗi khi xóa hóa đơn ID {}: ", id, e);
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa hóa đơn!");
        }
        return "redirect:/bills";
    }
}