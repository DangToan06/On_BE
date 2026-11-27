package vn.rikkei.cinemabox.booking;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class BookingRequest {
    @NotNull(message = "customerId is required")
    private Long customerId;

    @NotNull(message = "showtimeId is required")
    private Long showtimeId;

    @NotNull(message = "quantity is required")
    @Min(value = 1, message = "Quantity must be between 1 and 10")
    @Max(value = 10, message = "Quantity must be between 1 and 10")
    private Integer quantity;

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public Long getShowtimeId() { return showtimeId; }
    public void setShowtimeId(Long showtimeId) { this.showtimeId = showtimeId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
