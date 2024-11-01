package org.studysync.studysync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.studysync.studysync.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
