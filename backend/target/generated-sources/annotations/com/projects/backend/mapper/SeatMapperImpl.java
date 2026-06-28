package com.projects.backend.mapper;

import com.projects.backend.dto.ScreenDto;
import com.projects.backend.dto.SeatDto;
import com.projects.backend.dto.TheaterDto;
import com.projects.backend.enitiy.Screen;
import com.projects.backend.enitiy.Seat;
import com.projects.backend.enitiy.Theater;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-28T22:55:34+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.2 (Microsoft)"
)
@Component
public class SeatMapperImpl implements SeatMapper {

    @Override
    public Seat toEntity(SeatDto seatDto) {
        if ( seatDto == null ) {
            return null;
        }

        Seat seat = new Seat();

        seat.setId( seatDto.getId() );
        seat.setRowName( seatDto.getRowName() );
        seat.setSeatNumber( seatDto.getSeatNumber() );
        seat.setType( seatDto.getType() );
        seat.setScreen( screenDtoToScreen( seatDto.getScreen() ) );

        return seat;
    }

    @Override
    public SeatDto toDto(Seat seat) {
        if ( seat == null ) {
            return null;
        }

        SeatDto seatDto = new SeatDto();

        seatDto.setId( seat.getId() );
        seatDto.setRowName( seat.getRowName() );
        seatDto.setSeatNumber( seat.getSeatNumber() );
        seatDto.setType( seat.getType() );
        seatDto.setScreen( screenToScreenDto( seat.getScreen() ) );

        return seatDto;
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
}
