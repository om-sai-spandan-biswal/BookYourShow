package com.projects.BookYourShow.backend.user.mapper;

import com.projects.BookYourShow.backend.user.dto.UserRequest;
import com.projects.BookYourShow.backend.user.dto.UserResponse;
import com.projects.BookYourShow.backend.user.entity.Role;
import com.projects.BookYourShow.backend.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequest userRequest);

    UserResponse toResponse(User user);
    default String mapRole(Role role) {
        return role == null || role.getName() == null
                ? null
                : role.getName().name();
    }
}
