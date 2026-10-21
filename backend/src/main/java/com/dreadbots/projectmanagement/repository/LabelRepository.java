package com.dreadbots.projectmanagement.repository;

import com.dreadbots.projectmanagement.entity.LabelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelRepository extends JpaRepository<LabelEntity, Long> {
}
