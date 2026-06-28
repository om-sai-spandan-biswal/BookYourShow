package com.projects.backend.controller;

import com.projects.backend.dto.ScreenDto;
import com.projects.backend.service.ScreenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/screens")
@RequiredArgsConstructor
public class ScreenAdminController {
    private final ScreenService screenService ;

    @GetMapping(path = "/{screenId}")
    public ResponseEntity<ScreenDto> getScreen(@PathVariable Long screenId) {
        ScreenDto screenDto = screenService.getScreenById(screenId);
        return ResponseEntity.ok(screenDto);
    }

    @PostMapping(path = "/{theaterId}")
    public ResponseEntity<ScreenDto> createScreen(
            @PathVariable Long theaterId,
            @RequestBody ScreenDto screenDto) {
        ScreenDto screen = screenService.createScreen(theaterId, screenDto);
        return new ResponseEntity<>(screen, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{theaterId}/{screenId}")
    public ResponseEntity<ScreenDto> updateScreen(
            @PathVariable Long theaterId,
            @PathVariable Long screenId,
            @RequestBody ScreenDto screenDto) {
        ScreenDto screen = screenService.updateScreen(theaterId, screenId, screenDto);
        return ResponseEntity.ok(screen);
    }

    @DeleteMapping(path = "/{screenId}")
    public ResponseEntity<Void> deleteScreen(@PathVariable Long screenId) {
        screenService.deleteScreen(screenId);
        return ResponseEntity.noContent().build();
    }

}
