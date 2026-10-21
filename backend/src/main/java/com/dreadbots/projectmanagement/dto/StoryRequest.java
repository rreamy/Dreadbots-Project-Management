package com.dreadbots.projectmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoryRequest {
    private String summary;
    private String description;
    private String status;
    private String priority;
    private Long assigneeId;
    private LocalDate dueDate;
    private List<Long> labelIds;
}
