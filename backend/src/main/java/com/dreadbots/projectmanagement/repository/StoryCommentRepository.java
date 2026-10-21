package com.dreadbots.projectmanagement.repository;

import com.dreadbots.projectmanagement.entity.StoryCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryCommentRepository extends JpaRepository<StoryCommentEntity, Long> {
}
