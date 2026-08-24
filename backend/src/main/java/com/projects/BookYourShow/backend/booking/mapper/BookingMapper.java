package com.projects.BookYourShow.backend.booking.mapper;

import com.projects.BookYourShow.backend.booking.dto.BookingResponse;
import com.projects.BookYourShow.backend.booking.entity.Booking;
import com.projects.BookYourShow.backend.booking.entity.BookingSeat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "showSeatIds", source = "bookingSeats", qualifiedByName = "getShowSeatIds")
    BookingResponse toResponse(Booking booking);

    @Named("getShowSeatIds")
    default List<Long> getShowSeatIds(List<BookingSeat> bookingSeats) {
        if (bookingSeats == null) {
            return List.of();
        }
        return bookingSeats.stream()
                .map(bookingSeat -> bookingSeat.getShowSeat().getId())
                .toList();
    }
}
