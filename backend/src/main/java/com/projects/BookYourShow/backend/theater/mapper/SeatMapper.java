package com.projects.BookYourShow.backend.theater.mapper;

import com.projects.BookYourShow.backend.theater.dto.SeatRequest;
import com.projects.BookYourShow.backend.theater.dto.SeatResponse;
import com.projects.BookYourShow.backend.theater.entity.Seat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    Seat toEntity(SeatRequest seatRequest);
    SeatResponse toResponse(Seat seat);
}
