package com.dreadbots.projectmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoryResponse {
    private Long id;
    private String summary;
    private String description;
    private String status;
    private String priority;
    private Long assigneeId;
    private Long reporterId;
    private LocalDate dueDate;
    private List<Long> labelIds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
