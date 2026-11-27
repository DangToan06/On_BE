package vn.rikkei.cinemabox.showtime;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ShowtimeResponse {
    private Long id;
    private String movieTitle;
    private String roomName;
    private LocalDateTime startTime;
    private Double price;
    private Integer availableSeats;
}
