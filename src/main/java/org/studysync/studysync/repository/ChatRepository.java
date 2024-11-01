package org.studysync.studysync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.studysync.studysync.domain.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
