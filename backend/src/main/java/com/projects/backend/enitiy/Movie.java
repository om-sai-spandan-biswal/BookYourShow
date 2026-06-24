package com.projects.backend.enitiy;

import com.projects.backend.enums.Genre;
import com.projects.backend.enums.Language;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private List<Genre> genres = new ArrayList<>();

    private Integer duration;

    @Enumerated(EnumType.STRING)
    private Language language;

    @OneToMany(mappedBy = "movie")
    private List<Show> shows = new ArrayList<>();
}