package org.studysync.studysync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.studysync.studysync.domain.StudyGroup;

import java.util.Optional;

public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {
    Optional<StudyGroup> findByUuid(String uuid);
}
