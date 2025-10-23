package com.example.qlks_2.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.qlks_2.entity.Bill;
import com.example.qlks_2.repository.BillRepository;
import com.example.qlks_2.service.BillService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;

    @Override
    @Transactional(readOnly = true)
        public List<Bill> getAllBills(){
        log.info("Lấy danh sách tất cả hóa đơn");
        return billRepository.findAll();
    }

    @Override
    public Bill saveBill(Bill bill){
        log.info("Thêm hóa đơn mới : {}", bill);
        return billRepository.save(bill);
    }

    @Override
    public Bill updateBill(Long billId, Bill updateBill ){
        log.info("Cập nhật hóa đơn theo id: {}", billId);
        return billRepository.saveAndFlush(updateBill);
    }

    @Override
    public void deleteBillById(Long billId){
        log.info("Xóa hóa đơn  theo id: {}", billId);
        billRepository.deleteById(billId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Bill> searchBill(Long bookingId, LocalDateTime startDate, LocalDateTime endDate, Bill.BillStatus status) {

        if (bookingId != null && startDate != null && endDate != null) {
            return billRepository.findByFilters(bookingId, status, startDate, endDate);
        }

        if (bookingId != null) {
            return billRepository.findByBookingId(bookingId);
        }

        if (startDate != null && endDate != null) {
            return billRepository.findByCreatedAtBetween(startDate, endDate);
        }
        return billRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Bill> searchBillPageable(Long bookingId, LocalDateTime startDate, LocalDateTime endDate, Bill.BillStatus status, Pageable pageable){
        return billRepository.findByFiltersPageable(bookingId, status, startDate, endDate, pageable);
    }
}
