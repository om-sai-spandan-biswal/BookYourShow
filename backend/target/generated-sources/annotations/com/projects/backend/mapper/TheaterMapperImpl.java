package com.projects.backend.mapper;

import com.projects.backend.dto.TheaterDto;
import com.projects.backend.enitiy.Theater;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-28T22:55:34+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.2 (Microsoft)"
)
@Component
public class TheaterMapperImpl implements TheaterMapper {

    @Override
    public Theater toEntity(TheaterDto theaterDto) {
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

    @Override
    public TheaterDto toDto(Theater theater) {
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
}
