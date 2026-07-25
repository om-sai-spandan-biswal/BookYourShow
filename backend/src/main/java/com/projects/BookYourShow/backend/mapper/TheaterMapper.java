package com.projects.BookYourShow.backend.mapper;

import com.projects.BookYourShow.backend.dto.TheaterRequest;
import com.projects.BookYourShow.backend.dto.TheaterResponse;
import com.projects.BookYourShow.backend.entity.Theater;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TheaterMapper {
    Theater toEntity(TheaterRequest request) ;
    TheaterResponse toResponse(Theater theater) ;
}
