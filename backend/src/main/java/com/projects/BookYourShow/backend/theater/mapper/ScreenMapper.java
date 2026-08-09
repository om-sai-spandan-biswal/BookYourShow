package com.projects.BookYourShow.backend.theater.mapper;

import com.projects.BookYourShow.backend.theater.dto.ScreenRequest;
import com.projects.BookYourShow.backend.theater.dto.ScreenResponse;
import com.projects.BookYourShow.backend.theater.entity.Screen;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScreenMapper {
    Screen toEntity(ScreenRequest screenRequest);
    ScreenResponse toResponse(Screen screen);
}
