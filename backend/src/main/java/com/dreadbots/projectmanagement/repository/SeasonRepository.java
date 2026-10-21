package com.dreadbots.projectmanagement.repository;

import com.dreadbots.projectmanagement.entity.SeasonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeasonRepository extends JpaRepository<SeasonEntity, Long> {
}
