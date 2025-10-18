package dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import entity.Booking;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    private String bookingId;
    private String roomId;
    private String customerId;
    private LocalDateTime CheckIn;
    private LocalDateTime CheckOut;
    private Booking.BookingStatus status;

    public Booking toEntity(){
        Booking booking = new Booking();
        booking.setBookingId(this.bookingId);
        booking.setRoomId(this.roomId);
        booking.setCustomerId(this.customerId);
        booking.setCheckIn(this.CheckIn);
        booking.setCheckOut(this.CheckOut);
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
        if (CheckIn != null && CheckOut != null && !CheckOut.isAfter(CheckIn)) {
            throw new IllegalArgumentException("Ngày trả phòng (check-out) phải sau ngày nhận phòng (check-in).");
        }
    }
}
