package com.projects.BookYourShow.backend.show.mapper;

import com.projects.BookYourShow.backend.show.dto.ShowSeatResponse;
import com.projects.BookYourShow.backend.show.entity.ShowSeat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShowSeatMapper {

    @Mapping(source = "show.id", target = "showId")
    @Mapping(source = "seat.id", target = "seatId")
    ShowSeatResponse toResponse(ShowSeat showSeat);
}
