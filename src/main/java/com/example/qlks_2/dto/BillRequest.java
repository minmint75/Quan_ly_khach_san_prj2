package com.example.qlks_2.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.example.qlks_2.entity.Bill;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillRequest {

    private Long billId;
    private Long bookingId;
    private BigDecimal roomFee;
    private BigDecimal serviceFee;
    private BigDecimal tax;
    private BigDecimal total;
    private Bill.BillStatus billStatus;
    private LocalDateTime createdAt;

    public Bill toEntity() {
        Bill bill = new Bill();
        bill.setBillId(this.billId);
        bill.setBookingId(this.bookingId);
        bill.setRoomFee(this.roomFee);
        bill.setServiceFee(this.serviceFee);
        // Always compute tax and total at 10%
        BigDecimal subtotal = (this.roomFee != null ? this.roomFee : BigDecimal.ZERO)
                .add(this.serviceFee != null ? this.serviceFee : BigDecimal.ZERO);
        BigDecimal computedTax = subtotal.multiply(new BigDecimal("0.10"));
        BigDecimal computedTotal = subtotal.add(computedTax);
        bill.setTax(computedTax);
        bill.setTotal(computedTotal);
        bill.setStatus(this.billStatus);
        bill.setCreatedAt(this.createdAt);
        return bill;
    }

    public static BillRequest fromEntity(Bill bill) {
        BillRequest request = new BillRequest();
        request.setBillId(bill.getBillId());
        request.setBookingId(bill.getBookingId());
        request.setRoomFee(bill.getRoomFee());
        request.setServiceFee(bill.getServiceFee());
        request.setTax(bill.getTax());
        request.setTotal(bill.getTotal());
        request.setBillStatus(bill.getStatus());
        request.setCreatedAt(bill.getCreatedAt());
        return request;
    }

    public void validateData() {
        if (bookingId == null) {
            throw new IllegalArgumentException("Mã booking không được để trống.");
        }
        if (roomFee == null || roomFee.signum() < 0) {
            throw new IllegalArgumentException("Tiền phòng phải lớn hơn hoặc bằng 0.");
        }
        if (serviceFee == null || serviceFee.signum() < 0) {
            throw new IllegalArgumentException("Tiền dịch vụ phải lớn hơn hoặc bằng 0.");
        }
        // Compute tax = 10% of (roomFee + serviceFee) and total = subtotal + tax
        BigDecimal subtotal = roomFee.add(serviceFee);
        tax = subtotal.multiply(new BigDecimal("0.10"));
        total = subtotal.add(tax);
    }
}
