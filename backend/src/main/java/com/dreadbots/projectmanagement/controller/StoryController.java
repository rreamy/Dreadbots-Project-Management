package com.dreadbots.projectmanagement.controller;

import com.dreadbots.projectmanagement.dto.StoryRequest;
import com.dreadbots.projectmanagement.dto.StoryResponse;
import com.dreadbots.projectmanagement.entity.UserEntity;
import com.dreadbots.projectmanagement.service.StoryService;
import com.dreadbots.projectmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StoryController {
    private final StoryService storyService;
    private final UserService userService;

    @GetMapping("/stories")
    public ResponseEntity<List<StoryResponse>> getStories() {
        return ResponseEntity.ok(storyService.findAll());
    }

    @GetMapping("/stories/{id}")
    public ResponseEntity<StoryResponse> getStory(@PathVariable Long id) {
        return ResponseEntity.ok(storyService.findById(id));
    }

    @PostMapping("/stories")
    public ResponseEntity<StoryResponse> createStory(@RequestBody StoryRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity reporter = userService.findByUsername(username);
        return ResponseEntity.ok(storyService.create(request, reporter.getId()));
    }

    @PutMapping("/stories/{id}")
    public ResponseEntity<StoryResponse> updateStory(@PathVariable Long id, @RequestBody StoryRequest request) {
        return ResponseEntity.ok(storyService.update(id, request));
    }

    @DeleteMapping("/stories/{id}")
    public ResponseEntity<Void> deleteStory(@PathVariable Long id) {
        storyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
