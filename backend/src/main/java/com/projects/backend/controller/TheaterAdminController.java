package com.projects.backend.controller;

import com.projects.backend.dto.TheaterDto;
import com.projects.backend.service.TheaterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/theaters")
@RequiredArgsConstructor
public class TheaterAdminController {
    private final TheaterService theaterService;

    @GetMapping
    public ResponseEntity<List<TheaterDto>> getAllTheaters() {
        List<TheaterDto> theaters = theaterService.getAllTheaters();
        return ResponseEntity.ok(theaters);
    }

    @GetMapping(path = "/{theaterId}")
    public ResponseEntity<TheaterDto> getTheaterById(@PathVariable Long theaterId) {
        TheaterDto theaterDto = theaterService.getTheaterById(theaterId);
        return ResponseEntity.ok(theaterDto);
    }

    @PostMapping
    public ResponseEntity<TheaterDto> createTheater(@RequestBody TheaterDto theaterDto) {
        TheaterDto createdDto = theaterService.createTheater(theaterDto);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{theaterId}")
    public ResponseEntity<TheaterDto> updateTheater(
            @PathVariable Long theaterId,
            @RequestBody TheaterDto theaterDto) {
        TheaterDto updatedDto = theaterService.updateTheater(theaterId, theaterDto);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping(path = "/{theaterId}")
    public ResponseEntity<Void> deleteTheater(@PathVariable Long theaterId) {
        theaterService.deleteTheater(theaterId);
        return ResponseEntity.noContent().build();
    }
}
