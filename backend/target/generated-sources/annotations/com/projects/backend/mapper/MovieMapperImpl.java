package com.projects.backend.mapper;

import com.projects.backend.dto.MovieDto;
import com.projects.backend.enitiy.Movie;
import com.projects.backend.enums.Genre;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-28T22:55:34+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.2 (Microsoft)"
)
@Component
public class MovieMapperImpl implements MovieMapper {

    @Override
    public Movie toEntity(MovieDto movieDto) {
        if ( movieDto == null ) {
            return null;
        }

        Movie movie = new Movie();

        movie.setId( movieDto.getId() );
        movie.setTitle( movieDto.getTitle() );
        List<Genre> list = movieDto.getGenres();
        if ( list != null ) {
            movie.setGenres( new ArrayList<Genre>( list ) );
        }
        movie.setDuration( movieDto.getDuration() );
        movie.setLanguage( movieDto.getLanguage() );

        return movie;
    }

    @Override
    public MovieDto toDto(Movie movie) {
        if ( movie == null ) {
            return null;
        }

        MovieDto movieDto = new MovieDto();

        movieDto.setId( movie.getId() );
        movieDto.setTitle( movie.getTitle() );
        List<Genre> list = movie.getGenres();
        if ( list != null ) {
            movieDto.setGenres( new ArrayList<Genre>( list ) );
        }
        movieDto.setDuration( movie.getDuration() );
        movieDto.setLanguage( movie.getLanguage() );

        return movieDto;
    }
}
