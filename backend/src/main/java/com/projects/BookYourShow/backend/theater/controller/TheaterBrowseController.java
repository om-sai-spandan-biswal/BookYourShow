package com.projects.BookYourShow.backend.theater.controller;

import com.projects.BookYourShow.backend.show.service.ShowService;
import com.projects.BookYourShow.backend.theater.dto.TheaterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/theaters")
@RequiredArgsConstructor
public class TheaterBrowseController {
    private final ShowService showService;

//    @GetMapping()
//    public ResponseEntity<List<TheaterResponse>> browseTheaters() {
//        Page<TheaterResponse> hotels = showService.searchTheaters(
//    }
}
