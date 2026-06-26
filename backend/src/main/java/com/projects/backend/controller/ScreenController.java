package com.projects.backend.controller;

import com.projects.backend.service.ScreenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/screen")
@RequiredArgsConstructor
public class ScreenController {
    private final ScreenService screenService ;



}
