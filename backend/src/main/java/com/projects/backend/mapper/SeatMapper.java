package com.projects.backend.mapper;

import com.projects.backend.dto.SeatDto;
import com.projects.backend.enitiy.Seat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    Seat toEntity(SeatDto seatDto);
    SeatDto toDto(Seat seat);
}
