package vn.rikkei.cinemabox.booking;

import vn.rikkei.cinemabox.common.ConflictException;

public final class BookingExceptions {
    private BookingExceptions() {}

    public static ConflictException notEnoughSeats() {
        return new ConflictException("NOT_ENOUGH_SEATS", "Suất chiếu không còn đủ ghế");
    }

    public static ConflictException invalidState() {
        return new ConflictException("INVALID_STATE", "Vé không ở trạng thái CONFIRMED");
    }
}
