package com.projects.BookYourShow.backend.movie.entity;

import com.projects.BookYourShow.backend.show.entity.Show;
import com.projects.BookYourShow.backend.movie.enums.Genre;
import com.projects.BookYourShow.backend.movie.enums.Language;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ElementCollection
    @CollectionTable(
            name = "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private Set<Genre> genres = new HashSet<>();

    private Double duration;

    @Enumerated(EnumType.STRING)
    private Language language;

    @OneToMany(mappedBy = "movie")
    private List<Show> shows = new ArrayList<>();

    @Column(nullable = false)
    private boolean deleted;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}