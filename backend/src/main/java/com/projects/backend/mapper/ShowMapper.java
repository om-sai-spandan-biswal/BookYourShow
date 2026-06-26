package com.projects.backend.mapper;

import com.projects.backend.dto.ShowDto;
import com.projects.backend.enitiy.Show;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShowMapper {
    ShowDto toDto(Show show);
    Show toEntity(ShowDto showDto);
}
