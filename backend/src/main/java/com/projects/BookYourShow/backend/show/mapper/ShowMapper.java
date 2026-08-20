package com.projects.BookYourShow.backend.show.mapper;

import com.projects.BookYourShow.backend.show.dto.ShowRequest;
import com.projects.BookYourShow.backend.show.dto.ShowResponse;
import com.projects.BookYourShow.backend.show.entity.Show;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShowMapper {
    Show toEntity(ShowRequest showRequest);
    ShowResponse toResponse(Show show);
}
