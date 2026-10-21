package com.dreadbots.projectmanagement.repository;

import com.dreadbots.projectmanagement.entity.StoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<StoryEntity, Long> {
}
