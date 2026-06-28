package com.projects.backend.mapper;

import com.projects.backend.dto.MovieDto;
import com.projects.backend.dto.ScreenDto;
import com.projects.backend.dto.ShowDto;
import com.projects.backend.dto.TheaterDto;
import com.projects.backend.enitiy.Movie;
import com.projects.backend.enitiy.Screen;
import com.projects.backend.enitiy.Show;
import com.projects.backend.enitiy.Theater;
import com.projects.backend.enums.Genre;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-28T22:55:35+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.2 (Microsoft)"
)
@Component
public class ShowMapperImpl implements ShowMapper {

    @Override
    public ShowDto toDto(Show show) {
        if ( show == null ) {
            return null;
        }

        ShowDto showDto = new ShowDto();

        showDto.setId( show.getId() );
        showDto.setMovie( movieToMovieDto( show.getMovie() ) );
        showDto.setScreen( screenToScreenDto( show.getScreen() ) );
        showDto.setStartTime( show.getStartTime() );
        showDto.setEndTime( show.getEndTime() );
        showDto.setBasePrice( show.getBasePrice() );

        return showDto;
    }

    @Override
    public Show toEntity(ShowDto showDto) {
        if ( showDto == null ) {
            return null;
        }

        Show.ShowBuilder show = Show.builder();

        show.id( showDto.getId() );
        show.movie( movieDtoToMovie( showDto.getMovie() ) );
        show.screen( screenDtoToScreen( showDto.getScreen() ) );
        show.startTime( showDto.getStartTime() );
        show.endTime( showDto.getEndTime() );
        show.basePrice( showDto.getBasePrice() );

        return show.build();
    }

    protected MovieDto movieToMovieDto(Movie movie) {
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

    protected TheaterDto theaterToTheaterDto(Theater theater) {
        if ( theater == null ) {
            return null;
        }

        TheaterDto theaterDto = new TheaterDto();

        theaterDto.setId( theater.getId() );
        theaterDto.setName( theater.getName() );
        theaterDto.setCity( theater.getCity() );
        theaterDto.setAddress( theater.getAddress() );

        return theaterDto;
    }

    protected ScreenDto screenToScreenDto(Screen screen) {
        if ( screen == null ) {
            return null;
        }

        ScreenDto screenDto = new ScreenDto();

        screenDto.setId( screen.getId() );
        screenDto.setName( screen.getName() );
        screenDto.setTotalSeats( screen.getTotalSeats() );
        screenDto.setTheater( theaterToTheaterDto( screen.getTheater() ) );

        return screenDto;
    }

    protected Movie movieDtoToMovie(MovieDto movieDto) {
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

    protected Theater theaterDtoToTheater(TheaterDto theaterDto) {
        if ( theaterDto == null ) {
            return null;
        }

        Theater.TheaterBuilder theater = Theater.builder();

        theater.id( theaterDto.getId() );
        theater.name( theaterDto.getName() );
        theater.city( theaterDto.getCity() );
        theater.address( theaterDto.getAddress() );

        return theater.build();
    }

    protected Screen screenDtoToScreen(ScreenDto screenDto) {
        if ( screenDto == null ) {
            return null;
        }

        Screen.ScreenBuilder screen = Screen.builder();

        screen.id( screenDto.getId() );
        screen.name( screenDto.getName() );
        screen.totalSeats( screenDto.getTotalSeats() );
        screen.theater( theaterDtoToTheater( screenDto.getTheater() ) );

        return screen.build();
    }
}
