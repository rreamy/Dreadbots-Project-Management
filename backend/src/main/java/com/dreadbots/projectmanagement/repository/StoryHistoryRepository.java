package com.dreadbots.projectmanagement.repository;

import com.dreadbots.projectmanagement.entity.StoryHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryHistoryRepository extends JpaRepository<StoryHistoryEntity, Long> {
}
