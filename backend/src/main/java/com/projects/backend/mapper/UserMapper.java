package com.projects.backend.mapper;

import com.projects.backend.dto.UserDto;
import com.projects.backend.enitiy.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto userDto);
}
