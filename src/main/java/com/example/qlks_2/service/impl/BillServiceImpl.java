package com.example.qlks_2.service.impl;

import com.example.qlks_2.entity.Booking;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.qlks_2.entity.Bill;
import com.example.qlks_2.repository.BillRepository;
import com.example.qlks_2.service.BillService;

import java.util.List;
import java.util.Optional;

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


}
