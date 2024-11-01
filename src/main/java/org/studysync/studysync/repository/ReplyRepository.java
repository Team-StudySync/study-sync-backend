package org.studysync.studysync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.studysync.studysync.domain.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
