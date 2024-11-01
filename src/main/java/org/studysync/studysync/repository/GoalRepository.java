package org.studysync.studysync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.studysync.studysync.domain.Goal;

public interface GoalRepository extends JpaRepository<Goal, Long> {
}
