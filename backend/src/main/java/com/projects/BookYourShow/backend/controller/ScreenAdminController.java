package com.projects.BookYourShow.backend.controller;

import com.projects.BookYourShow.backend.dto.ScreenRequest;
import com.projects.BookYourShow.backend.dto.ScreenResponse;
import com.projects.BookYourShow.backend.service.ScreenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/admin/screens")
@RequiredArgsConstructor
public class ScreenAdminController {
    private final ScreenService screenService;


    @PutMapping("/{screenId}")
    public ResponseEntity<ScreenResponse> updateScreen(
            @PathVariable Long screenId,
            @Valid @RequestBody ScreenRequest request)
    {
        ScreenResponse response = screenService.updateScreen(screenId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{screenId}")
    public ResponseEntity<Void> deleteScreen(
            @PathVariable Long screenId)
    {
        screenService.deleteScreen(screenId);
        return ResponseEntity.noContent().build();
    }
}
