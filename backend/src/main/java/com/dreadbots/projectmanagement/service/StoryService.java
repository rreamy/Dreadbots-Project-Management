package com.dreadbots.projectmanagement.service;

import com.dreadbots.projectmanagement.dto.StoryRequest;
import com.dreadbots.projectmanagement.dto.StoryResponse;
import com.dreadbots.projectmanagement.entity.*;
import com.dreadbots.projectmanagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoryService {
    private final StoryRepository storyRepository;
    private final LabelRepository labelRepository;
    private final UserRepository userRepository;

    public List<StoryResponse> findAll() {
        return storyRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public StoryResponse findById(Long id) {
        return toResponse(storyRepository.findById(id).orElseThrow(() -> new RuntimeException("Story not found: " + id)));
    }

    public StoryResponse create(StoryRequest request, Long reporterId) {
        StoryEntity story = new StoryEntity();
        story.setSummary(request.getSummary());
        story.setDescription(request.getDescription());
        story.setStatus(StoryEntity.StoryStatus.valueOf(request.getStatus().toUpperCase()));
        story.setPriority(StoryEntity.StoryPriority.valueOf(request.getPriority().toUpperCase()));
        story.setDueDate(request.getDueDate());
        story.setReporter(userRepository.findById(reporterId).orElseThrow(() -> new RuntimeException("Reporter not found")));

        if (request.getAssigneeId() != null) {
            story.setAssignee(userRepository.findById(request.getAssigneeId()).orElseThrow(() -> new RuntimeException("Assignee not found")));
        }

        if (request.getLabelIds() != null) {
            story.setLabels(labelRepository.findAllById(request.getLabelIds()));
        }

        return toResponse(storyRepository.save(story));
    }

    public StoryResponse update(Long id, StoryRequest request) {
        StoryEntity story = storyRepository.findById(id).orElseThrow(() -> new RuntimeException("Story not found: " + id));
        story.setSummary(request.getSummary());
        story.setDescription(request.getDescription());
        story.setStatus(StoryEntity.StoryStatus.valueOf(request.getStatus().toUpperCase()));
        story.setPriority(StoryEntity.StoryPriority.valueOf(request.getPriority().toUpperCase()));
        story.setDueDate(request.getDueDate());

        if (request.getAssigneeId() != null) {
            story.setAssignee(userRepository.findById(request.getAssigneeId()).orElseThrow(() -> new RuntimeException("Assignee not found")));
        } else {
            story.setAssignee(null);
        }

        if (request.getLabelIds() != null) {
            story.setLabels(labelRepository.findAllById(request.getLabelIds()));
        }

        return toResponse(storyRepository.save(story));
    }

    public void delete(Long id) {
        storyRepository.deleteById(id);
    }

    private StoryResponse toResponse(StoryEntity story) {
        StoryResponse response = new StoryResponse();
        response.setId(story.getId());
        response.setSummary(story.getSummary());
        response.setDescription(story.getDescription());
        response.setStatus(story.getStatus().name());
        response.setPriority(story.getPriority().name());
        response.setAssigneeId(story.getAssignee() != null ? story.getAssignee().getId() : null);
        response.setReporterId(story.getReporter() != null ? story.getReporter().getId() : null);
        response.setDueDate(story.getDueDate());
        response.setCreatedAt(story.getCreatedAt());
        response.setUpdatedAt(story.getUpdatedAt());
        response.setLabelIds(story.getLabels() == null ? List.of() : story.getLabels().stream().map(LabelEntity::getId).collect(Collectors.toList()));
        return response;
    }
}
