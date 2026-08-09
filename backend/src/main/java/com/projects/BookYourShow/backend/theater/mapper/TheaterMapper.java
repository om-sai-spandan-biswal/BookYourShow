package com.projects.BookYourShow.backend.theater.mapper;

import com.projects.BookYourShow.backend.theater.dto.TheaterRequest;
import com.projects.BookYourShow.backend.theater.dto.TheaterResponse;
import com.projects.BookYourShow.backend.theater.entity.Theater;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TheaterMapper {
    Theater toEntity(TheaterRequest request) ;
    TheaterResponse toResponse(Theater theater) ;
}
