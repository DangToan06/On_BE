package vn.rikkei.cinemabox.showtime;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.rikkei.cinemabox.common.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class ShowtimeService {

    private final ShowtimeRepository showtimeRepository;

    @Transactional(readOnly = true)
    public ShowtimeResponse getShowtimeById(Long id) {

        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id: " + id));

        return ShowtimeResponse.builder()
                .id(showtime.getId())
                .movieTitle(showtime.getMovie().getTitle())
                .roomName(showtime.getRoom().getName())
                .startTime(showtime.getStartTime())
                .price(showtime.getPrice())
                .availableSeats(showtime.getAvailableSeats())
                .build();
    }

}
