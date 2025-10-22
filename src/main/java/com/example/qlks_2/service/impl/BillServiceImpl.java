package com.example.qlks_2.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.qlks_2.entity.Bill;
import com.example.qlks_2.repository.BillRepository;
import com.example.qlks_2.service.BillService;

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



}
