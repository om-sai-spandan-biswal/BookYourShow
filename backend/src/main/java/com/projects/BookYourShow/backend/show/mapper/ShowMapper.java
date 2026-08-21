package com.projects.BookYourShow.backend.show.mapper;

import com.projects.BookYourShow.backend.show.dto.ShowRequest;
import com.projects.BookYourShow.backend.show.dto.ShowResponse;
import com.projects.BookYourShow.backend.show.entity.Show;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShowMapper {
    Show toEntity(ShowRequest showRequest);

    @Mapping(source = "movie.id", target = "movieId")
    @Mapping(source = "screen.id", target = "screenId")
    ShowResponse toResponse(Show show);
}
