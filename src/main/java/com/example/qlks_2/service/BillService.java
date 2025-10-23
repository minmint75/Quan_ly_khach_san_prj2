package com.example.qlks_2.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.qlks_2.entity.Bill;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BillService {
    List<Bill> getAllBills();
    Bill saveBill(Bill bill);
    Bill updateBill(Long billId, Bill updatedBill);
    void deleteBillById(Long billId);
    List<Bill> searchBill(Long bookingId, LocalDateTime startDate, LocalDateTime endDate, Bill.BillStatus status);
    Page<Bill> searchBillPageable(Long bookingId, LocalDateTime startDate, LocalDateTime endDate, Bill.BillStatus status, Pageable pageable);

}
