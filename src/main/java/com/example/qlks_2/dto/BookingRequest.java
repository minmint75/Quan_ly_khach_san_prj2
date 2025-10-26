package com.example.qlks_2.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.example.qlks_2.entity.Booking;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    private Long bookingId;
    private Long roomId;
    private Long customerId;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Booking.BookingStatus status;

    public Booking toEntity(){
        Booking booking = new Booking();
        booking.setBookingId(this.bookingId);
        booking.setRoomId(this.roomId);
        booking.setCustomerId(this.customerId);
        booking.setCheckIn(this.checkIn);
        booking.setCheckOut(this.checkOut);
        booking.setStatus(this.status);
        return booking;
    }

    public static BookingRequest fromEntity(Booking booking){
        BookingRequest request = new BookingRequest();
        request.setBookingId(booking.getBookingId());
        request.setRoomId(booking.getRoomId());
        request.setCustomerId(booking.getCustomerId());
        request.setCheckIn(booking.getCheckIn());
        request.setCheckOut(booking.getCheckOut());
        request.setStatus(booking.getStatus());
        return request;
    }

    public void validateDates() {
        if (checkIn != null && checkOut != null && !checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Ngày trả phòng (check-out) phải sau ngày nhận phòng (check-in).");
        }
    }
}
