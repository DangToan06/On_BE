package vn.rikkei.cinemabox.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.rikkei.cinemabox.common.ResourceNotFoundException;
import vn.rikkei.cinemabox.customer.Customer;
import vn.rikkei.cinemabox.customer.CustomerRepository;
import vn.rikkei.cinemabox.payment.PaymentGateway;
import vn.rikkei.cinemabox.showtime.Showtime;
import vn.rikkei.cinemabox.showtime.ShowtimeRepository;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final ShowtimeRepository showtimeRepository;
    private final PaymentGateway paymentGateway;

    @Transactional
    public BookingResponse create(BookingRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));
        Showtime showtime = showtimeRepository.lockById(request.getShowtimeId())
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id: " + request.getShowtimeId()));

        if (showtime.getAvailableSeats() < request.getQuantity()) {
            throw BookingExceptions.notEnoughSeats();
        }

        long baseAmount = Math.round(showtime.getPrice()) * request.getQuantity();
        long totalAmount = "GOLD".equals(customer.getMemberLevel())
                ? (long) Math.floor(baseAmount * 0.90)
                : baseAmount;

        String paymentRef = paymentGateway.charge(customer.getId(), totalAmount);

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setShowtime(showtime);
        booking.setQuantity(request.getQuantity());
        booking.setTotalAmount(totalAmount);
        booking.setStatus("CONFIRMED");
        booking.setPaymentRef(paymentRef);
        showtime.setAvailableSeats(showtime.getAvailableSeats() - request.getQuantity());

        Booking saved = bookingRepository.save(booking);
        return toResponse(saved);
    }

    @Transactional
    public BookingResponse cancel(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
        if (!"CONFIRMED".equals(booking.getStatus())) {
            throw BookingExceptions.invalidState();
        }

        Showtime showtime = showtimeRepository.lockById(booking.getShowtime().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id: " + booking.getShowtime().getId()));
        booking.setStatus("CANCELLED");
        showtime.setAvailableSeats(showtime.getAvailableSeats() + booking.getQuantity());
        return toResponse(bookingRepository.save(booking));
    }

    private BookingResponse toResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .customerId(booking.getCustomer().getId())
                .showtimeId(booking.getShowtime().getId())
                .quantity(booking.getQuantity())
                .totalAmount(booking.getTotalAmount())
                .status(booking.getStatus())
                .paymentRef(booking.getPaymentRef())
                .createdAt(booking.getCreatedAt())
                .build();
    }
}
