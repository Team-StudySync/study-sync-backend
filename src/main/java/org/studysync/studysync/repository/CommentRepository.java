package org.studysync.studysync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.studysync.studysync.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
