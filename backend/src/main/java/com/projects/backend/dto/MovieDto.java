package com.projects.backend.dto;

import com.projects.backend.enums.Genre;
import com.projects.backend.enums.Language;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieDto {
    private Long id;

    private String title;

    private List<Genre> genres = new ArrayList<>();

    private Integer duration;

    private Language language;

}
