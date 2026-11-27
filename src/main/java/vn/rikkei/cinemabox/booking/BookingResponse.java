package vn.rikkei.cinemabox.booking;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long id;
    private Long customerId;
    private Long showtimeId;
    private Integer quantity;
    private Long totalAmount;
    private String status;
    private String paymentRef;
    private LocalDateTime createdAt;
}
