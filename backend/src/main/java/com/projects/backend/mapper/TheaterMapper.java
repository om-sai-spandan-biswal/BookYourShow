package com.projects.backend.mapper;

import com.projects.backend.dto.TheaterDto;
import com.projects.backend.enitiy.Theater;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TheaterMapper {
    Theater toEntity(TheaterDto theaterDto);
    TheaterDto toDto(Theater theater);
}
