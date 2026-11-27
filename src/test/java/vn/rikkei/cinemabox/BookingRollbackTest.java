package vn.rikkei.cinemabox;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import vn.rikkei.cinemabox.booking.BookingRepository;
import vn.rikkei.cinemabox.showtime.ShowtimeRepository;

@SpringBootTest
@AutoConfigureMockMvc
class BookingRollbackTest {
    @Autowired MockMvc mvc;
    @Autowired ShowtimeRepository showtimeRepository;
    @Autowired BookingRepository bookingRepository;

    @Test
    void paymentRejectedRollsBackSeatsAndConfirmedBooking() throws Exception {
        var showtime = showtimeRepository.findById(1L).orElseThrow();
        int seatsBefore = showtime.getAvailableSeats();
        long confirmedBefore = bookingRepository.findAll().stream()
                .filter(b -> b.getShowtime().getId().equals(1L)
                        && "CONFIRMED".equals(b.getStatus()))
                .count();

        mvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\":1,\"showtimeId\":1,\"quantity\":10}"))
                .andExpect(status().isPaymentRequired())
                .andExpect(jsonPath("$.error").value("PAYMENT_FAILED"));

        showtime = showtimeRepository.findById(1L).orElseThrow();
        long confirmedAfter = bookingRepository.findAll().stream()
                .filter(b -> b.getShowtime().getId().equals(1L)
                        && "CONFIRMED".equals(b.getStatus()))
                .count();
        assertEquals(seatsBefore, showtime.getAvailableSeats());
        assertEquals(confirmedBefore, confirmedAfter);
    }
}
