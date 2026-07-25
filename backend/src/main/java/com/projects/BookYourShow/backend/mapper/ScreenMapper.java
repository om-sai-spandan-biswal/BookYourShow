package com.projects.BookYourShow.backend.mapper;

import com.projects.BookYourShow.backend.dto.ScreenRequest;
import com.projects.BookYourShow.backend.dto.ScreenResponse;
import com.projects.BookYourShow.backend.entity.Screen;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScreenMapper {
    Screen toEntity(ScreenRequest screenRequest);
    ScreenResponse toResponse(Screen screen);
}
