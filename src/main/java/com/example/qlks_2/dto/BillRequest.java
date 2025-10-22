package com.example.qlks_2.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.example.qlks_2.entity.Bill;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillRequest {

    private Long billId;
    private Long bookingId;
    private BigDecimal roomFee;
    private BigDecimal serviceFee;
    private BigDecimal total;
    private Bill.BillStatus billStatus;

    public Bill toEntity() {
        Bill bill = new Bill();
        bill.setBillId(this.billId);
        bill.setBookingId(this.bookingId);
        bill.setRoomFee(this.roomFee);
        bill.setServiceFee(this.serviceFee);
        bill.setTotal(this.total);
        bill.setStatus(this.billStatus);
        return bill;
    }

    public static BillRequest fromEntity(Bill bill) {
        BillRequest request = new BillRequest();
        request.setBillId(bill.getBillId());
        request.setBookingId(bill.getBookingId());
        request.setRoomFee(bill.getRoomFee());
        request.setServiceFee(bill.getServiceFee());
        request.setTotal(bill.getTotal());
        request.setBillStatus(bill.getStatus());
        return request;
    }

    public void validateData() {
        if (bookingId == null) {
            throw new IllegalArgumentException("Mã booking không được để trống.");
        }
        if (total != null ) {
            throw new IllegalArgumentException("Tổng tiền không hợp lệ.");
        }
    }
}
