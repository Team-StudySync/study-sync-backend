package org.studysync.studysync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.studysync.studysync.domain.StudyGroup;

public interface GroupRepository extends JpaRepository<StudyGroup, Long> {
}
