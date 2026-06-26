package com.projects.backend.mapper;

import com.projects.backend.dto.ScreenDto;
import com.projects.backend.enitiy.Screen;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScreenMapper {
    ScreenDto toDto(Screen screen);
    Screen toEntity(ScreenDto screenDto);
}
