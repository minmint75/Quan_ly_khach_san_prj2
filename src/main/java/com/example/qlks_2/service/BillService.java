package com.example.qlks_2.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.qlks_2.entity.Bill;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BillService {
    List<Bill> getAllBills();
}
