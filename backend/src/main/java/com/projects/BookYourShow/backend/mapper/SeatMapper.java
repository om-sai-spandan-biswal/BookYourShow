package com.projects.BookYourShow.backend.mapper;

import com.projects.BookYourShow.backend.dto.SeatRequest;
import com.projects.BookYourShow.backend.dto.SeatResponse;
import com.projects.BookYourShow.backend.entity.Seat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    Seat toEntity(SeatRequest seatRequest);
    SeatResponse toResponse(Seat seat);
}
